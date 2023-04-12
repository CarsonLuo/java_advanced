package com.carson.context.event;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.BeanFactory;
import com.carson.context.ApplicationEvent;
import com.carson.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author carson_luo
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            if (!supportEvent(applicationListener, event)) {
                continue;
            }
            applicationListener.onApplicationEvent(event);
        }
    }

    /**
     * 监听器是否对事件感兴趣
     */
    protected boolean supportEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Type type = applicationListener.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name : " + className);
        }
        return eventClassName.isAssignableFrom(event.getClass());
    }
}
