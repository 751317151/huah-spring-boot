package com.huah.huahspringbootshortlink.service;

import com.huah.huahspringbootshortlink.utils.Base62;
import com.huah.huahspringbootshortlink.utils.SnowflakeIdGenerator;
import com.huah.huahspringbootshortlink.entity.ShortLinkEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ShortenService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private KafkaTemplate<String, ShortLinkEvent> kafkaTemplate;

    private static final String SHORT_KEY_PREFIX = "short:";

    public void publishShortLink(ShortLinkEvent event) {
        String key = SHORT_KEY_PREFIX + event.getCode();
        String value = event.getOriginUrl();

        // 1. 写 Redis（TTL=7天）
        long ttl = calculateTtl(event.getExpireAt()); // 单位：秒
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);

        // 2. 发送 Kafka 消息（异步落库）
        kafkaTemplate.send("shortlink-create", event.getCode(), event);
    }

    private long calculateTtl(Date expireAt) {
        if (expireAt == null) {
            return 7 * 24 * 3600; // 7天
        }
        long diff = expireAt.getTime() - System.currentTimeMillis();
        return Math.max(60, diff / 1000); // 至少60秒
    }

    public String generateShortCode() {
        long id = SnowflakeIdGenerator.nextId();
        return Base62.encode(id); // 62进制编码
    }
}