package com.springbook.practice.designPattern;

public class Tesla extends CarFactory{
    @Override
    Car getCar(int price,String type) {
       if(price>5000)return new Car("teslaModelY");
       else return new Car();
    }
}
