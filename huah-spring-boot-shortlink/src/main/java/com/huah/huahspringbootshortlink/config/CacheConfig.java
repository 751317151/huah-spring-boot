package com.huah.huahspringbootshortlink.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    /**
     * 短链本地缓存（L1 Cache）
     * - 最大容量：100,000 个 key（根据内存调整）
     * - 写后 10 分钟过期（与 Redis TTL 对齐）
     * - 自动收集指标（用于监控命中率）
     */
    @Bean
    public Cache<String, String> shortLinkLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(100_000)                          // 最多缓存 10 万个短链
                .expireAfterWrite(10, TimeUnit.MINUTES)       // 写入后 10 分钟过期
                .recordStats()                                // 启用统计（命中率等）
                .build();
    }
}