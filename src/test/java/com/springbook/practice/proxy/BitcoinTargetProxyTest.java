package com.springbook.practice.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.net.URISyntaxException;

class BitcoinTargetProxyTest {
    @Test
    void proxyFactoryBean() throws URISyntaxException, JsonProcessingException {

        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new BitcoinTarget());
        proxyFactoryBean.addAdvice(new ChangeKRWToUSAdvice());
        Bitcoin object = (Bitcoin) proxyFactoryBean.getObject();
        int price = object.getPrice();
        System.out.println("price = " + price);
    }

    static class ChangeKRWToUSAdvice implements MethodInterceptor {


        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            int ret = (int) invocation.proceed();
            return ret / 1300;
        }
    }
}
