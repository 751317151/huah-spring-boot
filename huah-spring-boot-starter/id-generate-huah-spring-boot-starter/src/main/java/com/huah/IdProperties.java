package com.huah;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huah 2023/12/18 19:44
 */
@ConfigurationProperties(prefix = IdProperties.PREFIX)
@Data
public class IdProperties {
    public static final String PREFIX = "huah";
    private Long workId;
}
