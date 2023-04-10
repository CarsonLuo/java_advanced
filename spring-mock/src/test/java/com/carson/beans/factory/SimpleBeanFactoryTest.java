package com.carson.beans.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author carson_luo
 */
class SimpleBeanFactoryTest {

    class HelloService {
        public String sayHello() {
            System.out.println("hello, i'm helloService");
            return "hello";
        }
    }

    @Test
    void registerBean() {
        SimpleBeanFactory bf = new SimpleBeanFactory();
        bf.registerBean("helloService", new HelloService());
        HelloService hs = (HelloService) bf.getBean("helloService");
        assertNotNull(hs);
        assertEquals(hs.sayHello(), "hello");
    }

    @Test
    void getBean() {
    }
}