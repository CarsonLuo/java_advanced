package com.carson.beans.factory.bean;

import com.carson.beans.factory.annotation.Value;
import com.carson.stereotype.Component;

/**
 * @author carson_luo
 */
@Component
public class Car {

    /**
     * 品牌
     */
    @Value("${brand}")
    private String brand;

    /**
     * 速度
     */
    private Double speed;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", speed=" + speed +
                '}';
    }
}
