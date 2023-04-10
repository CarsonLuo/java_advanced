package com.carson.beans.factory.support;

import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.exception.BeansException;

/**
 * Bean 的实例化策略
 * <p>
 * beanClass.newInstance() 仅使用于bean存在无参构造函数的情况
 * <p>
 * 针对Bean的实例化, 抽象出该实例化策略的接口
 *
 * @author carson_luo
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
