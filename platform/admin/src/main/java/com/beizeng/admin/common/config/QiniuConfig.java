package com.beizeng.admin.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: <h1>QiniuConfig 七牛配置类</h1>
 **/
@Data
@Component
@ConfigurationProperties(prefix = "liren.qiniu")
public class QiniuConfig {

    private String accessKey;
    private String secretKey;
    private String baseUrl;         //  外链默认的域名
    private String bucket;          //  要上传的空间名称

}