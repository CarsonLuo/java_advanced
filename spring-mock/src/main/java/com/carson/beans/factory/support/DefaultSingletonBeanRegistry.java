package com.carson.beans.factory.support;

import com.carson.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author carson_luo
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String, Object> singleObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singleObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singleObjects.put(beanName, singletonObject);
    }
}
