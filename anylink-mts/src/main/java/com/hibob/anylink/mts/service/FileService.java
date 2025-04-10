package com.hibob.anylink.mts.service;

import com.alibaba.fastjson.JSON;
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
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDateTime;
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

    public ResponseEntity<IMHttpResponse> upload(UploadReq dto) {
        log.info("FileService::upload");
        FileType fileType = FileType.determineFileType(dto.getFile().getContentType());
        String fileName = dto.getFile().getOriginalFilename();
        switch (fileType) {
            case IMAGE:
                if (!FileType.checkExtension(FileType.IMAGE, fileName)) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_IMAGE_FORMAT_ERROR);
                }
                return uploadImage(dto);
            case AUDIO:
                if (!FileType.checkExtension(FileType.AUDIO, fileName)) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_AUDIO_FORMAT_ERROR);
                }
                return uploadAudio(dto);
            case VIDEO:
                if (!FileType.checkExtension(FileType.VIDEO, fileName)) {
                    return ResultUtil.error(ServiceErrorCode.ERROR_MTS_VIDEO_FORMAT_ERROR);
                }
                return uploadVideo(dto);
            case TEXT:
            case DOCUMENT:
            default:
                return uploadDocument(dto);
        }
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> uploadAudio(UploadReq dto) {
        MultipartFile file = dto.getFile();
        String fileName = truncateFileName(file.getOriginalFilename());
        // 大小校验
        if (file.getSize() > (long) obsConfig.getAudioMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_AUDIO_TOO_BIG);
        }

        AudioVO vo = new AudioVO();
        String audioId = getMd5(file);
        long objectId = generateObjectId();
        MtsAudio mtsAudio = mtsAudioMapper.selectById(audioId);
        String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
        String source = "";
        String bucket = "";
        String fullPath = "";
        String url = "";

        if (mtsAudio != null) {
            MtsObject mtsObject = new MtsObject();
            mtsObject.setObjectId(objectId);
            mtsObject.setObjectType(ObjectType.AUDIO.value());
            mtsObject.setForeignId(mtsAudio.getAudioId());
            mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
            mtsObjectMapper.insert(mtsObject);

            url = (String)redisTemplate.opsForValue().get(redisKey);
            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(url)) {
                source = mtsAudio.getStoreSource();
                bucket = mtsAudio.getBucketName();
                fullPath = mtsAudio.getFullPath();
                url = ObsFactory.getObsService(source).getSignUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            vo.setObjectId(Long.toString(objectId));
            vo.setUrl(url);
            vo.setDuration(mtsAudio.getAudioDuration());
            vo.setFileName(mtsAudio.getFileName());
            vo.setSize(mtsAudio.getAudioSize());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

        ObsUploadRet obsUploadRet = obsService.uploadFile(file, generateRandomFileName(fileName), dto.getStoreType());
        if (obsUploadRet == null) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_ERROR);
        }

        bucket = obsUploadRet.getBucket();
        url = obsUploadRet.getUrl();
        fullPath = obsUploadRet.getFullPath();

        mtsAudio = new MtsAudio();
        mtsAudio.setAudioId(audioId);
        mtsAudio.setAudioType(file.getContentType());
        mtsAudio.setAudioSize(file.getSize());
        mtsAudio.setAudioDuration(dto.getDuration());
        mtsAudio.setFileName(fileName);
        mtsAudio.setStoreSource(obsConfig.getSource());
        mtsAudio.setBucketName(bucket);
        mtsAudio.setFullPath(fullPath);
        mtsAudio.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsAudio.setExpire(obsConfig.getTtl() * 86400L);
        mtsAudioMapper.insert(mtsAudio);

        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.AUDIO.value());
        mtsObject.setForeignId(mtsAudio.getAudioId());
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));

        vo.setObjectId(Long.toString(objectId));
        vo.setUrl(url);
        vo.setDuration(dto.getDuration());
        vo.setFileName(fileName);
        vo.setSize(file.getSize());
        vo.setCreatedTime(LocalDateTime.now());
        return ResultUtil.success(vo);
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> uploadVideo(UploadReq dto) {
        MultipartFile file = dto.getFile();
        String fileName = truncateFileName(file.getOriginalFilename());
        // 大小校验
        if (file.getSize() > (long) obsConfig.getVideoMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_VIDEO_TOO_BIG);
        }

        VideoVO vo = new VideoVO();
        String videoId = getMd5(file);
        long objectId = generateObjectId();
        MtsVideo mtsVideo = mtsVideoMapper.selectById(videoId);
        String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
        String source = "";
        String bucket = "";
        String fullPath = "";
        String url = "";

        if (mtsVideo != null) {
            MtsObject mtsObject = new MtsObject();
            mtsObject.setObjectId(objectId);
            mtsObject.setObjectType(ObjectType.VIDEO.value());
            mtsObject.setForeignId(mtsVideo.getVideoId());
            mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
            mtsObjectMapper.insert(mtsObject);

            url = (String)redisTemplate.opsForValue().get(redisKey);
            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(url)) {
                source = mtsVideo.getStoreSource();
                bucket = mtsVideo.getBucketName();
                fullPath = mtsVideo.getFullPath();
                url = ObsFactory.getObsService(source).getSignUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            vo.setObjectId(Long.toString(objectId));
            vo.setUrl(url);
            vo.setFileName(mtsVideo.getFileName());
            vo.setSize(mtsVideo.getVideoSize());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

        ObsUploadRet obsUploadRet = obsService.uploadFile(file, generateRandomFileName(fileName), dto.getStoreType());
        if (obsUploadRet == null) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_ERROR);
        }

        bucket = obsUploadRet.getBucket();
        url = obsUploadRet.getUrl();
        fullPath = obsUploadRet.getFullPath();

        mtsVideo = new MtsVideo();
        mtsVideo.setVideoId(videoId);
        mtsVideo.setVideoType(file.getContentType());
        mtsVideo.setVideoSize(file.getSize());
        mtsVideo.setFileName(fileName);
        mtsVideo.setStoreSource(obsConfig.getSource());
        mtsVideo.setBucketName(bucket);
        mtsVideo.setFullPath(fullPath);
        mtsVideo.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsVideo.setExpire(obsConfig.getTtl() * 86400L);
        mtsVideoMapper.insert(mtsVideo);

        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.VIDEO.value());
        mtsObject.setForeignId(mtsVideo.getVideoId());
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));

        vo.setObjectId(Long.toString(objectId));
        vo.setUrl(url);
        vo.setFileName(fileName);
        vo.setSize(file.getSize());
        vo.setCreatedTime(LocalDateTime.now());
        return ResultUtil.success(vo);
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> uploadImage(UploadReq dto) {
        MultipartFile file = dto.getFile();
        String fileName = truncateFileName(file.getOriginalFilename());
        // 大小校验
        if (file.getSize() > (long) obsConfig.getImageMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_IMAGE_TOO_BIG);
        }

        ImageVO vo = new ImageVO();
        String imageId = getMd5(file);
        long objectId = generateObjectId();
        MtsImage mtsImage = mtsImageMapper.selectById(imageId);
        String redisKey = RedisKey.MTS_IMAGE_URL + objectId;
        String source = "";
        String bucket = "";
        String originPath = "";
        String thumbPath = "";
        String originUrl = "";
        String thumbUrl = "";

        if (mtsImage != null) {
            MtsObject mtsObject = new MtsObject();
            mtsObject.setObjectId(objectId);
            mtsObject.setObjectType(ObjectType.IMAGE.value());
            mtsObject.setForeignId(mtsImage.getImageId());
            mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
            mtsObjectMapper.insert(mtsObject);

            String o = (String)redisTemplate.opsForValue().get(redisKey);
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
                source = mtsImage.getStoreSource();
                bucket = mtsImage.getBucketName();
                originPath = mtsImage.getOriginPath();
                thumbPath = mtsImage.getThumbPath();
                originUrl = ObsFactory.getObsService(source).getSignUrl(bucket, originPath);
                thumbUrl = ObsFactory.getObsService(source).getSignUrl(bucket, thumbPath);
                Map<String, String> map = new HashMap<>();
                map.put("originUrl", originUrl);
                map.put("thumbUrl", thumbUrl);
                redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(map), Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            vo.setObjectId(Long.toString(objectId));
            vo.setOriginUrl(originUrl);
            vo.setThumbUrl(thumbUrl);
            vo.setFileName(mtsImage.getFileName());
            vo.setSize(mtsImage.getImageSize());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

        ObsUploadRet obsUploadRet = obsService.uploadFile(file, generateRandomFileName(fileName), dto.getStoreType());
        if (obsUploadRet == null) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_ERROR);
        }

        bucket = obsUploadRet.getBucket();
        originUrl = obsUploadRet.getUrl();
        originPath = obsUploadRet.getFullPath();
        thumbUrl = originUrl;
        thumbPath = originPath;
        if (file.getSize() > obsConfig.getImageThumbSize()) {
            try {
                byte[] imageThumb = getImageThumb(file.getBytes());
                ObsUploadRet ret = obsService.uploadFile(imageThumb, file.getContentType(), generateRandomFileName(fileName), dto.getStoreType());
                if (ret != null) {
                    thumbUrl = ret.getUrl();
                    thumbPath = ret.getFullPath();
                }
            } catch (IOException e) {
                log.error("file.getBytes() error, exception is {}", e);
            }
        }

        mtsImage = new MtsImage();
        mtsImage.setImageId(imageId);
        mtsImage.setImageType(file.getContentType());
        mtsImage.setImageSize(file.getSize());
        mtsImage.setFileName(fileName);
        mtsImage.setStoreSource(obsConfig.getSource());
        mtsImage.setBucketName(bucket);
        mtsImage.setOriginPath(originPath);
        mtsImage.setThumbPath(thumbPath);
        mtsImage.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsImage.setExpire(obsConfig.getTtl() * 86400L);
        mtsImageMapper.insert(mtsImage);

        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.IMAGE.value());
        mtsObject.setForeignId(mtsImage.getImageId());
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        Map<String, String> map = new HashMap<>();
        map.put("originUrl", originUrl);
        map.put("thumbUrl", thumbUrl);
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(map), Duration.ofSeconds(obsConfig.getUrlExpire()));

        vo.setObjectId(Long.toString(objectId));
        vo.setOriginUrl(originUrl);
        vo.setThumbUrl(thumbUrl);
        vo.setFileName(fileName);
        vo.setSize(file.getSize());
        vo.setCreatedTime(LocalDateTime.now());
        return ResultUtil.success(vo);
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> uploadDocument(UploadReq dto) {
        MultipartFile file = dto.getFile();
        String fileName = truncateFileName(file.getOriginalFilename());
        // 大小校验
        if (file.getSize() > (long) obsConfig.getDocumentMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_VIDEO_TOO_BIG);
        }

        DocumentVO vo = new DocumentVO();
        String documentId = getMd5(file);
        long objectId = generateObjectId();
        MtsDocument mtsDocument = mtsDocumentMapper.selectById(documentId);
        String redisKey = RedisKey.MTS_OBJECT_URL + objectId;
        String source = "";
        String bucket = "";
        String fullPath = "";
        String url = "";

        if (mtsDocument != null) {
            MtsObject mtsObject = new MtsObject();
            mtsObject.setObjectId(objectId);
            mtsObject.setObjectType(ObjectType.DOCUMENT.value());
            mtsObject.setForeignId(mtsDocument.getDocumentId());
            mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
            mtsObjectMapper.insert(mtsObject);

            url = (String)redisTemplate.opsForValue().get(redisKey);
            // 如果redis中不存在url，则需要生成签名url
            if (!StringUtils.hasLength(url)) {
                source = mtsDocument.getStoreSource();
                bucket = mtsDocument.getBucketName();
                fullPath = mtsDocument.getFullPath();
                url = ObsFactory.getObsService(source).getSignUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            vo.setObjectId(Long.toString(objectId));
            vo.setDocumentType(mtsDocument.getDocumentType());
            vo.setUrl(url);
            vo.setFileName(mtsDocument.getFileName());
            vo.setSize(mtsDocument.getDocumentSize());
            vo.setCreatedTime(LocalDateTime.now());
            return ResultUtil.success(vo);
        }

        ObsUploadRet obsUploadRet = obsService.uploadFile(file, generateRandomFileName(fileName), dto.getStoreType());
        if (obsUploadRet == null) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_ERROR);
        }

        bucket = obsUploadRet.getBucket();
        url = obsUploadRet.getUrl();
        fullPath = obsUploadRet.getFullPath();

        mtsDocument = new MtsDocument();
        mtsDocument.setDocumentId(documentId);
        mtsDocument.setDocumentType(file.getContentType());
        mtsDocument.setDocumentSize(file.getSize());
        mtsDocument.setFileName(fileName);
        mtsDocument.setStoreSource(obsConfig.getSource());
        mtsDocument.setBucketName(bucket);
        mtsDocument.setFullPath(fullPath);
        mtsDocument.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsDocument.setExpire(obsConfig.getTtl() * 86400L);
        mtsDocumentMapper.insert(mtsDocument);

        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(ObjectType.DOCUMENT.value());
        mtsObject.setForeignId(documentId);
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));

        vo.setObjectId(Long.toString(objectId));
        vo.setDocumentType(mtsDocument.getDocumentType());
        vo.setUrl(url);
        vo.setFileName(fileName);
        vo.setSize(file.getSize());
        vo.setCreatedTime(LocalDateTime.now());
        return ResultUtil.success(vo);
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
            String objectId = item.get("objectId").toString();
            String fileName = item.get("fileName").toString();
            long size = (long) item.get("size");
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
                originUrl = ObsFactory.getObsService(source).getSignUrl(bucket, originPath);
                thumbUrl = ObsFactory.getObsService(source).getSignUrl(bucket, thumbPath);
                Map<String, String> map = new HashMap<>();
                map.put("originUrl", originUrl);
                map.put("thumbUrl", thumbUrl);
                redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(map), Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            ImageVO vo = new ImageVO();
            vo.setObjectId(objectId);
            vo.setOriginUrl(originUrl);
            vo.setThumbUrl(thumbUrl);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    public List<AudioVO> getAudioVOS(List<Long> objectIds) {
        List<Map<String, Object>> list = mtsObjectMapper.batchSelectAudio(objectIds);
        List<AudioVO> voList = list.stream().map((item) -> {
            String objectId = item.get("objectId").toString();
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
                url = ObsFactory.getObsService(source).getSignUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            AudioVO vo = new AudioVO();
            vo.setObjectId(objectId);
            vo.setUrl(url);
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
            String objectId = item.get("objectId").toString();
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
                url = ObsFactory.getObsService(source).getSignUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            VideoVO vo = new VideoVO();
            vo.setObjectId(objectId);
            vo.setUrl(url);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    public List<DocumentVO> getDocumentVOS(List<Long> objectIds) {
        List<Map<String, Object>> list = mtsObjectMapper.batchSelectDocument(objectIds);
        List<DocumentVO> voList = list.stream().map((item) -> {
            String objectId = item.get("objectId").toString();
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
                url = ObsFactory.getObsService(source).getSignUrl(bucket, fullPath);
                redisTemplate.opsForValue().set(redisKey, url, Duration.ofSeconds(obsConfig.getUrlExpire()));
            }

            DocumentVO vo = new DocumentVO();
            vo.setObjectId(objectId);
            vo.setDocumentType(documentType);
            vo.setUrl(url);
            vo.setFileName(fileName);
            vo.setSize(size);
            vo.setCreatedTime(createdTime);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    /**
     * 生成图片缩略图
     *
     * @param oriImageBytes 原图byte[]
     * @return 缩略图byte[]
     */
    private byte[] getImageThumb(byte[] oriImageBytes) {
        int srcSize = oriImageBytes.length;
        byte[] destImageBytes = oriImageBytes;
        try {
            while (destImageBytes.length > obsConfig.getImageThumbSize()) {
                double accuracy = getAccuracy(destImageBytes.length);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(destImageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(destImageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                destImageBytes = outputStream.toByteArray();
            }
            log.info("图片原大小={}kb | 压缩后大小={}kb", srcSize / 1024, destImageBytes.length / 1024);
        } catch (Exception e) {
            log.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return destImageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private double getAccuracy(int size) {
        double accuracy;
        if (size < 900 * 1024) {
            accuracy = 0.85;
        } else if (size < 2047 * 1024) {
            accuracy = 0.6;
        } else if (size < 3275 * 1024) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * 获取上传文件的md5
     * @param file
     * @return
     * @throws IOException
     */
    public String getMd5(MultipartFile file) {
        try {
            //获取文件的byte信息
            byte[] uploadBytes = file.getBytes();
            // 拿到一个MD5转换器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            //转换为16进制
            return new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
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
