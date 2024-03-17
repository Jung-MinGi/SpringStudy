package com.springbook.practice;

import com.springbook.practice.dao.UserDao;
import com.springbook.practice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
//@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;

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
    public void addAndGet() {

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
    public void jdbcStrategy() {
        userDao.deleteAll();
        userDao.add(user1);
        userDao.add(user2);
        Assertions.assertThat(userDao.getCount()).isEqualTo(2);
        userDao.deleteAll();
        Assertions.assertThat(userDao.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("getAll()")
    public void getAll() {
        userDao.deleteAll();
        List<User> list = userDao.getAll();
        Assertions.assertThat(list.size()).isZero();

        userDao.add(user1);
        list = userDao.getAll();
        Assertions.assertThat(list.size()).isOne();
        checkProperty(user1, list.get(0));

        userDao.add(user2);
        list = userDao.getAll();
        Assertions.assertThat(list.size()).isEqualTo(2);
        checkProperty(user2, list.get(1));

        userDao.add(user3);
        list = userDao.getAll();
        Assertions.assertThat(list.size()).isEqualTo(3);
        checkProperty(user3, list.get(2));

    }

    @Test
    @DisplayName("중복키 예외 테스트")
    public void duplicateKey() {
        userDao.deleteAll();
        userDao.add(user1);
        Assertions.assertThatThrownBy(() -> userDao.add(user1))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("SQLException 직접 예외 전환")
    public void sqlExToDataEx() {
        userDao.deleteAll();
        userDao.add(user1);
        try {
            userDao.add(user1);
        }catch (DuplicateKeyException e){
            SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            SQLException sqlException = (SQLException) e.getRootCause();
            DataAccessException dataAccessException = translator.translate(null,null,sqlException);
            Assertions.assertThat(dataAccessException).isInstanceOf(DuplicateKeyException.class);
        }
    }

    private void checkProperty(User user, User comp) {
        assertThat(user.getId()).isEqualTo(comp.getId());
        assertThat(user.getName()).isEqualTo(comp.getName());
        assertThat(user.getPassword()).isEqualTo(comp.getPassword());
    }
}