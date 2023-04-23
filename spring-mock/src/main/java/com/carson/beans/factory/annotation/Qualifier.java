package com.carson.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * Qualifier : 修饰语
 *
 * @author carson_luo
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Qualifier {

    String value() default "";
}
