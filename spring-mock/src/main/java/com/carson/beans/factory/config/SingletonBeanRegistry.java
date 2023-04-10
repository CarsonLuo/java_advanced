package com.carson.beans.factory.config;

/**
 * 单例注册表
 *
 * @author carson_luo
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);
}
