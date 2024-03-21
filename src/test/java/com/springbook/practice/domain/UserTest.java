package com.springbook.practice.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;

    @BeforeEach
    public void init(){
        user = new User();
    }

    @Test
    @DisplayName("upgradeLevel메서드 테스트")
    public void upgradeLevel(){
        Level[] values = Level.values();
        for (Level value : values) {
            if(value.nextLevel()==null)continue;
            user.setLevel(value);
            user.upgradeLevel();
            Assertions.assertThat(user.getLevel()).isEqualTo(value.nextLevel());
        }
    }

}