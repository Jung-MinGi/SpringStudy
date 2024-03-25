package com.springbook.practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springbook.practice.proxy.Bitcoin;
import com.springbook.practice.proxy.BitcoinTarget;
import com.springbook.practice.proxy.ChangeKRWToUS;

import java.lang.reflect.Proxy;
import java.net.URISyntaxException;

public class ClientUsingTargetClass {
    public static void main(String[] args) throws URISyntaxException, JsonProcessingException {

        Bitcoin dynamicProxy = (Bitcoin) Proxy.newProxyInstance(
                ClientUsingTargetClass.class.getClassLoader()
                , new Class[]{Bitcoin.class}
                , new ChangeKRWToUS(new BitcoinTarget()));


        int price = dynamicProxy.getPrice();

        System.out.println("비트코인 달러가격 = " + price+"USD");
    }
}
