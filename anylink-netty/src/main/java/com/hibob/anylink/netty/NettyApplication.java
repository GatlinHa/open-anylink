package com.hibob.anylink.netty;

import com.hibob.anylink.netty.server.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@EnableDiscoveryClient
@EnableDubbo
@ComponentScan(basePackages = {"com.hibob.anylink.netty", "com.hibob.anylink.common"})
@SpringBootApplication
public class NettyApplication implements CommandLineRunner {

    @Autowired
    WebSocketServer webSocketServer;

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 放在线程中启动，不阻塞主线程
        new Thread(() -> {
            webSocketServer.start();
        }).start();
    }
}

