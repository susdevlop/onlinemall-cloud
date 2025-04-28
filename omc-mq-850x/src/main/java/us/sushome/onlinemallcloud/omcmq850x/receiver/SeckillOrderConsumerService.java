package us.sushome.onlinemallcloud.omcmq850x.receiver;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.OrderKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.order.OrderServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.cache.RedisService;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omcmq850x.config.RocketMQConfig;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RocketMQMessageListener(
    topic = RocketMQConfig.SECKILL_TOPIC,
    consumerGroup = RocketMQConfig.SECKILL_ORDER_CONSUMER_GROUP,
    selectorExpression = RocketMQConfig.SECKILL_ORDER_TAG
)
public class SeckillOrderConsumerService implements RocketMQListener<OrderVo> {
    private static Logger logger = LoggerFactory.getLogger(SeckillOrderConsumerService.class);

    @Autowired
    private RedisService redisService;

    @Resource
    OrderServiceApi orderServiceApi;

    //@Autowired
    //private MqttClientComponent mqttClientComponent;

    @Override
    public void onMessage(OrderVo orderVo) {
        logger.info("收到秒杀订单消息: {}", orderVo);
        String userId = orderVo.getUserId();
        String seckillGoodId = orderVo.getOrderDetails().get(0).getSeckillId();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = today.format(formatter);// 生成日期字符串
        OrderVo order = redisService.get(OrderKeyPrefix.ORDER,userId+"_"+ dateStr+"_"+seckillGoodId,OrderVo.class);
        if(order == null){
            //当天为购买 继续通过数据库寻找
            order = orderServiceApi.getTodayOrderByUserOrder(seckillGoodId,userId);
        }
        System.out.println("用户抢购的订单数据"+order+"\n");
        if(order != null){
            logger.info("不能重复抢购: {}",order);
            return;
        }
        ResultVo<NewOrderVo> result = orderServiceApi.newOrder(orderVo);
        CodeMsg codeMsg = result.getMessage();
        logger.info("订单创建结果: {} {} {} {}", result,codeMsg,codeMsg.getMsg(),codeMsg.getCode());
        //前端通过预创建订单 id 去获取支付状态



        //与前端通信 该用 sse 推送支付结果
        //MqttClient client = mqttClientComponent.getClient();
        //String topic = RocketMQConfig.MQTT_ORDER_RESULT_TOPIC + "/" + userId;
        //String jsonMessage = JSON.toJSONString(result);
        //MqttMessage message = new MqttMessage(jsonMessage.getBytes());
        //message.setQos(1);
        //try{
        //    client.publish(topic,message);
        //    logger.info("MQTT消息已发送: {} {}", topic, jsonMessage);
        //}catch (Exception e){
        //    logger.error("mqtt消息发送失败: {}", e.getMessage());
        //}
    }
}
