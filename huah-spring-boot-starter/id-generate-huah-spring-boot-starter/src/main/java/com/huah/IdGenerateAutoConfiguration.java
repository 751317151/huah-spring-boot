package com.huah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huah 2023/12/18 20:00
 */
@EnableConfigurationProperties(IdProperties.class)
@Configuration
public class IdGenerateAutoConfiguration {

    static {
        System.out.println("自动配置类 IdGenerateAutoConfiguration 生效了.......");
    }

    @Autowired
    private IdProperties properties;

    @Bean
    public IdGenerateService idGenerateService() {
        return new IdGenerateService(properties.getWorkId());
    }

//    @Bean
//    public IdProperties idProperties() {
//        return new IdProperties();
//    }
}
