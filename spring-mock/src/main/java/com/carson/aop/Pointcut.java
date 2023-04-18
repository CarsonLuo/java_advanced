package com.carson.aop;

/**
 * 切面抽象
 *
 * @author carson_luo
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
