package com.hibob.anylink.mts.obs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ObsConfig {

    @Value("${obs.file.max-limit}")
    private int fileMaxLimit;

    @Value("${obs.image.max-limit}")
    private int imageMaxLimit;

    @Value("${obs.image.thumb-size}")
    private int imageThumbSize;

    @Value("${obs.audio.max-limit}")
    private int audioMaxLimit;

    @Value("${obs.ttl}")
    private int ttl;
}
