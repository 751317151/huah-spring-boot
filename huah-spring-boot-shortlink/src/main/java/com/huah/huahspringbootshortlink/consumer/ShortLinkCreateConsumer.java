package com.huah.huahspringbootshortlink.consumer;

import com.huah.huahspringbootshortlink.dao.ShortUrlMapper;
import com.huah.huahspringbootshortlink.entity.ShortLinkEvent;
import com.huah.huahspringbootshortlink.entity.ShortUrl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShortLinkCreateConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(ShortLinkCreateConsumer.class);

    @Autowired
    private ShortUrlMapper shortUrlMapper;

    @KafkaListener(topics = "shortlink-create", groupId = "shortlink-persist")
    public void consume(ConsumerRecord<String, ShortLinkEvent> record, Acknowledgment ack) {
        log.info("Consume: {}", record);
        ShortLinkEvent event = record.value();

        try {
            // 转为 DB Entity
            ShortUrl entity = new ShortUrl();
            entity.setId(event.getId());
            entity.setCode(event.getCode());
            entity.setOriginUrl(event.getOriginUrl());
            entity.setExpireAt(event.getExpireAt());
            entity.setCreatedAt(new Date());

            // 插入 MySQL（MyBatis 自动路由到分库分表）
            shortUrlMapper.insert(entity);
            ack.acknowledge();
        } catch (DuplicateKeyException e) {
            // 幂等：唯一索引冲突，忽略
            log.warn("Duplicate code: {}: {}", event.getCode(), event.getId());
            ack.acknowledge();
        } catch (Exception e) {
            // 重试 or 死信队列（简化）
            throw new RuntimeException("Persist failed, will retry", e);
        }
    }
}