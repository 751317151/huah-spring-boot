package com.huah.huahspringbootshortlink.controller;

import com.huah.huahspringbootshortlink.entity.CreateRequest;
import com.huah.huahspringbootshortlink.entity.ShortLinkEvent;
import com.huah.huahspringbootshortlink.service.CacheService;
import com.huah.huahspringbootshortlink.service.ShortenService;
import com.huah.huahspringbootshortlink.utils.SnowflakeIdGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@Tag(name = "短链管理", description = "短链相关的接口")
public class ShortenController {

    @Autowired
    private ShortenService shortenService;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/shorten")
    @Operation(summary = "短链生成接口", description = "返回短链")
    @ApiResponses
    public ResponseEntity<String> createShortUrl(@RequestBody CreateRequest req) {
        // 1. 校验 URL
//        if (!isValidUrl(req.getOriginUrl())) {
//            return ResponseEntity.badRequest().body("Invalid URL");
//        }

        // 2. 生成短链 code（基于 Snowflake）
        String code = shortenService.generateShortCode();
        String originUrl = req.getOriginUrl();
        originUrl = "https://www.baidu.com/" + code;

        // 3. 构造事件
        ShortLinkEvent event = ShortLinkEvent.builder()
            .id(SnowflakeIdGenerator.nextId())
            .code(code)
            .originUrl(originUrl)
            .expireAt(req.getExpireAt())
            .build();

        // 4. 【关键】写 Redis + 发 Kafka（异步持久化）
        shortenService.publishShortLink(event);
        System.out.println("处理完结果啦---" + code);

        // 5. 立即返回（<10ms）
        return ResponseEntity.ok("https://t.cn/" + code);
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable(name = "code") String code,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        // 1. 从三级缓存获取原始 URL
        String originUrl = cacheService.getOriginUrl(code);

        if (originUrl == null) {
            response.sendError(404, "Short link not found");
            return;
        }

        // 2. 异步记录点击（可选）
        kafkaTemplate.send("shortlink-click", code, getClientIP(request));

        // 3. 302 跳转
        response.sendRedirect(originUrl);
    }
    
    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}