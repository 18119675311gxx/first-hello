package com.beizeng.admin.common.utils.rocket;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.beizeng.admin.common.config.MqConfig;
import org.springframework.stereotype.Component;

/**
 * @description: <h1>RocketMessageProducer rocketMQ消息生产者</h1>
 **/
@Component
public class RocketMessageProducer {

    private static ProducerBean producer;

    private static MqConfig mqConfig;

    public RocketMessageProducer(ProducerBean producer, MqConfig mqConfig) {
        this.producer = producer;
        this.mqConfig = mqConfig;
    }

    /**
     * @Description: <h2>生产 延时/定时 消息</h2>
     */
    public static void producerDelay(String tag, long delayTime, String key, String body) {
        Message msg = new Message(mqConfig.getTopic(), tag, key, body.getBytes());
        msg.setStartDeliverTime(delayTime);
        long time = System.currentTimeMillis();
        try {
            SendResult sendResult = producer.send(msg);
            if (sendResult != null) {
                System.out.println(time
                        + " Send mq message success.Topic is：" + msg.getTopic()
                        + " Tag is：" + msg.getTag() + " Key is：" + msg.getKey()
                        + " msgId is：" + sendResult.getMessageId());
            }

        } catch (ONSClientException e) {
            e.printStackTrace();
        }
    }

}
