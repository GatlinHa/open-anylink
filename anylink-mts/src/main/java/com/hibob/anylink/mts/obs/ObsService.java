package com.hibob.anylink.mts.obs;

import org.springframework.web.multipart.MultipartFile;

public interface ObsService {

    String uploadFile(MultipartFile file, String fileName, int storeType);

    String uploadFile(byte[] fileByte, String contentType, String fileName, int storeType);
}
