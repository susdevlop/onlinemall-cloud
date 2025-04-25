package us.sushome.onlinemallcloud.omccommon.api.order.vo;

import lombok.Data;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetailVo implements Serializable {
    private String orderDetailId;
    private String orderId;
    private String goodName;
    private String seckillId;
    private String skuId;
    private Integer count;
    private GoodInfoVo goodInfo;
    private BigDecimal uniPrice;
    private BigDecimal sumPrice;
    private LocalDateTime updateTime;
    private LocalDateTime addTime;
}
