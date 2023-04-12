package com.carson.beans.factory;

import com.carson.beans.factory.bean.Car;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class FactoryBeanTest {

    @Test
    public void TestFactoryBean(){
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-factory-bean.xml");
        Car car = applicationContext.getBean("car", Car.class);
        Assertions.assertEquals(car.getBrand(), "porsche");

        Car car2 = applicationContext.getBean("car", Car.class);
        Assertions.assertTrue(car == car2);
    }
}
