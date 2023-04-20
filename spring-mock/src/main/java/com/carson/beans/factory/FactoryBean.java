package com.carson.beans.factory;

/**
 * FactoryBean 是一种特殊的bean
 * 当向容器获取该bean时, 返回的是 #getObject()的返回值
 *
 * @author carson_luo
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    boolean isSingleton();
}
