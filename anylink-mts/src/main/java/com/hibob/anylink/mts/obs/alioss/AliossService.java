package com.hibob.anylink.mts.obs.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.obs.ObsService;
import com.hibob.anylink.mts.obs.ObsUploadRet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AliossService implements ObsService {
    private final AliossConfig aliossConfig;
    private final OSS uploadClient;
    private final OSS signClient;

    @Override
    public ObsUploadRet uploadFile(MultipartFile file, String randomFileName, int storeType) {
        log.info("AliossService::uploadFile file");
        String bucket = 0 == storeType ? aliossConfig.getBucketLong() : aliossConfig.getBucketTtl();
        try {
            String prefixPath = FileType.determineFileType(file.getContentType()).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + randomFileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fullName, file.getInputStream());
            uploadClient.putObject(putObjectRequest);
            String signUrl = getSignUrl(bucket, fullName);
            return new ObsUploadRet(bucket, fullName, signUrl);
        } catch (Exception e) {
            log.error("AliossService upload file error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public ObsUploadRet uploadFile(byte[] fileByte, String contentType, String randomFileName, int storeType) {
        log.info("AliossService::uploadFile fileByte");
        String bucket = 0 == storeType ? aliossConfig.getBucketLong() : aliossConfig.getBucketTtl();
        try {
            String prefixPath = FileType.determineFileType(contentType).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + randomFileName;

            InputStream stream = new ByteArrayInputStream(fileByte);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fullName, stream, metadata);
            uploadClient.putObject(putObjectRequest);
            String signUrl = getSignUrl(bucket, fullName);
            return new ObsUploadRet(bucket, fullName, signUrl);
        } catch (Exception e) {
            log.error("AliossService upload file error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getSignUrl(String bucketName, String ObjectName) {
        log.info("AliossService::getSignUrl");
        if (aliossConfig.isPreSign()) {
            try {
                Date expiration = new Date(new Date().getTime() + aliossConfig.getUrlExpire() * 1000);
                URL url = signClient.generatePresignedUrl(bucketName, ObjectName, expiration);
                return url.toString();
            } catch (Exception e) {
                log.error("AliossService getSignUrl error: {}", e.getMessage());
                return "";
            }
        } else  {
            return "https://" + bucketName + "." + aliossConfig.getEndpoint().substring(8)  + "/" + ObjectName;
        }
    }

}
