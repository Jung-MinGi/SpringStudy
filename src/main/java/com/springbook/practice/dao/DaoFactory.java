package com.springbook.practice.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 오브젝트를 생성방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 클래스를 보통 factory라고 부른다
 */
@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean
    public static ConnectionMaker connectionMaker() {
        return new NaverConnectionMaker();
    }
}
