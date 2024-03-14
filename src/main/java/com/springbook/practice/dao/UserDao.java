package com.springbook.practice.dao;

import com.springbook.practice.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO란 db를 사용해 데이터를 조회하거나 조작하는 기능을 가진 오브젝트
 */
public class UserDao {

    private DataSource dataSource;
    //    private MyCustomJdbcTemplate template;
    private JdbcTemplate template;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        template = new JdbcTemplate(dataSource);
    }

    public void add(User user) throws SQLException {
        String sql = "insert into users(id,name,password) values(?,?,?)";
        template.update(sql, user.getId(), user.getName(), user.getPassword());
    }


    public User get(String id) throws SQLException {
        Connection con = dataSource.getConnection();
        PreparedStatement pstmt = con.prepareStatement("select * from users where id=?");
        pstmt.setString(1, id);

        ResultSet rs = pstmt.executeQuery();
        User user = new User();
        try {
            rs.next();
            user.setId(rs.getString(1));
            user.setName(rs.getString(2));
            user.setPassword(rs.getString(3));
        } catch (SQLException e) {
            throw new EmptyResultDataAccessException(0);
        }
        rs.close();
        pstmt.close();
        con.close();
        return user;
    }

    public void deleteAll() throws SQLException {
        String sql = "delete from users";
        template.update(sql);
    }

    public int getCount() throws SQLException {
        return template.query("select count(*) from users", new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });

    }
}
