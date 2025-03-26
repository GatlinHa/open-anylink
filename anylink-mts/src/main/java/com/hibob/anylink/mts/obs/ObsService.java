package com.hibob.anylink.mts.obs;

import org.springframework.web.multipart.MultipartFile;

public interface ObsService {

    String uploadFile(MultipartFile file, String randomFileName, int storeType);

    String uploadFile(byte[] fileByte, String contentType, String randomFileName, int storeType);
}
