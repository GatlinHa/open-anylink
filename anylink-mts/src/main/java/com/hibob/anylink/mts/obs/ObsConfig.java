package com.hibob.anylink.mts.obs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ObsConfig {

    @Value("${obs.source}")
    private String source;

    @Value("${obs.document.max-limit}")
    private int documentMaxLimit;

    @Value("${obs.image.max-limit}")
    private int imageMaxLimit;

    @Value("${obs.image.thumb-size}")
    private int imageThumbSize;

    @Value("${obs.audio.max-limit}")
    private int audioMaxLimit;

    @Value("${obs.video.max-limit}")
    private int videoMaxLimit;

    @Value("${obs.ttl}")
    private int ttl;

    @Value("${obs.url-expire}")
    private long urlExpire;

}
