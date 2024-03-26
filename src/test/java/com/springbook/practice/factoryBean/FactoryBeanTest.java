package com.springbook.practice.factoryBean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("팩토리 빈 테스트")
    void factoryBean(){
        Object bean = context.getBean("notCreateByConstructor");
        Assertions.assertThat(bean).isInstanceOf(NotCreateByConstructor.class);
        Assertions.assertThat(((NotCreateByConstructor)bean).getValue()).isEqualTo("FactoryBean!!");
    }
}