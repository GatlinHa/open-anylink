package com.hibob.anylink.mts.service;

import com.hibob.anylink.common.enums.ServiceErrorCode;
import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.common.session.ReqSession;
import com.hibob.anylink.common.utils.ResultUtil;
import com.hibob.anylink.common.utils.SnowflakeId;
import com.hibob.anylink.mts.dto.request.AudioReq;
import com.hibob.anylink.mts.dto.request.ImageReq;
import com.hibob.anylink.mts.dto.request.UploadReq;
import com.hibob.anylink.mts.dto.vo.AudioVO;
import com.hibob.anylink.mts.dto.vo.ImageVO;
import com.hibob.anylink.mts.entity.MtsAudio;
import com.hibob.anylink.mts.entity.MtsImage;
import com.hibob.anylink.mts.entity.MtsObject;
import com.hibob.anylink.mts.enums.FileType;
import com.hibob.anylink.mts.mapper.MtsAudioMapper;
import com.hibob.anylink.mts.mapper.MtsImageMapper;
import com.hibob.anylink.mts.mapper.MtsObjectMapper;
import com.hibob.anylink.mts.obs.ObsConfig;
import com.hibob.anylink.mts.obs.ObsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final ObsConfig obsConfig;
    private final ObsService obsService;
    private final MtsObjectMapper mtsObjectMapper;
    private final MtsImageMapper mtsImageMapper;
    private final MtsAudioMapper mtsAudioMapper;
    private SnowflakeId snowflakeId = null;

    public ResponseEntity<IMHttpResponse> upload(UploadReq dto) {
        log.info("FileService::upload");
        FileType fileType = FileType.determineFileType(dto.getFile().getOriginalFilename());
        switch (fileType) {
            case IMAGE:
                return uploadImage(dto);
            case DOCUMENT:
//                return uploadFile(dto);
                break;
            case AUDIO:
                return uploadAudio(dto);
            default:
                break;
        }

        return ResultUtil.success();
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> uploadAudio(UploadReq dto) {
        MultipartFile file = dto.getFile();
        String fileName = truncateFileName(file.getOriginalFilename());
        // 大小校验
        if (file.getSize() > obsConfig.getAudioMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_AUDIO_TOO_BIG);
        }

        // 文件后缀校验
        if (!FileType.isAudioFile(fileName)) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_AUDIO_FORMAT_ERROR);
        }

        AudioVO vo = new AudioVO();
        String audioId = getMd5(file);
        long objectId = generateObjectId();
        MtsAudio mtsAudio = mtsAudioMapper.selectById(audioId);
        if (mtsAudio != null) {
            MtsObject mtsObject = new MtsObject();
            mtsObject.setObjectId(objectId);
            mtsObject.setObjectType(1);
            mtsObject.setForeignId(mtsAudio.getAudioId());
            mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
            mtsObjectMapper.insert(mtsObject);

            vo.setObjectId(Long.toString(objectId));
            vo.setUrl(mtsAudio.getUrl());
            return ResultUtil.success(vo);
        }

        String url = obsService.uploadFile(file, fileName, dto.getStoreType());
        if (!StringUtils.hasLength(url)) {
            return ResultUtil.error(ServiceErrorCode.ERROR_MTS_FILE_UPLOAD_ERROR);
        }

        mtsAudio = new MtsAudio();
        mtsAudio.setAudioId(audioId);
        mtsAudio.setAudioType(file.getContentType());
        mtsAudio.setAudioSize(file.getSize());
        mtsAudio.setUrl(url);
        mtsAudio.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsAudio.setExpire(obsConfig.getTtl() * 86400L);
        mtsAudioMapper.insert(mtsAudio);

        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(1);
        mtsObject.setForeignId(mtsAudio.getAudioId());
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        vo.setObjectId(Long.toString(objectId));
        vo.setUrl(url);
        return ResultUtil.success(vo);
    }

    @Transactional
    public ResponseEntity<IMHttpResponse> uploadImage(UploadReq dto) {
        MultipartFile file = dto.getFile();
        String fileName = truncateFileName(file.getOriginalFilename());
        // 大小校验
        if (file.getSize() > obsConfig.getImageMaxLimit() * 1024 * 1024) {
            return ResultUtil.error(ServiceErrorCode.ERROR_IMAGE_TOO_BIG);
        }

        // 文件后缀校验
        if (!FileType.isImageFile(fileName)) {
            return ResultUtil.error(ServiceErrorCode.ERROR_IMAGE_FORMAT_ERROR);
        }


        ImageVO vo = new ImageVO();
        String imageId = getMd5(file);
        long objectId = generateObjectId();
        MtsImage mtsImage = mtsImageMapper.selectById(imageId);
        if (mtsImage != null) {
            MtsObject mtsObject = new MtsObject();
            mtsObject.setObjectId(objectId);
            mtsObject.setObjectType(0);
            mtsObject.setForeignId(mtsImage.getImageId());
            mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
            mtsObjectMapper.insert(mtsObject);

            vo.setObjectId(Long.toString(objectId));
            vo.setOriginUrl(mtsImage.getOriginUrl());
            vo.setThumbUrl(mtsImage.getThumbUrl());
            return ResultUtil.success(vo);
        }

        String originUrl = obsService.uploadFile(file, fileName, dto.getStoreType());
        if (!StringUtils.hasLength(originUrl)) {
            return ResultUtil.error(ServiceErrorCode.ERROR_FILE_UPLOAD_ERROR);
        }

        String thumbUrl = originUrl;
        if (file.getSize() > obsConfig.getImageThumbSize()) {
            try {
                byte[] imageThumb = getImageThumb(file.getBytes());
                int dotIndex = fileName.lastIndexOf('.'); // 文件名在前面已经校验过了，这里肯定合法
                fileName = fileName.substring(0, dotIndex) + "-thumb" + fileName.substring(dotIndex);
                thumbUrl = obsService.uploadFile(imageThumb, file.getContentType(), fileName, dto.getStoreType());
            } catch (IOException e) {
                log.error("file.getBytes() error, exception is {}", e);
            }
        }

        mtsImage = new MtsImage();
        mtsImage.setImageId(imageId);
        mtsImage.setImageType(file.getContentType());
        mtsImage.setImageSize(file.getSize());
        mtsImage.setOriginUrl(originUrl);
        mtsImage.setThumbUrl(thumbUrl);
        mtsImage.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsImage.setExpire(obsConfig.getTtl() * 86400L);
        mtsImageMapper.insert(mtsImage);

        MtsObject mtsObject = new MtsObject();
        mtsObject.setObjectId(objectId);
        mtsObject.setObjectType(0);
        mtsObject.setForeignId(mtsImage.getImageId());
        mtsObject.setCreatedAccount(ReqSession.getSession().getAccount());
        mtsObjectMapper.insert(mtsObject);

        vo.setObjectId(Long.toString(objectId));
        vo.setOriginUrl(originUrl);
        vo.setThumbUrl(thumbUrl);
        return ResultUtil.success(vo);
    }

    public ResponseEntity<IMHttpResponse> image(ImageReq dto) {
        List<ImageVO> voList = mtsObjectMapper.batchSelectImage(dto.getObjectIds());
        return ResultUtil.success(voList);
    }

    public ResponseEntity<IMHttpResponse> audio(AudioReq dto) {
        List<AudioVO> voList = mtsObjectMapper.selectAudio(dto.getObjectId());
        if (voList.size() > 0) {
            return ResultUtil.success(voList.get(0));
        } else  {
            return ResultUtil.success(new AudioVO());
        }
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
     * 截取文件名，最大保留64位
     * @param fileName
     * @return
     */
    private String truncateFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int truncateLength = 64;
        // 查找最后一个点的位置
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // 如果没有扩展名，直接截取前50位
            return fileName.length() <= 50 ? fileName : fileName.substring(0, truncateLength);
        }
        // 分离文件名主体和扩展名
        String nameWithoutExtension = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex);
        // 截取文件名主体部分
        String truncatedName = nameWithoutExtension.length() <= 50 ? nameWithoutExtension : nameWithoutExtension.substring(0, truncateLength);
        // 重新组合文件名
        return truncatedName + extension;
    }
}
