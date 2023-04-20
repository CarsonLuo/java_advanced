package com.carson.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author carson_luo
 */
public class WorkServiceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("doing something before do explode");
        Object result = invocation.proceed();
        System.out.println("doing something after do explode");
        return result;
    }
}
