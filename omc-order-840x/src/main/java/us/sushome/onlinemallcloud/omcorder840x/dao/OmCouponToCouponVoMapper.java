package us.sushome.onlinemallcloud.omcorder840x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.CouponVo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmCoupon;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OmCouponToCouponVoMapper {

    @Mapping(source = "omCouponId", target = "couponId")
    @Mapping(source = "omCouponName", target = "couponName")
    @Mapping(source = "omCouponType", target = "couponType")
    @Mapping(source = "omCouponDiscount", target = "couponDiscount")
    @Mapping(source = "omCouponConditions", target = "couponConditions")
    @Mapping(source = "omCouponStatus", target = "couponStatus")
    @Mapping(source = "omCouponStarttime", target = "couponStartTime")
    @Mapping(source = "omCouponEndtime", target = "couponEndTime")
    @Mapping(source = "omCouponUserid", target = "userId")
    @Mapping(source = "omCouponOwner", target = "ownerId")
    @Mapping(source = "omCouponUpdatetime", target = "updateTime")
    @Mapping(source = "omCouponAddtime", target = "addTime")
    CouponVo omCouponToCouponVo(OmCoupon omCoupon);

    List<CouponVo> omCouponToCouponVoList(List<OmCoupon> omCoupons);
}
