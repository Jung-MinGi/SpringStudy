package com.springbook.practice.dao;

import com.springbook.practice.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public void add(User user);

    public User get(String id);

    public void deleteAll();

    public int getCount();


    public List<User> getAll();

    void update(User user);
}
