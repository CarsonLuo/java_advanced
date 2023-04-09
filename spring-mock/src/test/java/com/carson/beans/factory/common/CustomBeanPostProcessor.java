package com.carson.beans.factory.common;

import com.carson.beans.exception.BeansException;
import com.carson.beans.factory.bean.Car;
import com.carson.beans.factory.config.BeanPostProcessor;

/**
 * @author carson_luo
 */
public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomBeanPostProcessor#postProcessBeforeInitialization");
        if (!beanName.equals("car")) {
            return bean;
        }
        Car car = (Car) bean;
        car.setSpeed(999.99);
        return car;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomBeanPostProcessor#postProcessAfterInitialization");
        return bean;
    }
}
