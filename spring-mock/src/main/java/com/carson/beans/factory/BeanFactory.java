package com.carson.beans.factory;

import com.carson.beans.exception.BeansException;

/**
 * bean 容器
 *
 * @author carson_luo
 */
public interface BeanFactory {

    /**
     * 获取bean
     *
     * @param name
     * @return Bean
     * @throws BeansException 当bean不存在时
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据名称和类型查找bean
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
