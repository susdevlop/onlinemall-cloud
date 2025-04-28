package us.sushome.onlinemallcloud.omcgoods830x.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2025-02-21
 */
@ToString
@Getter
@Setter
@TableName("om_goods")
@ApiModel(value = "OmGoods对象", description = "")
public class OmGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("uuid")
    @TableId("om_goods_id")
    private String omGoodsId;

    @TableField("om_goods_name")
    private String omGoodsName;

    @ApiModelProperty("使用 0 到 30000的数值去存储")
    @TableField("om_goods_type")
    private Integer omGoodsType;

    @TableField("om_goods_medialist")
    private String omGoodsMedialist;

    @TableField("om_goods_content")
    private String omGoodsContent;

    @TableField("om_goods_updatetime")
    private LocalDateTime omGoodsUpdatetime;

    @TableField("om_goods_addtime")
    private LocalDateTime omGoodsAddtime;

    @ApiModelProperty("发货地")
    @TableField("om_goods_shipaddress")
    private String omGoodsShipaddress;
}
