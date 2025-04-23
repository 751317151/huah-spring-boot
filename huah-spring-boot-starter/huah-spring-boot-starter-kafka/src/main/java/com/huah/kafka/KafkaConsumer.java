package com.huah.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    // 监听指定主题的消息
    @KafkaListener(topics = "hhtopic", groupId = "hhgroup")
    public void receiveMessage(
            String message,
            @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment) {  // 手动提交
        try {
            System.out.println("Consumed message: " + message);
            acknowledgment.acknowledge();  // 提交 Offset
        } catch (Exception e) {
            // 处理异常（可选：将消息存入死信队列）
        }
    }
}