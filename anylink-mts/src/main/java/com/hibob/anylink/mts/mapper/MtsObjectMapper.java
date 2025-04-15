package com.hibob.anylink.mts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hibob.anylink.mts.entity.MtsObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MtsObjectMapper extends BaseMapper<MtsObject> {

    /**
     * 批量查询图片信息
     * @param objectIds 富媒体ID数组
     * @return List<ImageVO>
     */
    @Select("<script>" +
            " select t1.object_id as objectId, " +
            " t2.image_type as imageType, " +
            " t2.file_name as fileName, " +
            " t2.image_size as size, " +
            " t2.origin_width as originWidth, " +
            " t2.origin_height as originHeight, " +
            " t2.thumb_width as thumbWidth, " +
            " t2.thumb_height as thumbHeight, " +
            " t2.store_source as source, " +
            " t2.bucket_name as bucket, " +
            " t2.origin_path as originPath, " +
            " t2.thumb_path as thumbPath, " +
            " t1.created_time as createdTime " +
            " from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_image t2 " +
            " ON t1.foreign_id = t2.image_id " +
            " AND t1.store_type = t2.store_type " +
            " AND t2.uploaded = true " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<Map<String, Object>> batchSelectImage(List<Long> objectIds);

    /**
     * 批量查询音频信息
     * @param objectIds 富媒体ID数组
     * @return List<AudioVO>
     */
    @Select("<script>" +
            " select t1.object_id as objectId, " +
            " t2.audio_duration as duration, " +
            " t2.file_name as fileName, " +
            " t2.audio_size as size, " +
            " t2.store_source as source, " +
            " t2.bucket_name as bucket, " +
            " t2.full_path as fullPath, " +
            " t1.created_time as createdTime " +
            " from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_audio t2 " +
            " ON t1.foreign_id = t2.audio_id " +
            " AND t1.store_type = t2.store_type " +
            " AND t2.uploaded = true " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<Map<String, Object>> batchSelectAudio(List<Long> objectIds);

    /**
     * 批量查询视频信息
     * @param objectIds 富媒体ID数组
     * @return List<VideoVO>
     */
    @Select("<script>" +
            " select t1.object_id as objectId, " +
            " t2.file_name as fileName, " +
            " t2.video_size as size, " +
            " t2.store_source as source, " +
            " t2.bucket_name as bucket, " +
            " t2.full_path as fullPath, " +
            " t1.created_time as createdTime " +
            " from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_video t2 " +
            " ON t1.foreign_id = t2.video_id " +
            " AND t1.store_type = t2.store_type " +
            " AND t2.uploaded = true " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<Map<String, Object>> batchSelectVideo(List<Long> objectIds);

    /**
     * 批量查询Document信息
     * @param objectIds Document ID数组
     * @return List<DocumentVO>
     */
    @Select("<script>" +
            " select t1.object_id as objectId, " +
            " t2.document_type as documentType, " +
            " t2.file_name as fileName, " +
            " t2.document_size as size, " +
            " t2.store_source as source, " +
            " t2.bucket_name as bucket, " +
            " t2.full_path as fullPath, " +
            " t1.created_time as createdTime " +
            " from anylink_mts_object t1 " +
            " INNER JOIN anylink_mts_document t2 " +
            " ON t1.foreign_id = t2.document_id " +
            " AND t1.store_type = t2.store_type " +
            " AND t2.uploaded = true " +
            " AND t1.object_id IN " +
            " <foreach item='item' index='index' collection='objectIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<Map<String, Object>> batchSelectDocument(List<Long> objectIds);
}
