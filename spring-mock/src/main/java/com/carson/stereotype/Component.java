package com.carson.stereotype;

import java.lang.annotation.*;

/**
 * @author carson_luo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String value() default "";
}
