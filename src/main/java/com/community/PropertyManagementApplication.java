package com.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 小区物业管理系统启动类
 * 
 * @author system
 */
@SpringBootApplication
@MapperScan("com.community.mapper")
public class PropertyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyManagementApplication.class, args);
        System.out.println("小区物业管理系统启动成功！");
        System.out.println("访问地址：http://localhost:8080/api");
    }

}