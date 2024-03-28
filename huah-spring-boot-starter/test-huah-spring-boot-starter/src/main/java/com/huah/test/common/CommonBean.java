package com.huah.test.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author huah 2023/12/18 14:16
 */
@EnableConfigurationProperties(CommonBean.class)
@ConfigurationProperties(prefix = "commonbean")
@Data
public class CommonBean {
    private int userId;
    private String userName;
}
