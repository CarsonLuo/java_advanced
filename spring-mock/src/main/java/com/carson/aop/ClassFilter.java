package com.carson.aop;

/**
 * @author carson_luo
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);
}
