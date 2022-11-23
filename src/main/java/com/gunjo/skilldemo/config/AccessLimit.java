package com.gunjo.skilldemo.config;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    int second();
    int maxCount();
    boolean needLogin() default true;
}
