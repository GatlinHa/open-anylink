package com.hibob.anylink.mts.obs.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.obs.ObsService;
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
public class AliossService implements ObsService {
    private final AliossConfig aliossConfig;
    private final OSS aliossClient;

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        log.info("AliossService::uploadFile file");
        try {
            String prefixPath = FileType.determineFileType(fileName).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + fileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest(aliossConfig.getBucket(), fullName, file.getInputStream());
            aliossClient.putObject(putObjectRequest);
            return  "https://" + aliossConfig.getBucket() + "." + aliossConfig.getEndpoint() + "/" + fullName;
        } catch (Exception e) {
            log.error("AliossService upload file error: {}", e.getMessage());
            return "";
        }
    }

    @Override
    public String uploadFile(byte[] fileByte, String contentType, String fileName) {
        log.info("AliossService::uploadFile fileByte");
        try {
            String prefixPath = FileType.determineFileType(fileName).name();
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidPath = UUID.randomUUID().toString();
            String fullName = prefixPath + "/" + datePath + "/" + uuidPath + "/" + fileName;
            InputStream stream = new ByteArrayInputStream(fileByte);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliossConfig.getBucket(), fullName, stream, metadata);
            aliossClient.putObject(putObjectRequest);
            return  "https://" + aliossConfig.getBucket() + "." + aliossConfig.getEndpoint() + "/" + fullName;
        } catch (Exception e) {
            log.error("AliossService upload file error: {}", e.getMessage());
            return "";
        }
    }

}
