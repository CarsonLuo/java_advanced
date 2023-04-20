package com.carson.beans.factory.config;

import com.carson.beans.exception.BeansException;

/**
 * BeanPostProcessor 处理阶段可以修改和替换bean, 正好可以在此阶段返回代理对象替换原对象
 *
 * @author carson_luo
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在bean实例化之前执行
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;
}
