package us.sushome.onlinemallcloud.omcmq850x.component;

import lombok.Getter;
//import org.eclipse.paho.mqttv5.client.*;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MqttClientComponent {
    //思考过后认为不用 mqtt 作为生成订单后返回消息给前端，此组件仅作记录
    //public MqttClient client;

    //@PostConstruct
    //public void init(){
    //    try{
    //        String clientId = "mqttx_" + System.currentTimeMillis();
    //        String broker = "ws:// xxxx:8083/mqtt";
    //        MqttClient mqttClient = new MqttClient(broker, clientId);
    //        mqttClient.setCallback(new MqttCallback() {
    //            @Override
    //            public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
    //                System.out.println("disconnected: " + mqttDisconnectResponse.getReasonString());
    //            }
    //
    //            @Override
    //            public void mqttErrorOccurred(MqttException e) {
    //                System.out.println("mqttErrorOccurred: " + e.getMessage());
    //            }
    //
    //            @Override
    //            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    //                System.out.println("qos: " + mqttMessage.getQos());
    //                System.out.println("message content: " + new String(mqttMessage.getPayload()));
    //            }
    //
    //            @Override
    //            public void deliveryComplete(IMqttToken iMqttToken) {
    //                System.out.println("deliveryComplete"+iMqttToken.toString());
    //            }
    //
    //            @Override
    //            public void connectComplete(boolean b, String s) {
    //                System.out.println("connected to: " + s);
    //            }
    //
    //            @Override
    //            public void authPacketArrived(int i, MqttProperties mqttProperties) {
    //                System.out.println("authPacketArrived"+mqttProperties.toString());
    //            }
    //        });
    //        MqttConnectionOptions options = new MqttConnectionOptions();
    //        options.setUserName("admin");
    //        options.setPassword("susmqtt123".getBytes(StandardCharsets.UTF_8));
    //        // 保留会话
    //        options.setCleanStart(true);
    //        options.setAutomaticReconnect(true);
    //        options.setConnectionTimeout(30);  // 30秒连接超时
    //        options.setKeepAliveInterval(60);  // 60秒心跳间隔
    //        mqttClient.connect(options);
    //        client = mqttClient;
    //        System.out.println("mqtt client connected");
    //    }catch (Exception e){
    //        System.out.println(e);
    //    }
    //}
}
