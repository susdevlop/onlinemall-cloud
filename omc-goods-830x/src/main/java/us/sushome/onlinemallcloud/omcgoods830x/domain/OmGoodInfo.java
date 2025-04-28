package us.sushome.onlinemallcloud.omcgoods830x.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OmGoodInfo extends OmGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("om_goodsku_id")
    private String omGoodskuId;

    @TableField("om_goodsku_goodsid")
    private String omGoodskuGoodsid;

    @TableField("om_goodsku_name")
    private String omGoodskuName;

    @TableField("om_goodsku_price")
    private BigDecimal omGoodskuPrice;

    @TableField("om_goodsku_inventory")
    private Integer omGoodskuInventory;

    @TableField("om_goodsku_media")
    private String omGoodskuMedia;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    @TableField("om_goodsku_updatetime")
    private LocalDateTime omGoodskuUpdatetime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    @TableField("om_goodsku_addtime")
    private LocalDateTime omGoodskuAddtime;

    @ApiModelProperty("乐观锁更新版本")
    @TableField("om_goodsku_version")
    private Long omGoodskuVersion;
}
