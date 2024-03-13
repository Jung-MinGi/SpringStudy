package com.springbook.practice.domain;

import lombok.Data;

@Data
public class User {
    public String id;
    public String name;
    public String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User() {
    }
}
