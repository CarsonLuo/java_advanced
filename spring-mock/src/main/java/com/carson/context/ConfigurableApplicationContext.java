package com.carson.context;

import com.carson.beans.exception.BeansException;

/**
 * @author carson_luo
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     */
    void refresh() throws BeansException;
}
