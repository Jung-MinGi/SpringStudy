package com.springbook.practice;

import com.springbook.practice.dao.DaoFactory;
import com.springbook.practice.dao.NaverConnectionMaker;
import com.springbook.practice.dao.UserDao;
import com.springbook.practice.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteShip");
        user.setName("백기선");
        user.setPassword("married");

//        userDao.add(user);
        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDao.get(user.getId());
        System.out.println("user2.getName() = " + user2.getName());
    }
}
