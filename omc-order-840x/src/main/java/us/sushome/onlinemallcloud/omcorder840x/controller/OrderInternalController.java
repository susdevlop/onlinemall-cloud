package us.sushome.onlinemallcloud.omcorder840x.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.order.OrderServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omcorder840x.service.OmOrderMainService;

@RestController
@RequestMapping("/internal/order")
public class OrderInternalController implements OrderServiceApi {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OmOrderMainService omOrderMainService;

    @GetMapping("/getOrderByIdFromFeign")
    @Override
    public OrderVo getOrderById(@RequestParam String orderId, @RequestParam String userId) {
        return omOrderMainService.getOrderById(orderId,userId);
    }

    @PostMapping("/newOrderFromFeign")
    @Override
    public ResultVo<NewOrderVo> newOrder(OrderVo order) {
        return omOrderMainService.newOrder(order);
    }

    @PostMapping("/updateOrderInfoFromFeign")
    @Override
    public CodeMsg updateOrderInfo(OrderVo order) {
        return omOrderMainService.updateOrderInfo(order);
    }

    @GetMapping("/getTodayOrderByUserOrderFromFeign")
    @Override
    public OrderVo getTodayOrderByUserOrder(@RequestParam String seckillId, @RequestParam String userId) {
        return omOrderMainService.getTodayOrderByUserOrder(seckillId,userId);
    }
}
