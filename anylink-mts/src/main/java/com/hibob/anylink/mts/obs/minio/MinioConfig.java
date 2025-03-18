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

    @Value("${obs.minio.endpoint-internal}")
    private String endpointInternal;

    @Value("${obs.minio.username}")
    private String username;

    @Value("${obs.minio.password}")
    private String password;

    @Value("${obs.minio.bucket-ttl}")
    private String bucketTtl;

    @Value("${obs.minio.bucket-long}")
    private String bucketLong;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(endpointInternal)
                .credentials(username, password)
                .build();
    }

}
