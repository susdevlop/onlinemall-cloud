package us.sushome.onlinemallcloud.omccommon.api.goods.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoodSkuVo implements Serializable{
    private String id;

    private String goodsId;

    private String name;

    private BigDecimal price;

    private Integer inventory;

    private String media;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    private Long version;
}
