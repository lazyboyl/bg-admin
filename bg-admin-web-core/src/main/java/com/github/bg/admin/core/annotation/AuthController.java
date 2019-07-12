package com.github.bg.admin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linzf
 * @since 2019-06-10
 * 类描述：controller方法的鉴权的注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthController {

    /**
     * 允许响应的权限的集合
     * @return
     */
    String[] authorities() default {};


}
