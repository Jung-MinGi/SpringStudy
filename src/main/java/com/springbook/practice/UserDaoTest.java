package com.springbook.practice;

import com.springbook.practice.dao.UserDao;
import com.springbook.practice.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException {
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteShip");
        user.setName("백기선");
        user.setPassword("married");

//        userDao.add(user);
        User user2 = userDao.get(user.getId());
        if(!user.getName().equals(user2.getName())){
            System.out.println("테스트 실패");
        }
        System.out.println("user2.getName() = " + user2.getName());
    }

    public void addAndGet(){

    }
}
