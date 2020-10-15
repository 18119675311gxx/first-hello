package com.beizeng.admin.common.utils.rocket;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: <h1>MessageListener 监听消费者消息</h1>
 * @author:
 **/
@Slf4j
@Component
public class RocketMessageListener implements MessageListener {

    @Override
    public Action consume(Message message, ConsumeContext context) {

        System.out.println("Receive: " + JSON.toJSONString(message));
        try {
            String msgTag = message.getTag();   // 消息类型
            String msgKey = message.getKey();   // 唯一key

            String body = new String(message.getBody());
            switch (msgTag) {
                case "appMsgPush":
                    System.out.println(message.getUserProperties("UNIQ_KEY"));
                    System.out.println("msgKey = " + msgKey);
                    log.info("body={}", body);
                    break;
                case "msg2":
                    //  处理1
                    break;
                default:
                    //  处理
                    return Action.ReconsumeLater;
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }

}
