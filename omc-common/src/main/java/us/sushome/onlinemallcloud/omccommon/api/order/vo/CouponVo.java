package us.sushome.onlinemallcloud.omccommon.api.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVo implements Serializable{
    private String couponId;
    private String couponName;
    private String couponType;
    private BigDecimal couponDiscount;
    private BigDecimal couponConditions;
    private Integer couponStatus;
    private LocalDateTime couponStartTime;
    private LocalDateTime couponEndTime;
    private String userId;
    private String ownerId;
    private LocalDateTime updateTime;
    private LocalDateTime addTime;
}
