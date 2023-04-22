package com.carson.beans.factory.config;

import com.carson.beans.PropertyValues;
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

    /**
     * Bean实例化之后, 设置属性之前执行
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;
}
