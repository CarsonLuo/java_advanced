package com.carson.beans.factory;

/**
 * @author carson_luo
 */
public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
