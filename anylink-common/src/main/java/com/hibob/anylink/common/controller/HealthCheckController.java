package com.hibob.anylink.common.controller;

import com.hibob.anylink.common.model.IMHttpResponse;
import com.hibob.anylink.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "健康检查公共类")
@RestController
@RequestMapping("/api/${spring.application.name}")
public class HealthCheckController {

    /**
     * 健康检查接口
     * @return
     */
    @ApiOperation(value = "健康检查接口", notes = "健康检查接口")
    @GetMapping("/healthcheck")
    public ResponseEntity<IMHttpResponse> healthcheck() {
        return ResultUtil.success();
    }

}
