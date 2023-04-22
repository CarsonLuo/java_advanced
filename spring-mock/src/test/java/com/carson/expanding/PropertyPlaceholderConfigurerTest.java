package com.carson.expanding;

import com.carson.beans.factory.bean.Car;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class PropertyPlaceholderConfigurerTest {

    @Test
    public void TestPropertyPlaceholderConfigurer() {
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-property-placeholder-configurer.xml");
        Car car = applicationContext.getBean("car", Car.class);
        Assertions.assertEquals("benz", car.getBrand());
    }
}
