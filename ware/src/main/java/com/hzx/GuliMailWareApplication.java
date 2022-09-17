package com.hzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableTransactionManagement
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailWareApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailWareApplication.class,args);
    }
}