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
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.springbook.practice.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.springbook.practice.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
class UserServiceTest {
    @Autowired
    private UserService userService;
    //    @Autowired
//    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MailSender mailSender;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;
    List<User> users;

    static class TestUserService extends UserServiceImpl {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (Objects.equals(user.getId(), this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    static class MockUserDao implements UserDao {
        private List<User> users;

        private List<User> updated = new ArrayList<>();

        public List<User> getUpdated() {
            return updated;
        }

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getAll() {
            return users;
        }

        public void update(User user) {
            updated.add(user);
        }

        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }

    @BeforeEach
    void before() {
        users = Arrays.asList(
                new User("test1", "tester1", "1111", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "tester1@naver.com"),
                new User("test2", "tester2", "2222", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 10, "tester2@naver.com"),
                new User("test3", "tester3", "3333", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "tester3@naver.com"),
                new User("test4", "tester4", "4444", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "tester4@naver.com"),
                new User("test5", "tester5", "5555", Level.GOLD, 100, 999, "tester5@naver.com")
        );
    }

    @Test
    @DisplayName("레벨 변경 확인")
    public void upgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();
        List<User> updated = mockUserDao.getUpdated();

        Assertions.assertThat(updated.size()).isEqualTo(2);
        checkUpgradeLevel(updated.get(0), users.get(1).getId(), Level.SILVER);
        checkUpgradeLevel(updated.get(1), users.get(3).getId(), Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        Assertions.assertThat(requests.size()).isEqualTo(2);
        Assertions.assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        Assertions.assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    @Test
    @DisplayName("레벨 초기화 설정 확인")
    public void levelInit() {
        userDao.deleteAll();
        User userWithLevel = users.get(4);
        User userNoLevel = users.get(0);

        userService.add(userWithLevel);
        userService.add(userNoLevel);

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
        testUserService.setMailSender(mailSender);

//        UserServiceTx userServiceTx = new UserServiceTx();
//        userServiceTx.setTransactionManager(transactionManager);
//        userServiceTx.setUserService(testUserService);
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTransactionManager(transactionManager);
        transactionHandler.setTarget(testUserService);
        transactionHandler.setPattern("upgradeLevels");

        UserService userServiceDTx = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader()
                , new Class[]{UserService.class}
                , transactionHandler
        );
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        Assertions.assertThatThrownBy(userServiceDTx::upgradeLevels)
                .isInstanceOf(TestUserServiceException.class);

        checkLevel(users.get(1), false);
    }

    public void checkUpgradeLevel(User user, String expectedId, Level expectedLevel) {
        Assertions.assertThat(user.getId()).isEqualTo(expectedId);
        Assertions.assertThat(user.getLevel()).isEqualTo(expectedLevel);
    }

    public void checkLevel(User user, boolean isUpgraded) {
        User ret = userDao.get(user.getId());
        if (isUpgraded) {
            Assertions.assertThat(ret.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else Assertions.assertThat(ret.getLevel()).isEqualTo(user.getLevel());
    }
}