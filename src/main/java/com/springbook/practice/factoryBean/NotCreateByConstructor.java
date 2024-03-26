package com.springbook.practice.factoryBean;

public class NotCreateByConstructor {
    private String value;

    private NotCreateByConstructor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static NotCreateByConstructor newNotCreateByConstructor(String value){
        return new NotCreateByConstructor(value);
    }
}
