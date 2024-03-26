package com.springbook.practice.factoryBean;

import org.springframework.beans.factory.FactoryBean;

public class NotCreateByConstructorFactoryBean implements FactoryBean<NotCreateByConstructor> {
    String value;

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public NotCreateByConstructor getObject() throws Exception {
        return NotCreateByConstructor.newNotCreateByConstructor(value);
    }

    @Override
    public Class<?> getObjectType() {
        return NotCreateByConstructorFactoryBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
