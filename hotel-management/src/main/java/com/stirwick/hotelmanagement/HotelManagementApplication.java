package com.stirwick.hotelmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
/**
 * 酒店管理系统启动类
 * SpringBoot2.7.18 + SpringMVC + MyBatis‑Plus
 */
@SpringBootApplication
@MapperScan("com.stirwick.hotelmanagement.mapper")
public class HotelManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApplication.class, args);
    }

}
