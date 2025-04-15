package com.hibob.anylink.mts.obs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ObsUploadRet {

    /**
     * 桶名
     */
    private final String bucket;

    /**
     * 全路径名
     */
    private final String fullPath;

    /**
     * 预签名URL
     */
    private final String url;
}
