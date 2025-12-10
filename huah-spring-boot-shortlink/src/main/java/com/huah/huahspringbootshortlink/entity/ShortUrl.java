package com.huah.huahspringbootshortlink.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ShortUrl {
    private Long id;
    private String code;
    private String originUrl;
    private Date expireAt;
    private Date createdAt;
}