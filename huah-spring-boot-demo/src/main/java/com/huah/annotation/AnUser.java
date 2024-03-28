package com.huah.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.CONSTRUCTOR)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AnUser {
    String name() default "huah";
    int age();
}
