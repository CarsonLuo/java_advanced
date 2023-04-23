package com.carson.service;

/**
 * @author carson_luo
 */
public class WorkServiceImpl implements WorkService {

    private String name;

    @Override
    public void explode() {
        System.out.println("doing explode");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
