package com.hzx;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableRabbit
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication
public class GuliMailOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMailOrderApplication.class,args);
    }
}