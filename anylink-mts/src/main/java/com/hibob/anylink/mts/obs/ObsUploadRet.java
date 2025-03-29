package com.hibob.anylink.mts.obs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ObsUploadRet {

    private final String bucket;

    private final String fullPath;

    private final String url;
}
