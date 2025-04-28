package us.sushome.onlinemallcloud.omcmq850x.config;


public class RocketMQConfig {
    public static final String BROKER_NAME = "broker-a";
    public static final String ORDER_TOPIC = "order-topic";
    public static final String SECKILL_TOPIC = "seckill-topic";

    public static final String SECKILL_ORDER_CONSUMER_GROUP = "seckill_order_consumer_group";
    public static final String GENERAL_ORDER_CONSUMER_GROUP = "general_order_consumer_group";
    public static final String GENERAL_ORDER_CONSUMER_DELAY_GROUP = "general_order_consumer_delay_group";
    public static final String SECKILL_ORDER_CONSUMER_DELAY_GROUP = "seckill_order_consumer_delay_group";

    public static final String GENERAL_ORDER_TAG = "general_order_tag";
    public static final String SECKILL_ORDER_TAG = "seckill_order_tag";

    public static final String GENERAL_ORDER_DELAY_TAG = "general_order_delay_tag";
    public static final String SECKILL_ORDER_DELAY_TAG = "seckill_order_delay_tag";

    public static final String MQTT_ORDER_RESULT_TOPIC = "mqtt/order/result";
}
