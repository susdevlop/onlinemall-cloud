package us.sushome.onlinemallcloud.omcorder840x.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author sushome
 * @since 2025-03-26
 */
@ToString
@Getter
@Setter
@TableName("om_order")
@ApiModel(value = "OmOrder对象", description = "")
public class OmOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("om_order_id")
    private String omOrderId;

    @ApiModelProperty("0新建未支付 1已支付 2未发货 3已发货 4已退款 5已完成 6已换货 7换货中 8所有售后\n20未发货售后中  24未发货售后退款中 30已发货售后中 34已发货售后退款中 37已发货售后换货中\n40已完成售后 44已完成售后退款  47已完成售后换货 -1已关闭")
    @TableField("om_order_status")
    private Integer omOrderStatus;

    @TableField("om_order_sum")
    private BigDecimal omOrderSum;

    @TableField("om_order_userid")
    private String omOrderUserid;

    @TableField("om_order_consignee")
    private String omOrderConsignee;

    @TableField("om_order_phone")
    private String omOrderPhone;

    @TableField("om_order_address")
    private String omOrderAddress;

    @TableField("om_order_date")
    private LocalDate omOrderDate;

    @TableField("om_order_updatetime")
    private LocalDateTime omOrderUpdatetime;

    @TableField("om_order_addtime")
    private LocalDateTime omOrderAddtime;

    @TableField("om_order_couponid")
    private String omOrderCouponid;

    @ApiModelProperty("订单类型 0 普通订单 1 秒杀订单")
    @TableField("om_order_type")
    private Integer omOrderType;

    @ApiModelProperty("订单支付时间")
    @TableField("om_order_paydate")
    private LocalDateTime omOrderPaydate;

    @ApiModelProperty("1 支付宝 2微信支付 3余额支付")
    @TableField("om_order_payway")
    private Integer omOrderPayway;

    @ApiModelProperty("快递单号")
    @TableField("om_order_sn")
    private String omOrderSn;

    @TableField("om_order_express")
    private String omOrderExpress;

    @TableField("om_order_remark")
    private String omOrderRemark;
}
