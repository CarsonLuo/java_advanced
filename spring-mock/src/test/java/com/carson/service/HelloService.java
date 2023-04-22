package com.carson.service;

import com.carson.stereotype.Component;

/**
 * @author carson_luo
 */
@Component
public class HelloService {

    public String sayHello() {
        System.out.println("hello, i'm helloService");
        return "hello";
    }
}
