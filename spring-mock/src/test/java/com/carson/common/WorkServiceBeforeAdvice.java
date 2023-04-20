package com.carson.common;

import com.carson.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author carson_luo
 */
public class WorkServiceBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("WorkServiceBeforeAdvice: doing something...");
    }
}
