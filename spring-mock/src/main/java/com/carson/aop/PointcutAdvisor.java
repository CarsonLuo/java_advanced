package com.carson.aop;

/**
 * @author carson_luo
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
