package com.carson.beans.factory;

import com.carson.beans.exception.BeansException;

/**
 * 实现该接口的类能感知所属的BeanFactory
 *
 * @author carson_luo
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
