package com.carson.context;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.Aware;

/**
 * 实现该接口的类能感知所属的ApplicationContext
 *
 * @author carson_luo
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
