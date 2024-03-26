package com.springbook.practice.factoryBean;

import com.springbook.practice.service.TransactionHandler;
import com.springbook.practice.service.UserService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TrProxyFactoryBean implements FactoryBean<Object> {
    public Object target;
    public PlatformTransactionManager transactionManager;
    public String pattern;
    public Class<?> serviceInterface;

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTarget(target);
        transactionHandler.setTransactionManager(transactionManager);
        transactionHandler.setPattern("upgradeLevels");

        return Proxy.newProxyInstance(
                getClass().getClassLoader()
                , new Class[]{UserService.class}
                , transactionHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
