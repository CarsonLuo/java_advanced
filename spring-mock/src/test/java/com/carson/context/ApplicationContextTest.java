package com.carson.context;

import com.carson.beans.factory.bean.Car;
import com.carson.beans.factory.bean.Person;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class ApplicationContextTest {

    @Test
    public void TestApplicationContext() {
        // 自动识别了 BeanFactoryPostProcessor, BeanPostProcessor
        // 不用像面向 BeanFactory时, 手动注册 BeanFactoryPostProcessor, BeanPostProcessor
        var classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring-applicationContext.xml");

        Person person = classPathXmlApplicationContext.getBean("person", Person.class);
        Car car = classPathXmlApplicationContext.getBean("car", Car.class);

        Assertions.assertEquals(person.getName(), "carson");

        // CustomBeanFactoryPostProcessor 修改 18 -> 20
        Assertions.assertEquals(person.getAge(), 20);

        Assertions.assertEquals(car.getBrand(), "benz");

        // CustomBeanPostProcessor 修改 120.88 -> 999.99
        Assertions.assertEquals(car.getSpeed(), 999.99);
    }
}
