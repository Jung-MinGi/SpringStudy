package com.springbook.practice.designPattern;

public class Client {
    public static void main(String[] args) {
        Factory factory = new CarFactory();
        Car car = factory.getCar(9999,"국산");
        System.out.println("car.getName() = " + car.getName());
    }
}
