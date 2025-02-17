package com.hibob.anylink.mts.obs.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Data
public class MinioConfig {

    @Value("${obs.minio.endpoint}")
    private String endpoint;

    @Value("${obs.minio.username}")
    private String username;

    @Value("${obs.minio.password}")
    private String password;

    @Value("${obs.minio.bucket}")
    private String bucket;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();
    }

}
