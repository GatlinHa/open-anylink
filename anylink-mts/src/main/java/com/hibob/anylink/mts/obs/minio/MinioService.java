package com.hibob.anylink.mts.obs.minio;

import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.obs.ObsService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService implements ObsService {
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @Override
    public String uploadFile(MultipartFile file, String fileName, int storeType) {
        log.info("MinioService::uploadFile file");
        String bucket = 0 == storeType ? minioConfig.getBucketLong() : minioConfig.getBucketTtl();
        try {
            String prefixPath = FileType.determineFileType(fileName).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + fileName;

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fullName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return  minioConfig.getEndpoint() + "/" + bucket + "/" + fullName;
        }
        catch (Exception e) {
            log.error("MinioService upload file error: {}", e.getMessage());
            return "";
        }
    }

    @Override
    public String uploadFile(byte[] fileByte, String contentType, String fileName, int storeType) {
        log.info("MinioService::uploadFile fileByte");
        String bucket = 0 == storeType ? minioConfig.getBucketLong() : minioConfig.getBucketTtl();
        try {
            String prefixPath = FileType.determineFileType(fileName).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + fileName;

            InputStream stream = new ByteArrayInputStream(fileByte);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fullName)
                    .stream(stream, fileByte.length, -1)
                    .contentType(contentType)
                    .build());
            return  minioConfig.getEndpoint() + "/" + bucket + "/" + fullName;
        }
        catch (Exception e) {
            log.error("MinioService upload file error: {}", e.getMessage());
            return "";
        }
    }
}
