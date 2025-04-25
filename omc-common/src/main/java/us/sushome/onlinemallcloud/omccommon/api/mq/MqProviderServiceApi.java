package us.sushome.onlinemallcloud.omccommon.api.mq;

import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;

public interface MqProviderServiceApi{


    void sendOrderMessage(OrderVo orderVo);

    void sendDelayOrderMessage(OrderVo orderVo,int delaySeconds);
}
