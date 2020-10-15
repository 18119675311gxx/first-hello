package com.beizeng.admin.common.config;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.beizeng.admin.common.utils.rocket.RocketMessageListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @description: <h1>MqConfig RocketMQ配置类</h1>
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "aliyun.rocketmq")
public class MqConfig {

    private boolean open;
    private String accessKey;
    private String secretKey;
    private String nameSrvAddr;

    private String topic;
    private String groupId;
    private String ConsumeThreadNums;

    private String tag = "*";

    private RocketMessageListener messageListener;


    public MqConfig(RocketMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public Properties getMqPropertie() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.groupId);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        properties.setProperty("Topic", this.topic);
        properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, "20");
        return properties;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(this.getMqPropertie());
        return producer;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = this.getMqPropertie();
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, this.getConsumeThreadNums());
        consumerBean.setProperties(properties);
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<Subscription, MessageListener>();
        Subscription subscription = new Subscription();
        subscription.setTopic(this.getTopic());
        subscription.setExpression(this.getTag());
        subscriptionTable.put(subscription, messageListener);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

}
