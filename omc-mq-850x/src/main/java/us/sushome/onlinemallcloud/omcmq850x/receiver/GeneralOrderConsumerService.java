package us.sushome.onlinemallcloud.omcmq850x.receiver;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omcmq850x.config.RocketMQConfig;

@Service
@RocketMQMessageListener(
        topic = RocketMQConfig.ORDER_TOPIC,
        consumerGroup = RocketMQConfig.GENERAL_ORDER_CONSUMER_GROUP,
        selectorExpression = RocketMQConfig.GENERAL_ORDER_TAG
)
public class GeneralOrderConsumerService implements RocketMQListener<OrderVo> {

    @Override
    public void onMessage(OrderVo orderVo) {
        //暂时普通订单不通过 mq 处理
    }
}
