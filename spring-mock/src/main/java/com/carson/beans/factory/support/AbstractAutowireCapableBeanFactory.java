package com.carson.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.carson.beans.PropertyValue;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.DisposableBean;
import com.carson.beans.factory.InitializingBean;
import com.carson.beans.factory.config.AutowireCapableBeanFactory;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanPostProcessor;
import com.carson.beans.factory.config.BeanReference;

import java.lang.reflect.Method;

/**
 * @author carson_luo
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean;
        try {
            // v1 版本
            // 仅使用于bean存在无参构造函数的情况
            // Class beanClass = beanDefinition.getBeanClass();
            // beanClass.newInstance()

            // v2 版本 引入实例化策略 strategy
            bean = createBeanInstance(beanDefinition);

            // 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);

            // v3 版本 引入 BeanPostProcessor
            bean = initializeBean(beanName, bean, beanDefinition);

        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册有销毁方法的Bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 缓存Bean
        addSingleton(beanName, bean);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
                // v1 版本, 仅是简单填充属性
                // BeanUtil.setFieldValue(bean, propertyValue.getName(), propertyValue.getValue());

                // v2 版本, 新增BeanReference, 包装一个Bean对另一个Bean的引用
                // 实例化BeanA后填充属性时, 若PropertyValue#value为BeanReference, 则先去实例化BeanB
                // 目前不考虑循环依赖
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }

                // 通过反射设置属性
                BeanUtil.setFieldValue(bean, propertyValue.getName(), value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values for bean : " + beanName, e);
        }
    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {

        // 执行 BeanPostProcessor 的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        try {
            // 执行 Bean 的初始化方法
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Throwable ex) {
            throw new BeansException("Invocation of init method of bean [" + beanName + "] failed", ex);
        }

        //执行 BeanPostProcessor 的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

        return wrappedBean;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object resultBean = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object currentBean = beanPostProcessor.postProcessBeforeInitialization(existingBean, beanName);
            if (currentBean == null) {
                return resultBean;
            }
            resultBean = currentBean;
        }
        return resultBean;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object resultBean = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object currentBean = beanPostProcessor.postProcessAfterInitialization(existingBean, beanName);
            if (currentBean == null) {
                return resultBean;
            }
            resultBean = currentBean;
        }
        return resultBean;
    }

    /**
     * 执行 Bean 的初始化方法
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Throwable {
        if (bean instanceof InitializingBean initializingBean) {
            initializingBean.afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isEmpty(initMethodName)) {
            return;
        }
        Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getBeanClass(), initMethodName);
        if (initMethod == null) {
            throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
        }
        initMethod.invoke(bean);
    }

    /**
     * 注册有销毁方法的Bean, 即继承自DisposableBean 或者 有自定义的销毁方法
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (!(bean instanceof DisposableBean) && StrUtil.isEmpty(beanDefinition.getDestroyMethodName())) {
            return;
        }
        registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
