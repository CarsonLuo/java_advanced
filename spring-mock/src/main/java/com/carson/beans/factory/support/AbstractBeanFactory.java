package com.carson.beans.factory.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.ConfigurableBeanFactory;

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

    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name, beanDefinition);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return ((T) getBean(name));
    }
}
