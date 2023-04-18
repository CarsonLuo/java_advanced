package com.carson.aop;

import java.lang.reflect.Method;

/**
 * matcher: 匹配程序, 匹配器
 *
 * @author carson_luo
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> clazz);
}
