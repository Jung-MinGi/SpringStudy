package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaumConnectionMaker implements ConnectionMaker{
    private static final String url = "jdbc:mysql://localhost:3306/toby";
    private static final String name = "root";
    private static final String password = "root";
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, name, password);
    }
}
