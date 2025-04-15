package com.hibob.anylink.mts.obs;

import org.springframework.web.multipart.MultipartFile;

public interface ObsService {
    /**
     * 获取预签名的下载URL
     * @param bucketName 桶名，上传的时候确定的
     * @param ObjectName 对象全路径名
     * @return
     */
    String getDownloadUrl(String bucketName, String ObjectName);

    /**
     * 获取预签名的上传URL
     * 场景1：首次上传这个文件
     * @param contentType 内容类型
     * @param randomFileName 随机文件名（上传对象服务的文件名字要被匿名化和标准化）
     * @param storeType 是永久存储还是临时存储，两种方式存储的桶不一样
     * @return
     */
    ObsUploadRet getUploadUrl(String contentType, String randomFileName, int storeType);

    /**
     * 获取预签名的上传URL
     * 场景2：非首次上传这个文件，在之前的请求中已经确定了桶名和全路径名
     * @param bucketName 桶名，上传的时候确定的
     * @param ObjectName 全路径名
     * @return
     */
    ObsUploadRet getUploadUrl(String bucketName, String ObjectName);
}
