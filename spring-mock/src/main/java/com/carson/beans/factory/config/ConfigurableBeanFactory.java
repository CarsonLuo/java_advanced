package com.carson.beans.factory.config;

import com.carson.beans.factory.HierarchicalBeanFactory;

/**
 * @author carson_luo
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * 添加 BeanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例Bean
     */
    void destroySingletons();
}
