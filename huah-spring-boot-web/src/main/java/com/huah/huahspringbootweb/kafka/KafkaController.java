package com.huah.huahspringbootweb.kafka;

import com.huah.kafka.KafkaProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    // 发送消息的 HTTP 接口
    @PostMapping("/send")
    public String send(@RequestParam String message) {
        kafkaProducer.sendMessage(message);
        return "Message sent: " + message;
    }
}