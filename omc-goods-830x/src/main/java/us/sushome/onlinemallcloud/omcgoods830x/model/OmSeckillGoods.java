package us.sushome.onlinemallcloud.omcgoods830x.model;

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
 * @since 2025-04-14
 */
@ToString
@Getter
@Setter
@TableName("om_seckill_goods")
@ApiModel(value = "OmSeckillGoods对象", description = "")
public class OmSeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("om_osg_id")
    private String omOsgId;

    @TableField("om_osg_goodid")
    private String omOsgGoodid;

    @TableField("om_osg_goodtype")
    private Integer omOsgGoodtype;

    @ApiModelProperty("商品id")
    @TableField("om_osg_goodskuid")
    private String omOsgGoodskuid;

    @TableField("om_osg_name")
    private String omOsgName;

    @ApiModelProperty("秒杀价")
    @TableField("om_osg_price")
    private BigDecimal omOsgPrice;

    @ApiModelProperty("库存")
    @TableField("om_osg_stock")
    private Integer omOsgStock;

    @ApiModelProperty("开始时间")
    @TableField("om_osg_starttime")
    private LocalDateTime omOsgStarttime;

    @ApiModelProperty("结束时间")
    @TableField("om_osg_endtime")
    private LocalDateTime omOsgEndtime;

    @TableField("om_osg_updatetime")
    private LocalDateTime omOsgUpdatetime;

    @TableField("om_osg_addtime")
    private LocalDateTime omOsgAddtime;

    @ApiModelProperty("状态 0 关闭 1 开启 2每天开启")
    @TableField("om_osg_status")
    private Integer omOsgStatus;

    @ApiModelProperty("乐观锁更新版本")
    @TableField("om_osg_version")
    private Long omOsgVersion;
}
