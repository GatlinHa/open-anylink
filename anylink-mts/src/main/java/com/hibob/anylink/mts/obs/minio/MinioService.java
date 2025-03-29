package com.hibob.anylink.mts.obs.minio;

import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.obs.ObsService;
import com.hibob.anylink.mts.obs.ObsUploadRet;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService implements ObsService {
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @Override
    public ObsUploadRet uploadFile(MultipartFile file, String randomFileName, int storeType) {
        log.info("MinioService::uploadFile file");
        String bucket = 0 == storeType ? minioConfig.getBucketLong() : minioConfig.getBucketTtl();
        try {
            String prefixPath = FileType.determineFileType(file.getContentType()).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + randomFileName;

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fullName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            String signUrl = getSignUrl(bucket, fullName);
            return new ObsUploadRet(bucket, fullName, signUrl);
        }
        catch (Exception e) {
            log.error("MinioService upload file error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public ObsUploadRet uploadFile(byte[] fileByte, String contentType, String randomFileName, int storeType) {
        log.info("MinioService::uploadFile fileByte");
        String bucket = 0 == storeType ? minioConfig.getBucketLong() : minioConfig.getBucketTtl();
        try {
            String prefixPath = FileType.determineFileType(contentType).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + randomFileName;

            InputStream stream = new ByteArrayInputStream(fileByte);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fullName)
                    .stream(stream, fileByte.length, -1)
                    .contentType(contentType)
                    .build());
            String signUrl = getSignUrl(bucket, fullName);
            return new ObsUploadRet(bucket, fullName, signUrl);
        }
        catch (Exception e) {
            log.error("MinioService upload file error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getSignUrl(String bucketName, String ObjectName) {
        log.info("MinioService::getSignUrl");
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs
                            .builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(ObjectName)
                            .expiry((int) minioConfig.getUrlExpire(), TimeUnit.SECONDS)
                            .build());
            return url;
        } catch (Exception e) {
            log.error("MinioService getSignUrl error: {}", e.getMessage());
            return "";
        }
    }
}
