package us.sushome.onlinemallcloud.omcmq850x.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import us.sushome.onlinemallcloud.omccommon.api.mq.MqProviderServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omcmq850x.config.RocketMQConfig;

@DubboService(interfaceClass = MqProviderServiceApi.class)
public class MqProviderService implements MqProviderServiceApi {
    private static Logger logger = LoggerFactory.getLogger(MqProviderService.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    //messageDelayLevel=
    //1 1s 5s 10s 30s
    //5 1m 2m 3m 4m
    //9 5m 6m 7m 8m
    //13 9m 10m 20m 30m
    //17 1h 2h
    private static int calculateDelayLevel(int delaySeconds) {
        if (delaySeconds <= 1) return 1;      // 1s
        if (delaySeconds <= 5) return 2;      // 5s
        if (delaySeconds <= 10) return 3;     // 10s
        if (delaySeconds <= 30) return 4;     // 30s
        if (delaySeconds <= 60) return 5;     // 1m
        if (delaySeconds <= 120) return 6;    // 2m
        if (delaySeconds <= 180) return 7;
        if (delaySeconds <= 240) return 8;
        if (delaySeconds <= 300) return 9;
        if (delaySeconds <= 360) return 10;
        if (delaySeconds <= 420) return 11;
        if (delaySeconds <= 480) return 12;
        if (delaySeconds <= 540) return 13;
        if (delaySeconds <= 600) return 14;
        if (delaySeconds <= 1200) return 15;
        if (delaySeconds <= 1800) return 16;
        if (delaySeconds <= 3600) return 17;
        if (delaySeconds <= 7200) return 18;
        return 18; // 默认30分钟（级别16）
    }

    @Override
    public void sendOrderMessage(OrderVo orderVo) {
        if(orderVo == null){
            logger.error("orderVo is null");
        }else{
            Integer type = orderVo.getType();
            if(type == 0){
                sendSkMessageAsync(orderVo,RocketMQConfig.ORDER_TOPIC,RocketMQConfig.GENERAL_ORDER_TAG);
            }else if(type == 1){
                //RocketMQConfig.SECKILL_ORDER_TAG;
                sendSkMessageAsync(orderVo,RocketMQConfig.SECKILL_TOPIC,RocketMQConfig.SECKILL_ORDER_TAG);
            }else{
                logger.error("未知订单类型");
            }
        }
    }

    public void sendSkMessageAsync(OrderVo orderVo,String topic,String tag) {
        logger.info("发送订单消息: {}{}", orderVo, tag);
        rocketMQTemplate.asyncSend(topic+":"+tag, orderVo, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("消息发送成功: {}{}", sendResult, tag);
                // 这里可以添加成功后的业务逻辑
            }
            @Override
            public void onException(Throwable e) {
                logger.error("消息发送失败: {}{}", orderVo, tag, e);
                // 这里可以添加失败后的处理逻辑，如重试等
            }
        });
    }


    @Override
    public void sendDelayOrderMessage(OrderVo orderVo,int delaySeconds) {
        if(orderVo == null){
            logger.error("延时队列参数有误");
        }else{
            Integer type = orderVo.getType();
            if(type == 0){
                sendDelayCloseOrderMessage(rocketMQTemplate,orderVo,RocketMQConfig.GENERAL_ORDER_DELAY_TAG,delaySeconds,0);
            }else if(type == 1){
                sendDelayCloseOrderMessage(rocketMQTemplate,orderVo,RocketMQConfig.SECKILL_ORDER_DELAY_TAG,delaySeconds,0);
            }
        }
    }

    public static void sendDelayCloseOrderMessage(RocketMQTemplate rocketMQTemplate,OrderVo orderVo,String tag,int delaySeconds,int tryCount) {
        logger.info("发送延时订单消息: {}{} {}秒后将消费", orderVo, tag,delaySeconds);
        rocketMQTemplate.asyncSend(RocketMQConfig.ORDER_TOPIC+":"+tag,
                MessageBuilder
                        .withPayload(orderVo)
                        .setHeader("tryCount",tryCount)
                        .build(),
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        logger.info("延迟关单消息发送成功，订单ID: {} {}", sendResult,tag);
                    }
                    @Override
                    public void onException(Throwable e) {
                        logger.error("延迟关单消息发送失败: {} {}", orderVo, tag,e);
                    }
                },3000,calculateDelayLevel(delaySeconds));
    }

}
