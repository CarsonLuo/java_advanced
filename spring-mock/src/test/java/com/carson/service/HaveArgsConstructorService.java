package com.carson.service;

/**
 * @author carson_luo
 */
public class HaveArgsConstructorService {

    private String msg;

    public HaveArgsConstructorService(String msg) {
        this.msg = msg;
    }

    public String sayHello() {
        System.out.println("HaveArgsConstructorService say : " + msg);
        return msg;
    }
}
