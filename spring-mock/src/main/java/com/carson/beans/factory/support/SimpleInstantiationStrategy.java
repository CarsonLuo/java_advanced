package com.carson.beans.factory.support;

import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.exception.BeansException;

import java.lang.reflect.Constructor;

/**
 * 使用Bean的构造函数来实例化
 *
 * @author carson_luo
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    /**
     * 简单的Bean实例化策略, 根据Bean的无参构造器函数实例化对象
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            Constructor<?> declaredConstructor = beanClass.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate [ " + beanClass.getName() + " ]", e);
        }
    }
}
