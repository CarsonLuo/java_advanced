package com.carson.beans.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author carson_luo
 */
public class SimpleBeanFactory {

    private Map<String, Object> map = new ConcurrentHashMap<>();

    public void registerBean(String name, Object bean) {
        map.put(name, bean);
    }

    public Object getBean(String name) {
        return map.get(name);
    }
}
