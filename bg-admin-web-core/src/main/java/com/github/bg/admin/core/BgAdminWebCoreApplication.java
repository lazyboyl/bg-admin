package com.github.bg.admin.core;

import com.didispace.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author linzf
 * @since 2019-05-06
 * 类描述：主入口类
 */
@SpringBootApplication
@EnableSwagger2Doc
@MapperScan("com.github.bg.admin.core.dao")
public class BgAdminWebCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BgAdminWebCoreApplication.class, args);
	}

}
