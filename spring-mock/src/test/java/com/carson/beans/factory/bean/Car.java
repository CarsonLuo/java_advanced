package com.carson.beans.factory.bean;

/**
 * @author carson_luo
 */
public class Car {

    /**
     * 品牌
     */
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
