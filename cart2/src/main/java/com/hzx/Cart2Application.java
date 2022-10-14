package com.hzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
////@EnableCaching
//@EnableFeignClients(basePackages = "com.hzx")
@EnableDiscoveryClient
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Cart2Application {
    public static void main(String[] args) {
        SpringApplication.run(Cart2Application.class,args);
    }
}