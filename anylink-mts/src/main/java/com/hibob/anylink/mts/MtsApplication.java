package com.hibob.anylink.mts;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@EnableDubbo
@ComponentScan(basePackages = {"com.hibob.anylink.mts", "com.hibob.anylink.common"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) // 禁用secrity，不需要Security的登录，会自己实现，而且不禁用swagger打开要密码
@MapperScan("com.hibob.anylink.mts.mapper")
public class MtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtsApplication.class, args);

    }
}
