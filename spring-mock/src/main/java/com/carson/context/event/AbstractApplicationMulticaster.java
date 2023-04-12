package com.carson.context.event;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.BeanFactory;
import com.carson.beans.factory.BeanFactoryAware;
import com.carson.context.ApplicationEvent;
import com.carson.context.ApplicationListener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author carson_luo
 */
public abstract class AbstractApplicationMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
