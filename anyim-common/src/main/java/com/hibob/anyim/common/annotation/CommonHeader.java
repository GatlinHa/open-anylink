package com.hibob.anyim.common.annotation;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "traceId", value = "日志追踪Id", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "timestamp", value = "时间戳", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "sign", value = "请求签名", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, paramType = "header", dataType = "String")
})
public @interface CommonHeader {
}
