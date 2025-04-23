package com.huah.kafka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.huah.kafka"})  // ComponentScan注解需要KafkaProducer加上@service注解，与@Bean注解分开使用
public class KafkaAutoConfiguration {

//    @Bean  // 不用ComponentScan注解，也不需要KafkaProducer加上@service注解
//    public KafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
//        return new KafkaProducer(kafkaTemplate);
//    }
}
