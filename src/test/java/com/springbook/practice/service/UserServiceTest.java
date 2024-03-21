package com.springbook.practice.service;

import com.springbook.practice.dao.UserDao;
import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.springbook.practice.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.springbook.practice.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
class UserServiceTest {
    @Autowired
    private UserService service;
    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;
    List<User> users;

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @BeforeEach
    void before() {
        users = Arrays.asList(
                new User("test1", "tester1", "1111", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("test2", "tester2", "2222", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 10),
                new User("test3", "tester3", "3333", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("test4", "tester4", "4444", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("test5", "tester5", "5555", Level.GOLD, 100, 999)
        );
    }

    @Test
    @DisplayName("레벨 변경 확인")
    public void test() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        service.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    @Test
    @DisplayName("레벨 초기화 설정 확인")
    public void levelInit() {
        userDao.deleteAll();
        User userWithLevel = users.get(4);
        User userNoLevel = users.get(0);

        service.add(userWithLevel);
        service.add(userNoLevel);

        User userWithLevelFromDb = userDao.get(userWithLevel.getId());
        User userNoLevelFromDb = userDao.get(userNoLevel.getId());

        Assertions.assertThat(userWithLevel.getLevel()).isEqualTo(userWithLevelFromDb.getLevel());
        Assertions.assertThat(userNoLevel.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    @DisplayName("레벨 업그레이드 롤백 확인")
    public void upgradeAllOrNothing() {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        Assertions.assertThatThrownBy(testUserService::upgradeLevels)
                .isInstanceOf(TestUserServiceException.class);

        checkLevel(users.get(1), false);
    }

    public void checkLevel(User user, boolean isUpgraded) {
        User ret = userDao.get(user.getId());
        if (isUpgraded) {
            Assertions.assertThat(ret.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else Assertions.assertThat(ret.getLevel()).isEqualTo(user.getLevel());
    }
}