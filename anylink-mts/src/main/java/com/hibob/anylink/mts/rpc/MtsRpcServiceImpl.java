package com.hibob.anylink.mts.rpc;

import com.hibob.anylink.common.rpc.service.MtsRpcService;
import com.hibob.anylink.mts.dto.vo.AudioVO;
import com.hibob.anylink.mts.dto.vo.DocumentVO;
import com.hibob.anylink.mts.dto.vo.ImageVO;
import com.hibob.anylink.mts.dto.vo.VideoVO;
import com.hibob.anylink.mts.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class MtsRpcServiceImpl implements MtsRpcService {
    private final FileService fileService;

    @Override
    public Map<String, Map<String, Object>> queryImageSignUrl(List<Long> objectIds) {
        List<ImageVO> imageVOS = fileService.getImageVOS(objectIds);
        Map<String, Map<String, Object>> result = new HashMap<>();
        imageVOS.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("originUrl", item.getOriginUrl());
            map.put("thumbUrl", item.getThumbUrl());
            result.put(item.getObjectId(), map);
        });
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> queryAudioSignUrl(List<Long> objectIds) {
        List<AudioVO> audioVOS = fileService.getAudioVOS(objectIds);
        Map<String, Map<String, Object>> result = new HashMap<>();
        audioVOS.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("url", item.getUrl());
            result.put(item.getObjectId(), map);
        });
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> queryVideoSignUrl(List<Long> objectIds) {
        List<VideoVO> videoVOS = fileService.getVideoVOS(objectIds);
        Map<String, Map<String, Object>> result = new HashMap<>();
        videoVOS.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("url", item.getUrl());
            result.put(item.getObjectId(), map);
        });
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> queryDocumentSignUrl(List<Long> objectIds) {
        List<DocumentVO> documentVOS = fileService.getDocumentVOS(objectIds);
        Map<String, Map<String, Object>> result = new HashMap<>();
        documentVOS.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("url", item.getUrl());
            result.put(item.getObjectId(), map);
        });
        return result;
    }
}
