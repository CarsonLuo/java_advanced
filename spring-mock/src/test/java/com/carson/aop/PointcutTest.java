package com.carson.aop;

import com.carson.aop.aspectj.AspectJExpressionPointcut;
import com.carson.beans.factory.service.HelloService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author carson_luo
 */
public class PointcutTest {

    @Test
    public void TestPointcut() throws Exception {
        var pointcut = new AspectJExpressionPointcut("execution(* com.carson.beans.factory.service.HelloService.*(..))");
        Class<HelloService> clazz = HelloService.class;
        Method method = clazz.getDeclaredMethod("sayHello");
        Assertions.assertTrue(pointcut.matches(clazz));
        Assertions.assertTrue(pointcut.matches(method, clazz));
    }
}
