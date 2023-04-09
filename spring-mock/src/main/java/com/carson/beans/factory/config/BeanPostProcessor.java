package com.carson.beans.factory.config;

import com.carson.beans.exception.BeansException;

/**
 * 用于修改实例化后的bean的修改扩展点
 * 容器扩展机制: 在 Bean实例化后修改Bean或替换Bean, 是后面实现AOP的关键
 *
 * @author carson_luo
 */
public interface BeanPostProcessor {

    /**
     * 在 bean 执行初始化方法之前执行此方法
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在 bean 执行初始化方法之后执行此方法
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
