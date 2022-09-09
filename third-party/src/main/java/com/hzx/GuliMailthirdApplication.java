package com.hzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailthirdApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailthirdApplication.class,args);
    }
}