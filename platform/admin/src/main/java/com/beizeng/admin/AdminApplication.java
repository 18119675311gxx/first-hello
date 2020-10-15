package com.beizeng.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: <h1>AdminApplication 启动类</h1>
 **/
@MapperScan("com.beizeng.admin.dao")
@SpringBootApplication()
public class AdminApplication {
    public static void main(String[] args) {
        System.out.println("启动项目");
        SpringApplication.run(AdminApplication.class, args);
    }
}
