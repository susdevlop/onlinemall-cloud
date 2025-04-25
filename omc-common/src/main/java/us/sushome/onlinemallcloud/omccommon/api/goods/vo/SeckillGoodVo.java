package us.sushome.onlinemallcloud.omccommon.api.goods.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillGoodVo implements Serializable {
    private String id;

    private String goodId;

    private Integer goodsType;

    private String goodSkuId;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    private GoodInfoVo goodInfo;

    private Integer status;

    private Long version;
}
