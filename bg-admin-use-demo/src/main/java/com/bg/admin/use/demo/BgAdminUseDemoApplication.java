package com.bg.admin.use.demo;

import com.github.bg.admin.core.scan.EnableBgAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableBgAdmin
@MapperScan("com.bg.admin.use.demo.dao")
public class BgAdminUseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BgAdminUseDemoApplication.class, args);
    }

}
