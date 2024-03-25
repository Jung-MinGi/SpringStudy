package com.springbook.practice.domain;

import lombok.Data;

@Data
public class User {
    public String id;
    public String name;
    public String password;
    public Level level;
    public int login;
    public int recommend;
    public String email;

    public User(String id, String name, String password, Level level, int login, int recommend, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }

    public User() {
    }
    public void upgradeLevel(){
        Level nextLevel = this.level.nextLevel();
        if(nextLevel==null){
            throw new IllegalStateException(this.level+"은 업그레이드 불가합니다.");
        }else this.level=nextLevel;
    }
}
