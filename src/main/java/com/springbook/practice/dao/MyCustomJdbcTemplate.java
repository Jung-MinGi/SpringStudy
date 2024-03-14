package com.springbook.practice.dao;

import com.springbook.practice.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyCustomJdbcTemplate {
    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void myJdbcTemplate(StatementStrategy stmt) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = stmt.makePreparedStatement(con);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void excuteSql(final String sql) throws SQLException {
        myJdbcTemplate(new StatementStrategy() {

            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement(sql);
            }
        });
    }

    public void excute(final String sql, User user) throws SQLException {
        myJdbcTemplate(new StatementStrategy() {

            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql);
               pstmt.setString(1,user.getId());
               pstmt.setString(2,user.getName());
               pstmt.setString(3,user.getPassword());
                return pstmt;
            }
        });
    }
}
