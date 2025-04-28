package us.sushome.onlinemallcloud.omcorder840x.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OmOrderInfo extends OmOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("om_orderdetail_id")
    private String omOrderdetailId;

    @TableField("om_orderdetail_orderid")
    private String omOrderdetailOrderid;

    @TableField("om_orderdetail_goodname")
    private String omOrderdetailGoodname;

    @TableField("om_orderdetail_skuid")
    private String omOrderdetailSkuid;

    @ApiModelProperty("默认空，当订单为抢购订单时值为抢购商品的 id")
    @TableField("om_orderdetail_seckillid")
    private String omOrderdetailSeckillid;

    @TableField("om_orderdetail_count")
    private Integer omOrderdetailCount;

    @TableField("om_orderdetail_unitprice")
    private BigDecimal omOrderdetailUnitprice;

    @TableField("om_orderdetail_sumprice")
    private BigDecimal omOrderdetailSumprice;

    @TableField("om_orderdetail_updatetime")
    private LocalDateTime omOrderdetailUpdatetime;

    @TableField("om_orderdetail_addtime")
    private LocalDateTime omOrderdetailAddtime;
}
