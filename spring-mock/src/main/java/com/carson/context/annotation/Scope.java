package com.carson.context.annotation;

import java.lang.annotation.*;

/**
 * @author carson_luo
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default "singleton";
}
