package com.huah.huahspringbootshortlink.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.huah.huahspringbootshortlink.dao.ShortUrlMapper;
import com.huah.huahspringbootshortlink.entity.ShortUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private Cache<String, String> localCache;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ShortUrlMapper shortUrlMapper;

    private static final String SHORT_KEY_PREFIX = "short:";

    public String getOriginUrl(String code) {
        // L2: 本地缓存
        String url = localCache.getIfPresent(code);
        if (url != null) {
            log.info("命中本地缓存~~~");
            return url;
        }

        // L3: Redis
        url = redisTemplate.opsForValue().get(SHORT_KEY_PREFIX + code);
        if (url != null) {
            log.info("命中Redis缓存~~~");
            localCache.put(code, url); // 回种本地缓存
            return url;
        }

        // L4: MySQL（兜底，防缓存穿透）
        ShortUrl dbUrl = shortUrlMapper.selectByCode(code);
        if (dbUrl != null && !isExpired(dbUrl)) {
            // 回种 Redis + 本地
            long ttl = calculateTtl(dbUrl.getExpireAt());
            redisTemplate.opsForValue().set(SHORT_KEY_PREFIX + code, dbUrl.getOriginUrl(), ttl, TimeUnit.SECONDS);
            localCache.put(code, dbUrl.getOriginUrl());
            return dbUrl.getOriginUrl();
        }

        // 防缓存穿透：写空值（TTL=60s）
        redisTemplate.opsForValue().set(SHORT_KEY_PREFIX + code, "", 60, TimeUnit.SECONDS);
        return null;
    }

    private boolean isExpired(ShortUrl url) {
        return url.getExpireAt() != null && url.getExpireAt().before(new Date());
    }
    
    private long calculateTtl(Date expireAt) {
        if (expireAt == null) {
            return 7 * 24 * 3600; // 7天
        }
        long diff = expireAt.getTime() - System.currentTimeMillis();
        return Math.max(60, diff / 1000); // 至少60秒
    }
}