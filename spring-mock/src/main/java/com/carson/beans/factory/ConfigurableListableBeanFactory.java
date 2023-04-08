package com.carson.beans.factory;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.AutowireCapableBeanFactory;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author carson_luo
 */
public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory, ListableBeanFactory, AutowireCapableBeanFactory {

    /**
     * 根据名称查找 BeanDefinition
     *
     * @param beanName Bean名称
     * @return Bean定义信息
     * @throws BeansException 当找不到BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
