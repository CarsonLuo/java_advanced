package com.carson.beans.factory;

import com.carson.beans.factory.bean.Person;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class AutowiredAnnotationTest {

    @Test
    public void TestAutowiredAnnotation() {
        var applicationCtx = new ClassPathXmlApplicationContext("classpath:spring-autowired-annotation.xml");
        Person person = applicationCtx.getBean("person", Person.class);
        Assertions.assertNotNull(person);
        Assertions.assertNotNull(person.getCar());
    }
}
