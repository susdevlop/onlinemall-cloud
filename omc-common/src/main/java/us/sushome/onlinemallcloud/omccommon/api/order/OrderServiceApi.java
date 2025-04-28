package us.sushome.onlinemallcloud.omccommon.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;

@FeignClient(value = "omc-order-840x",path = "/openApi/order")
public interface OrderServiceApi {

    @GetMapping("/getOrderByIdFromFeign")
    OrderVo getOrderById(String orderId,String userId);

    @Transactional
    @PostMapping("/newOrderFromFeign")
    ResultVo<NewOrderVo> newOrder(OrderVo order);

    @PostMapping("/updateOrderInfoFromFeign")
    CodeMsg updateOrderInfo(OrderVo order);

    @GetMapping("/getTodayOrderByUserOrderFromFeign")
    OrderVo getTodayOrderByUserOrder(String seckillId,String userId);
}
