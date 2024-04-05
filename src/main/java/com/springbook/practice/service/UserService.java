package com.springbook.practice.service;

import com.springbook.practice.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    void add(User user);

    @Transactional(readOnly = true)
    User get(String id);

    void deleteAll();

    @Transactional(readOnly = true)
    List<User> getAll();

    void update(User user);

    void upgradeLevels();
}
