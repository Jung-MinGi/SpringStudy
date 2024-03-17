package com.springbook.practice.dao;

import com.springbook.practice.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO란 db를 사용해 데이터를 조회하거나 조작하는 기능을 가진 오브젝트
 */
public class UserDaoJdbc implements UserDao{
    private JdbcTemplate template;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };
    private RowMapper<Integer> integerRowMapper = new RowMapper<Integer>() {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt(1);
        }
    };

    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public void add(User user){
        String sql = "insert into users(id,name,password) values(?,?,?)";
        template.update(sql, user.getId(), user.getName(), user.getPassword());
    }


    public User get(String id) {
        return template.queryForObject("select * from users where id=?", new Object[]{id}, userMapper);
    }

    public void deleteAll() {
        String sql = "delete from users";
        template.update(sql);
    }

    public int getCount() {
        return template.queryForObject("select count(*) from users", integerRowMapper);

    }

    public List<User> getAll() {
        return template.query("select * from users order by id", userMapper);

    }
}
