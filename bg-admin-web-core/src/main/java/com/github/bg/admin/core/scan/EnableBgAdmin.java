package com.github.bg.admin.core.scan;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author linzef
 * @since 2020-03-16
 * 注解描述： 实现开启可视化报表的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({BgAdminScannerRegister.class})
public @interface EnableBgAdmin {

    String [] basePackages() default {"com.github.bg.admin.core"};

}
