package com.beizeng.admin.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liren.basic.common.exception.RRException;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: <h1>ElasticConfig </h1>
 **/
@Data
@Component
@ConfigurationProperties(prefix = "liren.es")
public class ElasticConfig {

    private String host;
    private int port;
    private String scheme;
    private int connTimeout;
    private int socketTimeout;
    private int connectionRequestTimeout;

    @Bean
    public RestClientBuilder restClientBuilder() {
        return RestClient.builder(makeHttpHost());
    }

    @Bean
    public RestClient elasticsearchRestClient() {
        return RestClient.builder(new HttpHost(host, port, scheme))
                .setRequestConfigCallback(restClientBuilder -> restClientBuilder
                        .setConnectTimeout(connTimeout)
                        .setSocketTimeout(socketTimeout)
                        .setConnectionRequestTimeout(connectionRequestTimeout))
                .build();
    }

    private HttpHost makeHttpHost() {
        return new HttpHost(host, port, scheme);
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

    public String JsonTest(String path, String key) throws IOException {
        InputStream stream = this.getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new RRException("读取文件失败");
        } else {
            JSONObject json = JSON.parseObject(stream, JSONObject.class);
            String val = json.getString(key);
            return val;
        }
    }

}
