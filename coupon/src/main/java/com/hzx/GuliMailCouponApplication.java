package com.hzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailCouponApplication.class,args);
    }
}