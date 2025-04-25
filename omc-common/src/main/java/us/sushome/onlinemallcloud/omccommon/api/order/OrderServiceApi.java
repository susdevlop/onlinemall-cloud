package us.sushome.onlinemallcloud.omccommon.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;

public interface OrderServiceApi {

    OrderVo getOrderById(String orderId,String userId);

    IPage<OrderVo> getOrderList(String orderId, String userId, Integer status, Integer type, String phone, String payDate, String goodName, Integer pageNum, Integer pageSize);

    @Transactional
    ResultVo<NewOrderVo> newOrder(OrderVo order);

    CodeMsg updateOrderInfo(OrderVo order);

    OrderVo getTodayOrderByUserOrder(String seckillId,String userId);

    Boolean deleteOrder(String id);
}
