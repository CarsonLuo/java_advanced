package com.carson.beans.factory.config;

import com.carson.beans.factory.HierarchicalBeanFactory;
import com.carson.util.StringValueResolver;

/**
 * @author carson_luo
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * 添加 BeanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例Bean
     */
    void destroySingletons();

    /**
     * 添加内嵌值解析器, e.g @Value("${name}")
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 解析内嵌值变量
     */
    String resolveEmbeddedValue(String value);
}
