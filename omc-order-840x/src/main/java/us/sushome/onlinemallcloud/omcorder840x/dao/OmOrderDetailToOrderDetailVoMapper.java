package us.sushome.onlinemallcloud.omcorder840x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderDetailVo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrderdetail;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OmOrderDetailToOrderDetailVoMapper {

    @Mapping(source = "omOrderdetailId", target = "orderDetailId")
    @Mapping(source = "omOrderdetailOrderid", target = "orderId")
    @Mapping(source = "omOrderdetailGoodname", target = "goodName")
    @Mapping(source = "omOrderdetailSeckillid",target = "seckillId")
    @Mapping(source = "omOrderdetailSkuid", target = "skuId")
    @Mapping(source = "omOrderdetailCount", target = "count")
    @Mapping(source = "omOrderdetailUnitprice", target = "uniPrice")
    @Mapping(source = "omOrderdetailSumprice", target = "sumPrice")
    @Mapping(source = "omOrderdetailUpdatetime", target = "updateTime")
    @Mapping(source = "omOrderdetailAddtime", target = "addTime")
    OrderDetailVo omOrderDetailToOrderDetailVo(OmOrderdetail omOrderDetail);

    @Mapping(source = "orderDetailId", target = "omOrderdetailId")
    @Mapping(source = "orderId", target = "omOrderdetailOrderid")
    @Mapping(source = "goodName", target = "omOrderdetailGoodname")
    @Mapping(source = "seckillId",target = "omOrderdetailSeckillid")
    @Mapping(source = "skuId", target = "omOrderdetailSkuid")
    @Mapping(source = "count", target = "omOrderdetailCount")
    @Mapping(source = "uniPrice", target = "omOrderdetailUnitprice")
    @Mapping(source = "sumPrice", target = "omOrderdetailSumprice")
    @Mapping(source = "updateTime", target = "omOrderdetailUpdatetime")
    @Mapping(source = "addTime", target = "omOrderdetailAddtime")
    OmOrderdetail orderDetailVoToOmOrderDetail(OrderDetailVo orderDetailVo);

    List<OrderDetailVo> omOrderDetailListToOrderDetailVoList(List<OmOrderdetail> omOrderDetailList);

    List<OmOrderdetail> orderDetailVoListToOmOrderDetailList(List<OrderDetailVo> orderDetailVoList);
}
