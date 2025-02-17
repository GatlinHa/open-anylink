package com.hibob.anylink.mts.obs;

import com.hibob.anylink.mts.obs.alioss.AliossService;
import com.hibob.anylink.mts.obs.minio.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ObsFactory {

    private final MinioService minioService;
    private final AliossService aliossService;

    @Value("${obs.type}")
    private String type;

    @Bean
    public ObsService obsService() {
        switch (type) {
            case "minio":
                return minioService;
            case "alioss":
                return aliossService;
            default:
                log.error("The type of obs is not specified");
                return null;
        }
    }
}
