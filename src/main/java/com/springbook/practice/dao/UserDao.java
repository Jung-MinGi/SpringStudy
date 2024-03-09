package com.springbook.practice.dao;

import com.springbook.practice.domain.User;

import java.sql.*;
import java.util.Arrays;

/**
 * DAO란 db를 사용해 데이터를 조회하거나 조작하는 기능을 가진 오브젝트
 */
public class UserDao {
    private static final String url = "jdbc:mysql://localhost:3306/toby";
    private static final String name = "root";
    private static final String password = "root";

    public void add(User user) throws SQLException {
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement("insert into users(id,name,password) values(?,?,?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());

        pstmt.executeUpdate();
        pstmt.close();
        con.close();
    }


    public User get(String id) throws SQLException {
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement("select * from users where id=?");
        pstmt.setString(1, id);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString(1));
        user.setName(rs.getString(2));
        user.setPassword(rs.getString(3));
        rs.close();
        pstmt.close();
        con.close();
        return user;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, name, password);
    }

    public static void main(String[] args) throws SQLException {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setId("whiteShip");
        user.setName("백기선");
        user.setPassword("married");

        userDao.add(user);
        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDao.get(user.getId());
        System.out.println("user2.getName() = " + user2.getName());
    }
}
