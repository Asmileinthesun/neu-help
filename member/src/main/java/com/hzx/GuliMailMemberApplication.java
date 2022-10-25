package com.hzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hzx.member.feign")
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailMemberApplication.class,args);
    }
}