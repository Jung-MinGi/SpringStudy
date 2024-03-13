package com.springbook.practice.junitTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.HashSet;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
public class ApplicationContextTest {
    static HashSet<ApplicationContext> set = new HashSet<>();
    @Autowired
    static ApplicationContext context = null;

    @Test
    @DisplayName("동일한 컨테이너인지 확인1")
    public void test1() {
        Assertions.assertThat(set.add(context)).isTrue();
    }

    @Test
    @DisplayName("동일한 컨테이너인지 확인2")
    public void test2() {
        Assertions.assertThat(set.add(context)).isFalse();
    }@Test
    @DisplayName("동일한 컨테이너인지 확인3")
    public void test3() {
        Assertions.assertThat(set.add(context)).isFalse();
    }
}
