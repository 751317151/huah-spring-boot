package com.huah.test.config;

import com.huah.test.common.CommonBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huah 2023/12/18 14:40
 */
@Configuration
@ConditionalOnBean(ConfigMarker.class)
public class TestAutoConfiguration {
    static {
        System.out.println("自动配置类生效了.......");
    }

    @Bean
    public CommonBean simpleBean() {
        return new CommonBean();
    }
}
