package com.carson.beans.factory.config;

import com.carson.beans.factory.HierarchicalBeanFactory;

/**
 * @author carson_luo
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
