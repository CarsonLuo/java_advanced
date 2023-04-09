package com.carson.beans.factory.config;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.ConfigurableListableBeanFactory;

/**
 * 容器扩展机制: 允许自定义修改BeanDefinition的属性值
 * <p/>
 * postProcessor : 后处理程序
 *
 * @author carson_luo
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有BeanDefinition加载完成后, 但在Bean实例化之前, 提供修改BeanDefinition属性值的机制
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
