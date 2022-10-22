package com.hzx;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableRabbit
@EnableFeignClients
@EnableDiscoveryClient
//@EnableTransactionManagement
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailWareApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailWareApplication.class,args);
    }
}