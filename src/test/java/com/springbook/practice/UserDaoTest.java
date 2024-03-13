package com.springbook.practice;

import com.springbook.practice.dao.StatementStrategy;
import com.springbook.practice.dao.UserDao;
import com.springbook.practice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
//@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void before() {
        user1 = new User("test1", "tester1", "1111");
        user2 = new User("test2", "tester2", "2222");
        user3 = new User("test3", "tester3", "3333");
    }

    @Test
    public void addAndGet() throws SQLException {

        userDao.deleteAll();
        userDao.add(user1);
        userDao.add(user2);
        User result1 = userDao.get(user1.getId());
        User result2 = userDao.get(user2.getId());
        checkProperty(user1, result1);
        checkProperty(user2, result2);
    }

    @Test
    @DisplayName("get메서드 예외 테스트")
    public void getOccurEx() {
        assertThatThrownBy(() -> userDao.get("1"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Jdbc 전략패턴 분리 삭제 메서드 테스트")
    public void jdbcStrategy() throws SQLException {
        userDao.deleteAll();
        userDao.add(user1);
        userDao.add(user2);
        Assertions.assertThat(userDao.getCount()).isEqualTo(2);
        userDao.jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                String sql = "delete from users";
                return con.prepareStatement(sql);
            }
        });
        Assertions.assertThat(userDao.getCount()).isEqualTo(0);
    }

    private void checkProperty(User user, User comp) {
        assertThat(user.getId()).isEqualTo(comp.getId());
        assertThat(user.getName()).isEqualTo(comp.getName());
        assertThat(user.getPassword()).isEqualTo(comp.getPassword());
    }
}