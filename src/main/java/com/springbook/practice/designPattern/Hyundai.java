package com.springbook.practice.designPattern;

public class Hyundai extends CarFactory{
    @Override
    Car getCar(int price,String type) {
        if(price>=9000)return new Car("제네시스 G90");
        else return new Car();
    }
}
