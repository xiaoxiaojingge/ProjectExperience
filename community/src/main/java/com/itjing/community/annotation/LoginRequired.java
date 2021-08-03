package com.itjing.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: lijing
 * @Date: 2021年07月31日 13:50
 * @Description: 自定义注解，用于拦截一些用户登录后才能访问的内容
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
