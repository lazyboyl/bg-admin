package com.github.bg.admin.core;

import com.github.bg.admin.core.util.RedisCache;
import com.github.lazyboyl.chat.core.scan.EnableOnlineChat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author linzf
 * @since 2019-05-06
 * 类描述：主入口类
 */
@SpringBootApplication
@MapperScan({"com.github.bg.admin.core.dao","com.github.lazyboyl.chat.core.dao"})
@EnableOnlineChat
public class BgAdminWebCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BgAdminWebCoreApplication.class, args);
	}

	@Bean
	RedisCache redisCache(){
		return new RedisCache();
	}

}
