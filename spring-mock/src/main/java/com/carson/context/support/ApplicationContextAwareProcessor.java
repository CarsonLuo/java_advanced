package com.carson.context.support;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.config.BeanPostProcessor;
import com.carson.context.ApplicationContext;
import com.carson.context.ApplicationContextAware;

/**
 * @author carson_luo
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware applicationContextAwareBean) {
            System.out.printf("""
                    ApplicationContextAwareProcessor#postProcessBeforeInitialization(ApplicationContextAware) -> beanName: %s
                    """, beanName);
            applicationContextAwareBean.setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
