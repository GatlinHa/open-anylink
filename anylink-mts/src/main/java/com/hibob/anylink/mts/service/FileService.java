package com.hibob.anylink.mts.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibob.anylink.common.constants.RedisKey;
import com.hibob.anylink.common.enums.ServiceErrorCode;
import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.common.session.ReqSession;
import com.hibob.anylink.common.utils.CommonUtil;
import com.hibob.anylink.common.utils.ResultUtil;
import com.hibob.anylink.common.utils.SnowflakeId;
import com.hibob.anylink.mts.dto.request.*;
import com.hibob.anylink.mts.dto.vo.AudioVO;
import com.hibob.anylink.mts.dto.vo.DocumentVO;
import com.hibob.anylink.mts.dto.vo.ImageVO;
import com.hibob.anylink.mts.dto.vo.VideoVO;
import com.hibob.anylink.mts.entity.*;
import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.enums.ObjectType;
import com.hibob.anylink.mts.mapper.*;
import com.hibob.anylink.mts.obs.ObsConfig;
import com.hibob.anylink.mts.obs.ObsFactory;
import com.hibob.anylink.mts.obs.ObsService;
import com.hibob.anylink.mts.obs.ObsUploadRet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObsConfig obsConfig;
    private final ObsService obsService;
    private final MtsObjectMapper mtsObjectMapper;
    private final MtsImageMapper mtsImageMapper;
    private final MtsAudioMapper mtsAudioMapper;
    private final MtsVideoMapper mtsVideoMapper;
    private final MtsDocumentMapper mtsDocumentMapper;
    private SnowflakeId snowflakeId = null;

    public ResponseEntity<IMHttpResponse> getUploadUrl(GetUploadUrlReq dto) {
        log.info("FileService::getUploadUrl");
        String contentType = dto.getFileRawType();
        String fileName = dto.getFileName();
        FileType fileType = FileType.determineFileType(contentType);
        switch (fileType) {
            case IMAGE:
                if (!FileType.checkExtension(FileType.IMAGE, fileName)) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_IMAGE_FORMAT_ERROR);
                }
                return getUploadUrlForImage(dto);
            case AUDIO:
                if (!FileType.checkExtension(FileType.AUDIO, fileName)) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_AUDIO_FORMAT_ERROR);
                }
                return getUploadUrlForAudio(dto);
            case VIDEO:
                if (!FileType.checkExtension(FileType.VIDEO, fileName)) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_VIDEO_FORMAT_ERROR);
                }
                return getUploadUrlForVideo(dto);
            case TEXT:
            case DOCUMENT:
            default:
                return getUploadUrlForDocument(dto);
        }
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> getUploadUrlForImage(GetUploadUrlReq dto) {
        // 大小校验
        if (dto.getSize() > (long) obsConfig.getImageMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_IMAGE_TOO_BIG);
        }

        ImageVO vo = new ImageVO();
        long objectId = generateObjectId(); // 使用雪花算法生成ID，每次都生成不同
        String imageId = dto.getMd5(); // 以md5作为imageId
        int storeType = dto.getStoreType();

        // 先插入对象记录
        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.IMAGE.value());
        mtsObject.setStoreType(dto.getStoreType());
        mtsObject.setForeignId(imageId);
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        MtsImage mtsImage = null;
        LambdaQueryWrapper<MtsImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MtsImage::getImageId, imageId)
                .eq(MtsImage::getStoreType, storeType);
        List<MtsImage> mtsImages = mtsImageMapper.selectList(queryWrapper);
        if (mtsImages != null && !mtsImages.isEmpty()) {
            mtsImage = mtsImages.get(0);
        }
        if (mtsImage != null && mtsImage.isUploaded()) {
            // 如果这个图片已经上传过，直接返回下载url
            String redisKey = RedisKey.MTS_IMAGE_URL + objectId;
            String source = mtsImage.getStoreSource();
            String bucket = mtsImage.getBucketName();
            String originPath = mtsImage.getOriginPath();
            String thumbPath = mtsImage.getThumbPath();
            String originUrl = ObsFactory.getObsService(source).getDownloadUrl(bucket, originPath);
            String thumbUrl = ObsFactory.getObsService(source).getDownloadUrl(bucket, thumbPath);
            if (!StringUtils.hasLength(originUrl) || !StringUtils.hasLength(thumbUrl)) {
                return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_DOWNLOAD_PRESIGN_URL);
            }
            Map<String, String> map = new HashMap<>();
            map.put("originUrl", originUrl);
            map.put("thumbUrl", thumbUrl);
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(map), Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));

            vo.setScope(1);
            vo.setObjectId(objectId);
            vo.setOriginUrl(originUrl);
            vo.setThumbUrl(thumbUrl);
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        } else {
            ObsUploadRet originObsUploadRet;
            ObsUploadRet thumbObsUploadRet;
            if (mtsImage != null && !mtsImage.isUploaded()) {
                // 如果mtsImage有记录，但是isUploaded状态为false，表示这个文件没有上传成功，这里直接复用之前的记录
                String contentType = mtsImage.getImageType();
                String bucketName = mtsImage.getBucketName();
                String originPath = mtsImage.getOriginPath();
                String  thumbPath = mtsImage.getThumbPath();
                originObsUploadRet = obsService.getUploadUrl(contentType, bucketName, originPath);
                if (dto.getSize() < obsConfig.getImageThumbSize()) {
                    thumbObsUploadRet = originObsUploadRet;
                } else {
                    thumbObsUploadRet = obsService.getUploadUrl(contentType, bucketName, thumbPath);
                }
                if (originObsUploadRet == null || thumbObsUploadRet == null) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }
            } else {
                String contentType = dto.getFileRawType();
                originObsUploadRet = obsService.getUploadUrl(contentType, generateRandomFileName(dto.getFileName()), storeType);
                if (dto.getSize() < obsConfig.getImageThumbSize()) {
                    thumbObsUploadRet = originObsUploadRet;
                } else {
                    thumbObsUploadRet = obsService.getUploadUrl(contentType, generateRandomFileName(dto.getFileName()), storeType);
                }
                if (originObsUploadRet == null || thumbObsUploadRet == null) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }

                // 插入image记录
                mtsImage = new MtsImage();
                mtsImage.setImageId(imageId);
                mtsImage.setStoreType(storeType);
                mtsImage.setImageType(contentType);
                mtsImage.setImageSize(dto.getSize());
                mtsImage.setOriginWidth(dto.getOriginWidth());
                mtsImage.setOriginHeight(dto.getOriginHeight());
                mtsImage.setThumbWidth(dto.getThumbWidth());
                mtsImage.setThumbHeight(dto.getThumbHeight());
                mtsImage.setFileName(truncateFileName(dto.getFileName()));
                mtsImage.setStoreSource(obsConfig.getSource());
                mtsImage.setBucketName(originObsUploadRet.getBucket());
                mtsImage.setOriginPath(originObsUploadRet.getFullPath());
                mtsImage.setThumbPath(thumbObsUploadRet.getFullPath());
                mtsImage.setCreatedAccount(ReqSession.getSession().getAccount());
                mtsImage.setExpire(obsConfig.getTtl() * 86400L);
                mtsImage.setUploaded(false); // 这里默认是false，需要等前端下一个上传成功请求才修改这里状态
                mtsImageMapper.insert(mtsImage);
            }

            // 返回结果
            vo.setScope(0);
            vo.setObjectId(objectId);
            vo.setUploadOriginUrl(originObsUploadRet.getUrl());
            vo.setUploadThumbUrl(thumbObsUploadRet.getUrl());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> getUploadUrlForAudio(GetUploadUrlReq dto) {
        // 大小校验
        if (dto.getSize() > (long) obsConfig.getAudioMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_AUDIO_TOO_BIG);
        }

        AudioVO vo = new AudioVO();
        long objectId = generateObjectId(); // 使用雪花算法生成ID，每次都生成不同
        String audioId = dto.getMd5(); // 以md5作为audioId
        int storeType = dto.getStoreType();

        // 先插入对象记录
        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.AUDIO.value());
        mtsObject.setStoreType(dto.getStoreType());
        mtsObject.setForeignId(audioId);
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        MtsAudio mtsAudio = null;
        LambdaQueryWrapper<MtsAudio> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MtsAudio::getAudioId, audioId)
                .eq(MtsAudio::getStoreType, storeType);
        List<MtsAudio> mtsAudios = mtsAudioMapper.selectList(queryWrapper);
        if (mtsAudios != null && !mtsAudios.isEmpty()) {
            mtsAudio = mtsAudios.get(0);
        }
        if (mtsAudio != null && mtsAudio.isUploaded()) {
            // 如果这个图片已经上传过，直接返回下载url
            String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
            String source = mtsAudio.getStoreSource();
            String bucketName = mtsAudio.getBucketName();
            String fullPath = mtsAudio.getFullPath();
            String url = ObsFactory.getObsService(source).getDownloadUrl(bucketName, fullPath);
            redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));

            vo.setScope(1);
            vo.setObjectId(objectId);
            vo.setDownloadUrl(url);
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        } else {
            ObsUploadRet obsUploadRet;
            if (mtsAudio != null && !mtsAudio.isUploaded()) {
                // 如果mtsAudio有记录，但是isUploaded状态为false，表示这个文件没有上传成功，这里直接复用之前的记录
                String contentType = mtsAudio.getAudioType();
                String bucketName = mtsAudio.getBucketName();
                String fullPath = mtsAudio.getFullPath();
                obsUploadRet = obsService.getUploadUrl(contentType, bucketName, fullPath);
                if (!StringUtils.hasLength(obsUploadRet.getUrl()) ) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }
            } else {
                String contentType = dto.getFileRawType();
                obsUploadRet = obsService.getUploadUrl(contentType, generateRandomFileName(dto.getFileName()), storeType);
                if (obsUploadRet == null) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }

                // 插入audio记录
                mtsAudio = new MtsAudio();
                mtsAudio.setAudioId(audioId);
                mtsAudio.setStoreType(storeType);
                mtsAudio.setAudioType(contentType);
                mtsAudio.setAudioSize(dto.getSize());
                mtsAudio.setAudioDuration(dto.getAudioDuration());
                mtsAudio.setFileName(truncateFileName(dto.getFileName()));
                mtsAudio.setStoreSource(obsConfig.getSource());
                mtsAudio.setBucketName(obsUploadRet.getBucket());
                mtsAudio.setFullPath(obsUploadRet.getFullPath());
                mtsAudio.setCreatedAccount(ReqSession.getSession().getAccount());
                mtsAudio.setExpire(obsConfig.getTtl() * 86400L);
                mtsAudio.setUploaded(false);// 这里默认是false，需要等前端下一个上传成功请求才修改这里状态
                mtsAudioMapper.insert(mtsAudio);
            }

            // 返回结果
            vo.setScope(0);
            vo.setObjectId(objectId);
            vo.setUploadUrl(obsUploadRet.getUrl());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

    }

    @Transactional
    public ResponseEntity<IMHttpResponse> getUploadUrlForVideo(GetUploadUrlReq dto) {
        // 大小校验
        if (dto.getSize() > (long) obsConfig.getVideoMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_VIDEO_TOO_BIG);
        }

        VideoVO vo = new VideoVO();
        long objectId = generateObjectId(); // 使用雪花算法生成ID，每次都生成不同
        String videoId = dto.getMd5(); // 以md5作为videoId
        int storeType = dto.getStoreType();

        // 先插入对象记录
        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.VIDEO.value());
        mtsObject.setStoreType(dto.getStoreType());
        mtsObject.setForeignId(videoId);
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        MtsVideo mtsVideo = null;
        LambdaQueryWrapper<MtsVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MtsVideo::getVideoId, videoId)
                .eq(MtsVideo::getStoreType, storeType);
        List<MtsVideo> mtsVideos = mtsVideoMapper.selectList(queryWrapper);
        if (mtsVideos != null && !mtsVideos.isEmpty()) {
            mtsVideo = mtsVideos.get(0);
        }
        if (mtsVideo != null && mtsVideo.isUploaded()) {
            // 如果这个图片已经上传过，直接返回下载url
            String redisKey = RedisKey.MTS_OBJECT_URL + objectId;

            String source = mtsVideo.getStoreSource();
            String bucketName = mtsVideo.getBucketName();
            String fullPath = mtsVideo.getFullPath();
            String url = ObsFactory.getObsService(source).getDownloadUrl(bucketName, fullPath);
            redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));

            vo.setScope(1);
            vo.setObjectId(objectId);
            vo.setDownloadUrl(url);
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        } else {
            ObsUploadRet obsUploadRet;
            if (mtsVideo != null && !mtsVideo.isUploaded()) {
                // 如果mtsVideo有记录，但是isUploaded状态为false，表示这个文件没有上传成功，这里直接复用之前的记录
                String contentType = mtsVideo.getVideoType();
                String bucketName = mtsVideo.getBucketName();
                String fullPath = mtsVideo.getFullPath();
                obsUploadRet = obsService.getUploadUrl(contentType, bucketName, fullPath);
                if (!StringUtils.hasLength(obsUploadRet.getUrl()) ) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }
            } else {
                String contentType = dto.getFileRawType();
                obsUploadRet = obsService.getUploadUrl(contentType, generateRandomFileName(dto.getFileName()), storeType);
                if (obsUploadRet == null) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }

                // 插入video记录
                mtsVideo = new MtsVideo();
                mtsVideo.setVideoId(videoId);
                mtsVideo.setStoreType(storeType);
                mtsVideo.setVideoType(contentType);
                mtsVideo.setVideoSize(dto.getSize());
                mtsVideo.setVideoWidth(dto.getVideoWidth());
                mtsVideo.setVideoHeight(dto.getVideoHeight());
                mtsVideo.setFileName(truncateFileName(dto.getFileName()));
                mtsVideo.setStoreSource(obsConfig.getSource());
                mtsVideo.setBucketName(obsUploadRet.getBucket());
                mtsVideo.setFullPath(obsUploadRet.getFullPath());
                mtsVideo.setCreatedAccount(ReqSession.getSession().getAccount());
                mtsVideo.setExpire(obsConfig.getTtl() * 86400L);
                mtsVideo.setUploaded(false);// 这里默认是false，需要等前端下一个上传成功请求才修改这里状态
                mtsVideoMapper.insert(mtsVideo);
            }

            // 返回结果
            vo.setScope(0);
            vo.setObjectId(objectId);
            vo.setUploadUrl(obsUploadRet.getUrl());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

    }


    @Transactional
    public ResponseEntity<IMHttpResponse> getUploadUrlForDocument(GetUploadUrlReq dto) {
        // 大小校验
        if (dto.getSize() > (long) obsConfig.getDocumentMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_TOO_BIG);
        }

        DocumentVO vo = new DocumentVO();
        long objectId = generateObjectId(); // 使用雪花算法生成ID，每次都生成不同
        String documentId = dto.getMd5(); // 以md5作为documentId
        int storeType = dto.getStoreType();

        // 先插入对象记录
        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.DOCUMENT.value());
        mtsObject.setStoreType(dto.getStoreType());
        mtsObject.setForeignId(documentId);
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        MtsDocument mtsDocument = null;
        LambdaQueryWrapper<MtsDocument> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MtsDocument::getDocumentId, documentId)
                .eq(MtsDocument::getStoreType, storeType);
        List<MtsDocument> mtsDocuments = mtsDocumentMapper.selectList(queryWrapper);
        if (mtsDocuments != null && !mtsDocuments.isEmpty()) {
            mtsDocument = mtsDocuments.get(0);
        }
        if (mtsDocument != null && mtsDocument.isUploaded()) {
            // 如果这个图片已经上传过，直接返回下载url
            String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
            String source = mtsDocument.getStoreSource();
            String bucketName = mtsDocument.getBucketName();
            String fullPath = mtsDocument.getFullPath();
            String url = ObsFactory.getObsService(source).getDownloadUrl(bucketName, fullPath);
            redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));

            vo.setScope(1);
            vo.setObjectId(objectId);
            vo.setDownloadUrl(url);
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        } else {
            ObsUploadRet obsUploadRet;
            if (mtsDocument != null && !mtsDocument.isUploaded()) {
                // 如果mtsDocument有记录，但是isUploaded状态为false，表示这个文件没有上传成功，这里直接复用之前的记录
                String contentType = mtsDocument.getDocumentType();
                String bucketName = mtsDocument.getBucketName();
                String fullPath = mtsDocument.getFullPath();
                obsUploadRet = obsService.getUploadUrl(contentType, bucketName, fullPath);
                if (!StringUtils.hasLength(obsUploadRet.getUrl()) ) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }
            } else {
                String contentType = dto.getFileRawType();
                obsUploadRet = obsService.getUploadUrl(contentType, generateRandomFileName(dto.getFileName()), storeType);
                if (obsUploadRet == null) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_PRESIGN_URL);
                }

                // 插入document记录
                mtsDocument = new MtsDocument();
                mtsDocument.setDocumentId(documentId);
                mtsDocument.setStoreType(storeType);
                mtsDocument.setDocumentType(contentType);
                mtsDocument.setDocumentSize(dto.getSize());
                mtsDocument.setFileName(truncateFileName(dto.getFileName()));
                mtsDocument.setStoreSource(obsConfig.getSource());
                mtsDocument.setBucketName(obsUploadRet.getBucket());
                mtsDocument.setFullPath(obsUploadRet.getFullPath());
                mtsDocument.setCreatedAccount(ReqSession.getSession().getAccount());
                mtsDocument.setExpire(obsConfig.getTtl() * 86400L);
                mtsDocument.setUploaded(false);// 这里默认是false，需要等前端下一个上传成功请求才修改这里状态
                mtsDocumentMapper.insert(mtsDocument);
            }

            // 返回结果
            vo.setScope(0);
            vo.setObjectId(objectId);
            vo.setUploadUrl(obsUploadRet.getUrl());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

    }

    public ResponseEntity<IMHttpResponse> reportUploaded(ReportUploaded dto) {
        // 根据objectId查询到媒体Id，媒体类型，储存类型
        MtsObject mtsObject = mtsObjectMapper.selectById(dto.getObjectId());
        int objectType = mtsObject.getObjectType();
        if (objectType == ObjectType.IMAGE.value()) {
            updateIsUploadedForImage(mtsObject.getForeignId(), mtsObject.getStoreType());
            return ResultUtil.success(getImageVo(dto.getObjectId()));
        } else if (objectType == ObjectType.AUDIO.value()) {
            updateIsUploadedForAudio(mtsObject.getForeignId(), mtsObject.getStoreType());
            return ResultUtil.success(getAudioVo(dto.getObjectId()));
        } else if (objectType == ObjectType.VIDEO.value()) {
            updateIsUploadedForVideo(mtsObject.getForeignId(), mtsObject.getStoreType());
            return ResultUtil.success(getVideoVo(dto.getObjectId()));
        } else {
            updateIsUploadedForDocument(mtsObject.getForeignId(), mtsObject.getStoreType());
            return ResultUtil.success(getDocumentVo(dto.getObjectId()));
        }
    }

    public ResponseEntity<IMHttpResponse> image(ImageReq dto) {
        List<ImageVO> voList = getImageVOS(dto.getObjectIds());
        return ResultUtil.success(voList);
    }

    public ResponseEntity<IMHttpResponse> audio(AudioReq dto) {
        List<AudioVO> voList = getAudioVOS(dto.getObjectIds());
        return ResultUtil.success(voList);
    }

    public ResponseEntity<IMHttpResponse> video(VideoReq dto) {
        List<VideoVO> voList = getVideoVOS(dto.getObjectIds());
        return ResultUtil.success(voList);
    }

    public ResponseEntity<IMHttpResponse> document(DocumentReq dto) {
        List<DocumentVO> voList = getDocumentVOS(dto.getObjectIds());
        return ResultUtil.success(voList);
    }

    public List<ImageVO> getImageVOS(List<Long> objectIds) {
        List<Map<String, Object>> list = mtsObjectMapper.batchSelectImage(objectIds);
        List<ImageVO> voList = list.stream().map((item) -> {
            Long objectId = (Long) item.get("objectId");
            String imageType = item.get("imageType").toString();
            String fileName = item.get("fileName").toString();
            long size = (long) item.get("size");
            int originWidth = (int) item.get("originWidth");
            int originHeight = (int) item.get("originHeight");
            int thumbWidth = (int) item.get("thumbWidth");
            int thumbHeight = (int) item.get("thumbHeight");
            String source = item.get("source").toString();
            String bucket = item.get("bucket").toString();
            String originPath = item.get("originPath").toString();
            String thumbPath = item.get("thumbPath").toString();
            LocalDateTime createdTime = (LocalDateTime)item.get("createdTime");

            String originUrl = "";
            String thumbUrl = "";

            String redisKey = RedisKey.MTS_IMAGE_URL + objectId;
            String o = (String) redisTemplate.opsForValue().get(redisKey);
            if (o != null) {
                try {
                    Map<String, Object> map = new ObjectMapper().readValue(o, Map.class);
                    originUrl = (String) map.get("originUrl");
                    thumbUrl = (String) map.get("thumbUrl");
                } catch (JsonProcessingException e) {
                    log.error("json format for image url in redis is failed, exception is {}", e.getMessage());
                }
            }

            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(originUrl) || !StringUtils.hasLength(thumbUrl)) {
                originUrl = ObsFactory.getObsService(source).getDownloadUrl(bucket, originPath);
                thumbUrl = ObsFactory.getObsService(source).getDownloadUrl(bucket, thumbPath);
                Map<String, String> map = new HashMap<>();
                map.put("originUrl", originUrl);
                map.put("thumbUrl", thumbUrl);
                redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(map), Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));
            }

            ImageVO vo = new ImageVO();
            vo.setObjectId(objectId);
            vo.setOriginUrl(originUrl);
            vo.setThumbUrl(thumbUrl);
            vo.setImageType(imageType);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setOriginWidth(originWidth);
            vo.setOriginHeight(originHeight);
            vo.setThumbWidth(thumbWidth);
            vo.setThumbHeight(thumbHeight);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    public List<AudioVO> getAudioVOS(List<Long> objectIds) {
        List<Map<String, Object>> list = mtsObjectMapper.batchSelectAudio(objectIds);
        List<AudioVO> voList = list.stream().map((item) -> {
            Long objectId = (Long) item.get("objectId");
            int duration = (int) item.get("duration");
            String fileName = item.get("fileName").toString();
            long size = (long) item.get("size");
            String source = item.get("source").toString();
            String bucket = item.get("bucket").toString();
            String fullPath = item.get("fullPath").toString();
            LocalDateTime createdTime = (LocalDateTime)item.get("createdTime");

            String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
            String url = (String) redisTemplate.opsForValue().get(redisKey);
            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(url)) {
                url = ObsFactory.getObsService(source).getDownloadUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));
            }

            AudioVO vo = new AudioVO();
            vo.setObjectId(objectId);
            vo.setDownloadUrl(url);
            vo.setDuration(duration);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    public List<VideoVO> getVideoVOS(List<Long> objectIds) {
        List<Map<String, Object>> list = mtsObjectMapper.batchSelectVideo(objectIds);
        List<VideoVO> voList = list.stream().map((item) -> {
            Long objectId = (Long) item.get("objectId");
            String fileName = item.get("fileName").toString();
            long size = (long) item.get("size");
            int width = (int) item.get("width");
            int height = (int) item.get("height");
            String source = item.get("source").toString();
            String bucket = item.get("bucket").toString();
            String fullPath = item.get("fullPath").toString();
            LocalDateTime createdTime = (LocalDateTime)item.get("createdTime");

            String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
            String url = (String) redisTemplate.opsForValue().get(redisKey);
            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(url)) {
                url = ObsFactory.getObsService(source).getDownloadUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));
            }

            VideoVO vo = new VideoVO();
            vo.setObjectId(objectId);
            vo.setDownloadUrl(url);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setWidth(width);
            vo.setHeight(height);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    public List<DocumentVO> getDocumentVOS(List<Long> objectIds) {
        List<Map<String, Object>> list = mtsObjectMapper.batchSelectDocument(objectIds);
        List<DocumentVO> voList = list.stream().map((item) -> {
            Long objectId = (Long) item.get("objectId");
            String documentType = item.get("documentType").toString();
            String fileName = item.get("fileName").toString();
            long size = (long) item.get("size");
            String source = item.get("source").toString();
            String bucket = item.get("bucket").toString();
            String fullPath = item.get("fullPath").toString();
            LocalDateTime createdTime = (LocalDateTime)item.get("createdTime");

            String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
            String url = (String) redisTemplate.opsForValue().get(redisKey);
            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(url)) {
                url = ObsFactory.getObsService(source).getDownloadUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getDownloadUrlExpire()));
            }

            DocumentVO vo = new DocumentVO();
            vo.setObjectId(objectId);
            vo.setDocumentType(documentType);
            vo.setDownloadUrl(url);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    private int updateIsUploadedForImage(String imageId, int storeType) {
        LambdaUpdateWrapper<MtsImage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MtsImage::getImageId, imageId)
                .eq(MtsImage::getStoreType, storeType)
                .set(MtsImage::isUploaded, true);
        return mtsImageMapper.update(updateWrapper);
    }

    private int updateIsUploadedForAudio(String audioId, int storeType) {
        LambdaUpdateWrapper<MtsAudio> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MtsAudio::getAudioId, audioId)
                .eq(MtsAudio::getStoreType, storeType)
                .set(MtsAudio::isUploaded, true);
        return mtsAudioMapper.update(updateWrapper);
    }

    private int updateIsUploadedForVideo(String videoId, int storeType) {
        LambdaUpdateWrapper<MtsVideo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MtsVideo::getVideoId, videoId)
                .eq(MtsVideo::getStoreType, storeType)
                .set(MtsVideo::isUploaded, true);
        return mtsVideoMapper.update(updateWrapper);
    }

    private int updateIsUploadedForDocument(String documentId, int storeType) {
        LambdaUpdateWrapper<MtsDocument> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MtsDocument::getDocumentId, documentId)
                .eq(MtsDocument::getStoreType, storeType)
                .set(MtsDocument::isUploaded, true);
        return mtsDocumentMapper.update(updateWrapper);
    }

    private ImageVO getImageVo(Long objectId) {
        List<Long> objectIds = Arrays.asList(objectId);
        List<ImageVO> imageVOS = getImageVOS(objectIds);
        if (imageVOS != null && !imageVOS.isEmpty()) {
            return imageVOS.get(0);
        } else {
            return null;
        }
    }

    private AudioVO getAudioVo(Long objectId) {
        List<Long> objectIds = Arrays.asList(objectId);
        List<AudioVO> audioVOS = getAudioVOS(objectIds);
        if (audioVOS != null && !audioVOS.isEmpty()) {
            return audioVOS.get(0);
        } else {
            return null;
        }
    }

    private VideoVO getVideoVo(Long objectId) {
        List<Long> objectIds = Arrays.asList(objectId);
        List<VideoVO> videoVOS = getVideoVOS(objectIds);
        if (videoVOS != null && !videoVOS.isEmpty()) {
            return videoVOS.get(0);
        } else {
            return null;
        }
    }

    private DocumentVO getDocumentVo(Long objectId) {
        List<Long> objectIds = Arrays.asList(objectId);
        List<DocumentVO> documentVOS = getDocumentVOS(objectIds);
        if (documentVOS != null && !documentVOS.isEmpty()) {
            return documentVOS.get(0);
        } else {
            return null;
        }
    }

    private long generateObjectId() {
        if (snowflakeId == null) { // 懒加载
            snowflakeId = SnowflakeId.getInstance();
        }
        return snowflakeId.nextId();
    }

    /**
     * 截取文件名，最大保留128位
     * @param fileName
     * @return
     */
    private String truncateFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int truncateLength = 128;
        // 查找最后一个点的位置
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // 如果没有扩展名，直接截取前64位
            return fileName.length() <= truncateLength ? fileName : fileName.substring(0, truncateLength);
        }
        // 分离文件名主体和扩展名
        String nameWithoutExtension = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex);
        // 截取文件名主体部分
        String truncatedName = nameWithoutExtension.length() <= truncateLength ? nameWithoutExtension : nameWithoutExtension.substring(0, truncateLength);
        // 重新组合文件名
        return truncatedName + extension;
    }

    /**
     * 上传的文件生成随机文件名，用于保存在对象存储中
     * @param fileName 原始文件名
     * @return
     */
    private String generateRandomFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        String randomStr = CommonUtil.generateRandomStr(16);
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return randomStr;
        } else {
            return randomStr + fileName.substring(lastDotIndex);
        }
    }
}
