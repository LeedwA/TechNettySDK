package com.example.nettylib.netty.base;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*************************************
 功能：
 *************************************/


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TechFled {
    String value();
}
