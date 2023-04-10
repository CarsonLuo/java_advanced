package com.carson.beans.factory.support;

import com.carson.beans.exception.BeansException;
import com.carson.core.io.Resource;
import com.carson.core.io.ResourceLoader;

/**
 * 读取 Bean 定义信息的抽象接口
 * <p>
 * 1. 需要有获取资源的能力
 * 2. 读取Bean定义信息后, 往容器中注册BeanDefinition
 *
 * @author carson_luo
 */
public interface BeanDefinitionReader {

    ResourceLoader getResourceLoader();

    BeanDefinitionRegistry getRegistry();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String[] locations) throws BeansException;
}
