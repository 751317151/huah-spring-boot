package com.huah.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "hhtopic";  // Kafka 主题名称

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 发送消息到 Kafka
    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Produced message: " + message);
    }
}