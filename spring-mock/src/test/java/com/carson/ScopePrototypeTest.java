package com.carson;

import com.carson.beans.factory.bean.Car;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class ScopePrototypeTest {

    @Test
    public void TestScopePrototype(){
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-prototype.xml");
        Car car01 = applicationContext.getBean("car", Car.class);
        Car car02 = applicationContext.getBean("car", Car.class);
        Assertions.assertFalse(car01 == car02);
    }
}
