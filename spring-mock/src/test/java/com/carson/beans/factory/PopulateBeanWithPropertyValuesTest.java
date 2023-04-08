package com.carson.beans.factory;

import com.carson.beans.PropertyValue;
import com.carson.beans.PropertyValues;
import com.carson.beans.factory.bean.Car;
import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void testPopulateBeanWithPropertyValues() {
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("brand", "benz"));
        propertyValues.addPropertyValue(new PropertyValue("speed", 120.56));

        BeanDefinition beanDefinition = new BeanDefinition(Car.class, propertyValues);

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("car", beanDefinition);

        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car.toString());
        Assertions.assertEquals(car.getBrand(), "benz");
        Assertions.assertEquals(car.getSpeed(), 120.56);
    }
}
