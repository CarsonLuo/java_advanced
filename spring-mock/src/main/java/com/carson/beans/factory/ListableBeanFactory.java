package com.carson.beans.factory;

import com.carson.beans.exception.BeansException;

import java.util.Map;

/**
 * Listable : 能列在单子上的
 *
 * @author carson_luo
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 返回指定类型的所有实例
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回定义的所有bean的名称
     */
    String[] getBeanDefinitionNames();
}
