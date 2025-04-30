package us.sushome.onlinemallcloud.omcmq850x.receiver;

import com.alibaba.fastjson2.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.SeckillKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.goods.GoodsServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.OrderServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderDetailVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.cache.RedisService;
import us.sushome.onlinemallcloud.omccommon.constants.OrderConstants;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omcmq850x.config.RocketMQConfig;
import us.sushome.onlinemallcloud.omcmq850x.service.MqProviderService;

import javax.annotation.Resource;
import java.util.Objects;

import static us.sushome.onlinemallcloud.omccommon.constants.OrderConstants.UN_CREATE_ORDER;


@Service
@RocketMQMessageListener(
        topic = RocketMQConfig.ORDER_TOPIC,
        consumerGroup = RocketMQConfig.SECKILL_ORDER_CONSUMER_DELAY_GROUP,
        selectorExpression = RocketMQConfig.SECKILL_ORDER_DELAY_TAG
)
public class SeckillOrderDelayConsumerService implements RocketMQListener<MessageExt> {
    private static Logger logger = LoggerFactory.getLogger(SeckillOrderDelayConsumerService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    OrderServiceApi orderServiceApi;

    @Autowired
    GoodsServiceApi goodsServiceApi;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(MessageExt messageExt) {
        logger.info("收到秒杀订单延迟消息：{}",messageExt);
        OrderVo orderVo = JSON.parseObject(messageExt.getBody(), OrderVo.class);
        int retryCount = Integer.parseInt(messageExt.getProperty("tryCount"));
        if(Objects.equals(orderVo.getOrderId(), UN_CREATE_ORDER)){
            updateInventory(orderVo,retryCount,10);//未创建成功的订单，直接回复库存
        }else{
            //当执行重试必然是库存未处理
            //获取最新订单状态
            OrderVo order = orderServiceApi.getOrderById(orderVo.getOrderId(),null);
            Integer orderType = order.getType();
            if(Objects.equals(orderType, OrderConstants.SECKILL_ORDER) && Objects.equals(order.getStatus(), OrderConstants.NEW_UNPAID)){
                if(retryCount == 0){
                    order.setStatus(OrderConstants.CANCELED);
                    CodeMsg codeMsg = orderServiceApi.updateOrderInfo(order);
                    logger.info("取消订单：{}",codeMsg.getMsg());
                    updateInventory(order,retryCount,60);
                }else{
                    //重试操作，继续更新库存
                    updateInventory(order,retryCount,60);
                }
            }
        }
    }

    public void updateInventory(OrderVo order,Integer retryCount,int delaySeconds){
        //未支付的订单只通过 redis 锁库存，取消订单时需要恢复(增加)库存
        OrderDetailVo orderDetail = order.getOrderDetails().get(0);
        String seckillId = orderDetail.getSeckillId();
        boolean result = goodsServiceApi.incrementSeckillGoodInventory(seckillId);
        String innerText = Objects.equals(order.getOrderId(), UN_CREATE_ORDER) ? "恢复未创建抢购订单的库存" : "恢复未支付抢购订单的库存";
        if(result){
            redisService.increment(SeckillKeyPrefix.SK_GOOD_STOCK,seckillId);
        }else{
            logger.info("恢复{}时因为乐观锁而失败，执行重试",innerText);
            if(retryCount < 19){
                retryCount++;
                logger.info("重试{}次数：{}",innerText,retryCount);
                MqProviderService.sendDelayCloseOrderMessage(rocketMQTemplate,order,RocketMQConfig.SECKILL_ORDER_DELAY_TAG,delaySeconds*retryCount,retryCount);
            }else if(retryCount == 19 || retryCount == 20){
                delaySeconds = retryCount == 19 ? 60 * 30 : 60 * 60;
                MqProviderService.sendDelayCloseOrderMessage(rocketMQTemplate,order,RocketMQConfig.SECKILL_ORDER_DELAY_TAG,delaySeconds,retryCount);
            }else{
                logger.info("重试{}次数过多,请人工处理,{}",innerText,order);
            }
        }
    }
}
