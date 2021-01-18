package com.maltsev.jwt;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
    boolean required() default true;

    String[] roles() default {};
}

