package us.sushome.onlinemallcloud.omcorder840x.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("om_coupon")
@ApiModel(value = "OmCoupon对象", description = "")
public class OmCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("om_coupon_id")
    private String omCouponId;

    @TableField("om_coupon_name")
    private String omCouponName;

    @ApiModelProperty("0金额券1折扣券")
    @TableField("om_coupon_type")
    private Byte omCouponType;

    @ApiModelProperty("无论是折购还是金额券都用该字段比如金额30券就是30,比如折购七折优惠券就是0.7")
    @TableField("om_coupon_discount")
    private BigDecimal omCouponDiscount;

    @ApiModelProperty("使用条件需要满多少钱可用")
    @TableField("om_coupon_conditions")
    private BigDecimal omCouponConditions;

    @ApiModelProperty("0已使用1未使用2未考虑加入已过期，暂时未过期仅以时间做判定")
    @TableField("om_coupon_status")
    private Integer omCouponStatus;

    @TableField("om_coupon_starttime")
    private LocalDateTime omCouponStarttime;

    @TableField("om_coupon_endtime")
    private LocalDateTime omCouponEndtime;

    @ApiModelProperty("可以为空，空时未发放给别人")
    @TableField("om_coupon_userid")
    private String omCouponUserid;

    @ApiModelProperty("优惠券创建者，发放者id,不能为空")
    @TableField("om_coupon_owner")
    private String omCouponOwner;

    @TableField("om_coupon_updatetime")
    private LocalDateTime omCouponUpdatetime;

    @TableField("om_coupon_addtime")
    private LocalDateTime omCouponAddtime;
}
