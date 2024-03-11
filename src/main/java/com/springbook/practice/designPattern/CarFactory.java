package com.springbook.practice.designPattern;

public class CarFactory extends Factory{
    @Override
    Car getCar(int price,String type) {
        if(price>9000&&!type.equals("국산"))return new Car("BMW");
        else return new Car();
    }
}
