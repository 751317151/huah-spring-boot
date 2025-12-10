package com.huah.huahspringbootshortlink.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "短链生成请求")
public class CreateRequest {

    @Schema(description = "原始 URL")
    private String originUrl;

    @Schema(description = "过期时间")
    private Date expireAt;
}