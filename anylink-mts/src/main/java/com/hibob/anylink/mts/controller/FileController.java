package com.hibob.anylink.mts.controller;

import com.hibob.anylink.common.annotation.ApiCommonHeader;
import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.mts.dto.request.*;
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
     * 预签名方式上传文件，这个请求用于获取预签名URL
     * @param dto
     * @return
     */
    @ApiOperation(value = "获取上传文件的预签名URL", notes = "获取上传文件的预签名URL")
    @PostMapping("/getUploadUrl")
    public ResponseEntity<IMHttpResponse> getUploadUrl(@Valid GetUploadUrlReq dto) {
        return fileService.getUploadUrl(dto);
    }

    /**
     * 客户端上报上传结果
     * @param dto
     * @return
     */
    @ApiOperation(value = "客户端上报上传结果", notes = "客户端上报上传结果")
    @PostMapping("/reportUploaded")
    public ResponseEntity<IMHttpResponse> reportUploaded(@Valid ReportUploaded dto) {
        return fileService.reportUploaded(dto);
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

    /**
     * 根据objectId查询audio的url
     */
    @ApiOperation(value = "根据objectId查询audio的url", notes = "根据objectId查询audio的url")
    @ApiCommonHeader
    @GetMapping("/audio")
    public ResponseEntity<IMHttpResponse> audio(@Validated AudioReq dto) {
        return fileService.audio(dto);
    }

    /**
     * 根据objectId查询video的url
     */
    @ApiOperation(value = "根据objectId查询video的url", notes = "根据objectId查询video的url")
    @ApiCommonHeader
    @GetMapping("/video")
    public ResponseEntity<IMHttpResponse> video(@Validated VideoReq dto) {
        return fileService.video(dto);
    }

    /**
     * 根据objectId查询document的url
     */
    @ApiOperation(value = "根据objectId查询document的url", notes = "根据objectId查询document的url")
    @ApiCommonHeader
    @GetMapping("/document")
    public ResponseEntity<IMHttpResponse> document(@Validated DocumentReq dto) {
        return fileService.document(dto);
    }
}
