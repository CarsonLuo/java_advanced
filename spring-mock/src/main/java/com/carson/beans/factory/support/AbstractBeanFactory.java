package com.carson.beans.factory.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.FactoryBean;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.BeanPostProcessor;
import com.carson.beans.factory.config.ConfigurableBeanFactory;
import com.carson.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author carson_luo
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    /**
     * 抽象方法: 创建Bean
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    /**
     * 抽象方法: 根据 BeanName 获取Bean定义信息
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object bean = getSingleton(beanName);
        if (bean != null) {
            return getObjectForBeanInstance(bean, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Object createdBean = createBean(beanName, beanDefinition);
        return getObjectForBeanInstance(createdBean, beanName);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 有则覆盖
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver valueResolver : this.embeddedValueResolvers) {
            result = valueResolver.resolveStringValue(value);
        }
        return result;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 不是 FactoryBean 类型的, 直接返回
        if (!(beanInstance instanceof FactoryBean<?> factoryBean)) {
            return beanInstance;
        }
        try {
            // 如果该 FactoryBean 不是单例, 则创建
            if (!factoryBean.isSingleton()) {
                return factoryBean.getObject();
            }
            // 单例FactoryBean, 看下缓存中有没有, 没有则创建&存入缓存
            Object existFactoryBean = factoryBeanObjectCache.get(beanName);
            if (existFactoryBean != null) {
                return existFactoryBean;
            }
            Object createdFactoryBean = factoryBean.getObject();
            factoryBeanObjectCache.put(beanName, createdFactoryBean);
            return createdFactoryBean;
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }
}
