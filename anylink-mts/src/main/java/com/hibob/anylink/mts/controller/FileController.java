package com.hibob.anylink.mts.controller;

import com.hibob.anylink.common.annotation.ApiCommonHeader;
import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.mts.dto.request.ImageReq;
import com.hibob.anylink.mts.dto.request.UploadReq;
import com.hibob.anylink.mts.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "文件操作的接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mts")
public class FileController {

    private final FileService fileService;

    /**
     * 上传文件
     * 注意：由于请求体参数要和接收文件的参数类型MultipartFile兼容，这里要把MultipartFile参数放在Req属性中，并且要取消@RequestBody注解
     * @param dto
     * @return
     */
    @ApiOperation(value = "上传文件", notes = "上传文件：头像，自定义表情，文件")
    @PostMapping("/upload")
    public ResponseEntity<IMHttpResponse> upload(@Valid UploadReq dto) {
        return fileService.upload(dto);
    }

    /**
     * 根据objectId查询image的url
     */
    @ApiOperation(value = "根据objectId查询image的url", notes = "根据objectId查询image的url")
    @ApiCommonHeader
    @GetMapping("/image")
    public ResponseEntity<IMHttpResponse> image(@Validated ImageReq dto) {
        return fileService.image(dto);
    }

}
