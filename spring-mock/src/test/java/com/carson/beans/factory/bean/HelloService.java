package com.carson.beans.factory.bean;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.BeanFactory;
import com.carson.beans.factory.BeanFactoryAware;
import com.carson.context.ApplicationContext;
import com.carson.context.ApplicationContextAware;

/**
 * @author carson_luo
 */
public class HelloService implements BeanFactoryAware, ApplicationContextAware {

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
