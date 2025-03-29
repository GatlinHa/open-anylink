package com.hibob.anylink.common.rpc.service;

import java.util.List;
import java.util.Map;

public interface MtsRpcService {

    /**
     * 批量返回image对象的原图url和缩略图url（短期有效的签名url）
     * @param objectIds
     * @return
     */
    Map<String, Map<String, Object>> queryImageSignUrl(List<Long> objectIds);

    /**
     * 批量返回音频的url（短期有效的签名url）
     * @param objectIds
     * @return
     */
    Map<String, Map<String, Object>> queryAudioSignUrl(List<Long> objectIds);

    /**
     * 批量返回视频的url（短期有效的签名url）
     * @param objectIds
     * @return
     */
    Map<String, Map<String, Object>> queryVideoSignUrl(List<Long> objectIds);

    /**
     * 批量返回文档的url（短期有效的签名url）
     * @param objectIds
     * @return
     */
    Map<String, Map<String, Object>> queryDocumentSignUrl(List<Long> objectIds);
}
