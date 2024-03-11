package com.springbook.practice.designPattern;

public abstract class Factory {
    public  Car sameContext(int price,String type){
        return getCar(price,type);
    }
     abstract Car getCar(int price,String type);


}
