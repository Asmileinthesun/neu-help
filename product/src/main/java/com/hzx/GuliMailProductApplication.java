package com.hzx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
//@EnableCaching
@EnableFeignClients(basePackages = "com.hzx.product.feign")
@EnableDiscoveryClient
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailProductApplication.class,args);
    }
}