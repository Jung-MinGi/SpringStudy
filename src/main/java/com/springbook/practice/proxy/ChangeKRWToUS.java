package com.springbook.practice.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ChangeKRWToUS implements InvocationHandler {
    Bitcoin target;

    public ChangeKRWToUS(Bitcoin target) {
        this.target = target;
    }

    public ChangeKRWToUS() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int price = (int) method.invoke(target);
        System.out.println("비트코인 원화가격 = " + price+"KRW");
        return price/1300;
    }
}
