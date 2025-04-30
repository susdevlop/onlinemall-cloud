package us.sushome.onlinemallcloud.omcmq850x.component;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.remoting.protocol.body.ClusterInfo;
import org.apache.rocketmq.remoting.protocol.route.BrokerData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcmq850x.config.RocketMQConfig;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class RocketMQTopicInitializer {
    private static Logger logger = LoggerFactory.getLogger(RocketMQTopicInitializer.class);

    // 从application.properties读取的配置
    @Value("${rocketmq.name-server}")
    private String nameServerAddr;



    @PostConstruct
    public void init(){
        if(StringUtils.isEmpty(nameServerAddr)){
            throw new IllegalStateException("rocketmq.name-server must be configured in application.properties");
        }

        createTopicIfNotExists();
    }

    private void createTopicIfNotExists(){
        DefaultMQAdminExt admin = new DefaultMQAdminExt(
            new AclClientRPCHook(new SessionCredentials("rocketmq2","12345678")),
           5000
        );
        try{
            admin.setNamesrvAddr(nameServerAddr);
            admin.setAdminExtGroup("ADMIN_GROUP");
            admin.setVipChannelEnabled(true);
            admin.start();
            Set<String> existingTopics = admin.fetchAllTopicList().getTopicList();
            // 需要初始化的 Topic
            String[] topicsToCreate = {RocketMQConfig.ORDER_TOPIC, RocketMQConfig.SECKILL_TOPIC};
            for (String topicName : topicsToCreate){
                if(existingTopics.contains(topicName)){
                    logger.info("Topic {} 已存在，无需重复创建", topicName);
                    return;
                }
                TopicConfig topicConfig = new TopicConfig();
                topicConfig.setTopicName(topicName);
                if (RocketMQConfig.SECKILL_TOPIC.equals(topicName)){
                    topicConfig.setReadQueueNums(6);
                    topicConfig.setWriteQueueNums(6);
                }else{
                    //普通订单和普通订单的延迟消息或抢购的延时消息使用多队列
                    topicConfig.setReadQueueNums(16);
                    topicConfig.setWriteQueueNums(16);
                }
                topicConfig.setPerm(6);
                ClusterInfo clusterInfo = admin.examineBrokerClusterInfo();
                BrokerData brokerData = clusterInfo.getBrokerAddrTable().get(RocketMQConfig.BROKER_NAME);
                String brokerAddr = "116.205.180.204:10911";
                if(brokerData == null){
                    logger.warn("Broker {} 不存在，使用默认地址 {}", RocketMQConfig.BROKER_NAME, brokerAddr);
                }else{
                    brokerAddr = brokerData.getBrokerAddrs().get(MixAll.MASTER_ID);
                    System.out.println("brokerAddr = " + brokerAddr);
                }
                admin.createAndUpdateTopicConfig(brokerAddr, topicConfig);
                // 5. 验证结果（带重试）
                validateTopicCreation(admin, brokerAddr, topicName, topicConfig);
            }

        } catch (Exception e) {
            logger.error("创建Topic失败", e);
            throw new RuntimeException("RocketMQ Topic 初始化失败", e);
        } finally {
            admin.shutdown();
        }
    }

    private void validateTopicCreation(DefaultMQAdminExt admin,
                                       String brokerAddr,
                                       String topicName,
                                       TopicConfig expectedConfig) throws Exception {
        int retry = 3;
        TopicConfig actualConfig = null;

        while (retry-- > 0) {
            try {
                actualConfig = admin.examineTopicConfig(brokerAddr, topicName);
                if (actualConfig != null) break;
            } catch (Exception e) {
                if (retry == 0) throw e;
                Thread.sleep(1000);
            }
        }

        if (actualConfig == null || !actualConfig.getTopicName().equals(topicName)) {
            throw new RuntimeException("Topic创建验证失败");
        }

        logger.info("Topic验证成功，当前配置：{}", actualConfig);
    }
}
