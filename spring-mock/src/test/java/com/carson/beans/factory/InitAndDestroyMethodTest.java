package com.carson.beans.factory;

import com.carson.beans.factory.bean.Person;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class InitAndDestroyMethodTest {

    @Test
    public void TestInitAndDestroyMethod() {
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-init-destroy.xml");
        Person person = applicationContext.getBean("person", Person.class);
        System.out.println("main -> person : " + person.toString());

        // applicationContext.close();
        applicationContext.registerShutdownHook();
    }
}
