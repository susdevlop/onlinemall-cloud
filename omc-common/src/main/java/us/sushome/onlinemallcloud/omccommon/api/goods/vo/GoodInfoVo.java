package us.sushome.onlinemallcloud.omccommon.api.goods.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GoodInfoVo implements Serializable {

    private String goodsId;

    private String name;

    private Integer goodsType;

    private String mediaList;

    private String content;

    private List<GoodSkuVo> skuList;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    private String shipAddress;
}
