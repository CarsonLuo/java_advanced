package com.carson.beans.factory;

import com.carson.beans.factory.bean.Car;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class ValueAnnotationTest {

    @Test
    public void TestValueAnnotation() {
        var applicationCtx = new ClassPathXmlApplicationContext("classpath:spring-value-annotation.xml");
        Car car = applicationCtx.getBean("car", Car.class);
        Assertions.assertNotNull(car);
        Assertions.assertEquals("benz", car.getBrand());
    }
}
