package com.hibob.anylink.mts.obs.alioss;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.exceptions.ClientException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;

@Component
@Data
public class AliossConfig {

    @Value("${obs.alioss.endpoint}")
    private String endpoint;

    @Value("${obs.alioss.endpoint-internal}")
    private String endpointInternal;

    @Value("${obs.alioss.region}")
    private String region;

    @Value("${obs.alioss.bucket}")
    private String bucket;

    @Value("${obs.alioss.ak}")
    private String ak;

    @Value("${obs.alioss.sk}")
    private String sk;

    @Bean
    public OSS aliossClient() throws ClientException {
        System.setProperty("OSS_ACCESS_KEY_ID", ak);
        System.setProperty("OSS_ACCESS_KEY_SECRET", sk);
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        return OSSClientBuilder.create()
                .endpoint(endpointInternal)
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();
    }
}
