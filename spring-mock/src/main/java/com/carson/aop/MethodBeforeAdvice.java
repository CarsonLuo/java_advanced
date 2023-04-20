package com.carson.aop;

import java.lang.reflect.Method;

/**
 * @author carson_luo
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method, Object[] args, Object target) throws Throwable;
}
