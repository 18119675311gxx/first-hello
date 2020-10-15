package com.beizeng.admin.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: <h1>TokenConfig Token配置类</h1>
 * @author:
 **/
@Data
@Component
@ConfigurationProperties(prefix = "liren.token")
public class TokenConfig {

    private String secret;
    private Integer expire;
    private String header;

}
