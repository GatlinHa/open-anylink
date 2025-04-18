package com.hibob.anylink.mts.obs.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.obs.ObsService;
import com.hibob.anylink.mts.obs.ObsUploadRet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AliossService implements ObsService {
    private final AliossConfig aliossConfig;
    private final OSS aliossClient;

    @Override
    public String getDownloadUrl(String bucketName, String ObjectName) {
        log.info("AliossService::getDownloadUrl");
        if (aliossConfig.isPreSign()) {
            try {
                Date expiration = new Date(new Date().getTime() + aliossConfig.getDownloadUrlExpire() * 1000);
                URL url = aliossClient.generatePresignedUrl(bucketName, ObjectName, expiration);
                return url.toString();
            } catch (Exception e) {
                log.error("AliossService getDownloadUrl error: {}", e.getMessage());
                return "";
            }
        } else  {
            log.error("AliossService getDownloadUrl does not support non presign mode");
            return "";
        }
    }

    @Override
    public ObsUploadRet getUploadUrl(String contentType, String randomFileName, int storeType) {
        log.info("AliossService::getUploadUrl  method 1");

        String bucketName = 0 == storeType ? aliossConfig.getBucketLong() : aliossConfig.getBucketTtl();
        String prefixPath = FileType.determineFileType(contentType).name();
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuidPath = UUID.randomUUID().toString();
        String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + randomFileName;

        if (aliossConfig.isPreSign()) {
            try {
                Date expiration = new Date(new Date().getTime() + aliossConfig.getUploadUrlExpire() * 1000);
                GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fullName);
                request.setMethod(com.aliyun.oss.HttpMethod.PUT); // 设置HTTP方法为PUT
                request.setExpiration(expiration); // 设置过期时间
                // 指定Content-Type请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", StringUtils.hasLength(contentType) ? contentType : "application/octet-stream");
                request.setHeaders(headers);
                URL url = aliossClient.generatePresignedUrl(request);
                return new ObsUploadRet(bucketName, fullName, url.toString());
            } catch (Exception e) {
                log.error("AliossService getDownloadUrl error: {}", e.getMessage());
                return null;
            }
        } else  {
            log.error("AliossService getUploadUrl does not support non presign mode");
            return null;
        }
    }

    @Override
    public ObsUploadRet getUploadUrl(String contentType, String bucketName, String ObjectName) {
        log.info("AliossService::getUploadUrl  method 2");
        if (aliossConfig.isPreSign()) {
            try {
                Date expiration = new Date(new Date().getTime() + aliossConfig.getUploadUrlExpire() * 1000);
                GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, ObjectName);
                request.setMethod(com.aliyun.oss.HttpMethod.PUT); // 设置HTTP方法为PUT
                request.setExpiration(expiration); // 设置过期时间
                // 指定Content-Type请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", StringUtils.hasLength(contentType) ? contentType : "application/octet-stream");
                request.setHeaders(headers);
                URL url = aliossClient.generatePresignedUrl(request);
                return new ObsUploadRet(bucketName, ObjectName, url.toString());
            } catch (Exception e) {
                log.error("AliossService getDownloadUrl error: {}", e.getMessage());
                return null;
            }
        } else  {
            log.error("AliossService getUploadUrl does not support non presign mode");
            return null;
        }
    }
}
