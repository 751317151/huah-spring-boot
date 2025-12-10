package com.huah.huahspringbootshortlink.config;

import com.huah.huahspringbootshortlink.entity.ShortLinkEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, ShortLinkEvent> shortLinkProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(null),
                new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, ShortLinkEvent> shortLinkKafkaTemplate() {
        return new KafkaTemplate<>(shortLinkProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, ShortLinkEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(null),
            new StringDeserializer(), new JsonDeserializer<>(ShortLinkEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShortLinkEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ShortLinkEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}