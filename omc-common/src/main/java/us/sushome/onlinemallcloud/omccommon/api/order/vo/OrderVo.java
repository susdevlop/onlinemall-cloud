package us.sushome.onlinemallcloud.omccommon.api.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVo implements Serializable {
    private String orderId;
    private Integer status;
    //0新建未支付 1已支付 2未发货 3已发货 4已退款 5已完成 6已换货 7换货中 8所有售后
    //20未发货售后中  24未发货售后退款中 30已发货售后中 34已发货售后退款中 37已发货售后换货中
    //40已完成售后 44已完成售后退款  47已完成售后换货 -1已关闭
    private BigDecimal sum;
    private String userId;
    private String consignee;//收货人
    private String phone;
    private String address;
    private LocalDate date;
    private LocalDateTime updateTime;
    private LocalDateTime addTime;
    private String couponName;
    private String couponId;
    private Integer type;//订单类型 0 普通订单 1 秒杀订单
    private LocalDateTime payDate;//订单支付时间
    private Integer payWay;//1 支付宝 2微信支付 3余额支付
    private List<OrderDetailVo> orderDetails;
    private String orderSn;
    private String expressName;
    private String remark;
}
