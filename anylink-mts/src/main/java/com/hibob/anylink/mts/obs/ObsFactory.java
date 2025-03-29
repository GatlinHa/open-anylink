package com.hibob.anylink.mts.obs;

import com.hibob.anylink.mts.obs.alioss.AliossService;
import com.hibob.anylink.mts.obs.minio.MinioService;
import com.hibob.anylink.mts.utils.SpringContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ObsFactory {

    private final ObsConfig obsConfig;
    private final MinioService minioService;
    private final AliossService aliossService;

    @Bean
    public ObsService obsService() {
        switch (obsConfig.getSource()) {
            case "minio":
                return minioService;
            case "alioss":
                return aliossService;
            default:
                log.error("The type of obs is not specified");
                return null;
        }
    }

    public static ObsService ObsService(String source) {
        switch (source) {
            case "minio":
                return SpringContextUtil.getBean(MinioService.class);
            case "alioss":
                return SpringContextUtil.getBean(AliossService.class);
            default:
                log.error("The type of obs is not specified");
                return null;
        }
    }
}
