package com.hibob.anylink.mts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hibob.anylink.mts.dto.vo.AudioVO;
import com.hibob.anylink.mts.dto.vo.ImageVO;
import com.hibob.anylink.mts.entity.MtsObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MtsObjectMapper extends BaseMapper<MtsObject> {

    /**
     * 批量查询图片信息
     * @param objectIds 富媒体ID数组
     * @return List<ImageVO>
     */
    @Select("<script>" +
            " select t1.object_id, t2.origin_url, t2.thumb_url from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_image t2 " +
            " ON t1.foreign_id = t2.image_id " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<ImageVO> batchSelectImage(List<String> objectIds);

    /**
     * 查询音频信息
     * @param objectId 富媒体ID
     * @return AudioVO
     */
    @Select("<script>" +
            " select t1.object_id, t2.url from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_audio t2 " +
            " ON t1.foreign_id = t2.audio_id " +
            " AND t1.object_id = #{objectId} " +
            "</script>")
    List<AudioVO> selectAudio(String objectId);
}
