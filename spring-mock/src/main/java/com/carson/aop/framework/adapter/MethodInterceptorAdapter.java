package com.carson.aop.framework.adapter;

import com.carson.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author carson_luo
 */
public class MethodInterceptorAdapter implements MethodInterceptor {

    private MethodBeforeAdvice advice;

    public MethodInterceptorAdapter(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 在被代理方法之前, 先执行before advice操作
        this.advice.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return invocation.proceed();
    }
}
