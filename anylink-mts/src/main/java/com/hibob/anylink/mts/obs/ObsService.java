package com.hibob.anylink.mts.obs;

import org.springframework.web.multipart.MultipartFile;

public interface ObsService {

    ObsUploadRet uploadFile(MultipartFile file, String randomFileName, int storeType);

    ObsUploadRet uploadFile(byte[] fileByte, String contentType, String randomFileName, int storeType);

    String getSignUrl(String bucketName, String ObjectName);
}
