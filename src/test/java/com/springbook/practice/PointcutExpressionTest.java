package com.springbook.practice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
public class PointcutExpressionTest {
    @Test
    public void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int com.springbook.practice.Target.hello(int))");
        System.out.println("pointcut = " + pointcut.getExpression());
        System.out.println("pointcut.getClassFilter() = " + pointcut.getClassFilter());
        System.out.println("pointcut.getMethodMatcher() = " + pointcut.getMethodMatcher());
        System.out.println(Target.class.getMethod("hello", int.class));
        Assertions.assertThat(pointcut.getClassFilter().matches(Target.class)).isTrue();
        Assertions.assertThat(pointcut.getMethodMatcher().matches(Target.class.getMethod("hello", int.class), null)).isTrue();

        Assertions.assertThat(pointcut.getMethodMatcher().matches(Target.class.getMethod("bye", int.class), null)).isFalse();
    }
}