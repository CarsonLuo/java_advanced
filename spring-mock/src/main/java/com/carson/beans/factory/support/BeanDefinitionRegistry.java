package com.carson.beans.factory.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition 注册表接口
 *
 * @author carson_luo
 */
public interface BeanDefinitionRegistry {

    /**
     * 向注册表中注册BeanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据名称查找BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 是否包含指定名称的BeanDefinition
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 返回定义的所有bean的名称
     */
    String[] getBeanDefinitionNames();
}
