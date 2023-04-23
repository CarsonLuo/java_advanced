package com.carson.beans.factory.bean;

import com.carson.beans.factory.DisposableBean;
import com.carson.beans.factory.InitializingBean;
import com.carson.beans.factory.annotation.Autowired;
import com.carson.beans.factory.annotation.Qualifier;
import com.carson.stereotype.Component;

/**
 * @author carson_luo
 */
@Component
public class Person implements InitializingBean, DisposableBean {

    private String name;

    private Integer age;

    @Autowired
    @Qualifier(value = "car")
    private Car car;

    public void customInitMethod() {
        this.age = 999;
    }

    public void customDestroyMethod() {
        this.age = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Person -> InitializingBean#afterPropertiesSet");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Person -> DisposableBean#destroy");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", car=" + car +
                '}';
    }
}
