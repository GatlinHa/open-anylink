package com.hibob.anylink.mts.obs.minio;

import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.obs.ObsService;
import com.hibob.anylink.mts.obs.ObsUploadRet;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public String getDownloadUrl(String bucketName, String ObjectName) {
        log.info("MinioService::getDownloadUrl");
        if (minioConfig.isPreSign()) {
            try {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs
                                .builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(ObjectName)
                                .expiry((int) minioConfig.getDownloadUrlExpire(), TimeUnit.SECONDS)
                                .build());
            } catch (Exception e) {
                log.error("MinioService getDownloadUrl error: {}", e.getMessage());
                return "";
            }
        } else  {
            // 直接下载文件，需要bucket具有公共读权限，目前只针对本地Docker开发环境
            return  minioConfig.getEndpoint() + "/" + bucketName + "/" + ObjectName;
        }
    }

    @Override
    public ObsUploadRet getUploadUrl(String contentType, String randomFileName, int storeType) {
        log.info("MinioService::getUploadUrl method 1");

        String bucketName = 0 == storeType ? minioConfig.getBucketLong() : minioConfig.getBucketTtl();
        String prefixPath = FileType.determineFileType(contentType).name();
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuidPath = UUID.randomUUID().toString();
        String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + randomFileName;

        if (minioConfig.isPreSign()) {
            try {
                String url = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs
                                .builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(fullName)
                                .expiry((int) minioConfig.getUploadUrlExpire(), TimeUnit.SECONDS)
                                .build());
                return new ObsUploadRet(bucketName, fullName, url);
            } catch (Exception e) {
                log.error("MinioService getDownloadUrl error: {}", e.getMessage());
                return null;
            }
        } else  {
            // 直接上传文件，需要bucket具有公共写权限，目前只针对本地Docker开发环境
            return new ObsUploadRet(bucketName, fullName, minioConfig.getEndpoint() + "/" + bucketName + "/" + fullName);
        }
    }

    @Override
    public ObsUploadRet getUploadUrl(String contentType, String bucketName, String ObjectName) {
        log.info("MinioService::getUploadUrl 2");
        if (minioConfig.isPreSign()) {
            try {
                String url = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs
                                .builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(ObjectName)
                                .expiry((int) minioConfig.getUploadUrlExpire(), TimeUnit.SECONDS)
                                .build());
                return new ObsUploadRet(bucketName, ObjectName, url);
            } catch (Exception e) {
                log.error("MinioService getDownloadUrl error: {}", e.getMessage());
                return null;
            }
        } else  {
            // 直接上传文件，需要bucket具有公共写权限，目前只针对本地Docker开发环境
            return new ObsUploadRet(bucketName, ObjectName, minioConfig.getEndpoint() + "/" + bucketName + "/" + ObjectName);
        }
    }
}
