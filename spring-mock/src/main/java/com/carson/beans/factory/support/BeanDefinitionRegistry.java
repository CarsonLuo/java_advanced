package com.carson.beans.factory.support;

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
}
