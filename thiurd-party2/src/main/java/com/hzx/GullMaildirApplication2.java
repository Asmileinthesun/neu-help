package com.hzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
//@MapperScan("com.hzx.product.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GullMaildirApplication2 {
    public static void main(String[] args) {
        SpringApplication.run(GullMaildirApplication2.class,args);
    }
}