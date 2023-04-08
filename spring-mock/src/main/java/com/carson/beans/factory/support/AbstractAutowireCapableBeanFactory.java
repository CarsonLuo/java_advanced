package com.carson.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.carson.beans.PropertyValue;
import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.AutowireCapableBeanFactory;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanReference;

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

        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
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
                BeanUtil.setFieldValue(bean, propertyValue.getName(), value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values for bean : " + beanName, e);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
