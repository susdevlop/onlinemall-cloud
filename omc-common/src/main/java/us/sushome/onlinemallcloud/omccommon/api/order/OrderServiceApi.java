package us.sushome.onlinemallcloud.omccommon.api.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.saToken.FeignInterceptor;

@FeignClient(value = "omc-order-840x",path = "/internal/order",configuration = FeignInterceptor.class)
public interface OrderServiceApi {

    @GetMapping("/getOrderByIdFromFeign")
    OrderVo getOrderById(@RequestParam String id, @RequestParam String name);

    @Transactional
    @PostMapping("/newOrderFromFeign")
    ResultVo<NewOrderVo> newOrder(@RequestBody OrderVo order);

    @PostMapping("/updateOrderInfoFromFeign")
    CodeMsg updateOrderInfo(@RequestBody OrderVo order);

    @GetMapping("/getTodayOrderByUserOrderFromFeign")
    OrderVo getTodayOrderByUserOrder(@RequestParam String seckillId,@RequestParam String userId);
}
