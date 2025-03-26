package com.hibob.anylink.mts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hibob.anylink.mts.dto.vo.AudioVO;
import com.hibob.anylink.mts.dto.vo.DocumentVO;
import com.hibob.anylink.mts.dto.vo.ImageVO;
import com.hibob.anylink.mts.dto.vo.VideoVO;
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
            " select t1.object_id, t2.origin_url, t2.thumb_url, t2.file_name, t2.image_size as size from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_image t2 " +
            " ON t1.foreign_id = t2.image_id " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<ImageVO> batchSelectImage(List<String> objectIds);

    /**
     * 批量查询音频信息
     * @param objectIds 富媒体ID数组
     * @return List<AudioVO>
     */
    @Select("<script>" +
            " select t1.object_id, t2.url, t2.audio_duration as duration, t2.file_name, t2.audio_size as size from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_audio t2 " +
            " ON t1.foreign_id = t2.audio_id " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<AudioVO> batchSelectAudio(List<String> objectIds);

    /**
     * 批量查询视频信息
     * @param objectIds 富媒体ID数组
     * @return List<VideoVO>
     */
    @Select("<script>" +
            " select t1.object_id, t2.url, t2.file_name, t2.video_size as size from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_video t2 " +
            " ON t1.foreign_id = t2.video_id " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<VideoVO> batchSelectVideo(List<String> objectIds);

    /**
     * 批量查询Document信息
     * @param objectIds Document ID数组
     * @return List<DocumentVO>
     */
    @Select("<script>" +
            " select t1.object_id, t2.document_type, t2.url, t2.file_name, t2.document_size as size from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_document t2 " +
            " ON t1.foreign_id = t2.document_id " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<DocumentVO> batchSelectDocument(List<String> objectIds);
}
