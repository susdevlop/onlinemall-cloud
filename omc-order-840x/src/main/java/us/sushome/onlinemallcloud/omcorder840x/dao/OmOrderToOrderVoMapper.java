package us.sushome.onlinemallcloud.omcorder840x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OmOrderToOrderVoMapper {

    @Mapping(source = "omOrderId",target = "orderId")
    @Mapping(source = "omOrderStatus",target = "status")
    @Mapping(source = "omOrderSum",target = "sum")
    @Mapping(source = "omOrderUserid",target = "userId")
    @Mapping(source = "omOrderConsignee",target = "consignee")
    @Mapping(source = "omOrderPhone",target = "phone")
    @Mapping(source = "omOrderAddress",target = "address")
    @Mapping(source = "omOrderDate",target = "date")
    @Mapping(source = "omOrderUpdatetime",target = "updateTime")
    @Mapping(source = "omOrderAddtime",target = "addTime")
    @Mapping(source = "omOrderCouponid",target = "couponId")
    @Mapping(source = "omOrderType",target = "type")
    @Mapping(source = "omOrderPaydate",target = "payDate")
    @Mapping(source = "omOrderPayway",target = "payWay")
    @Mapping(source = "omOrderSn",target = "orderSn")
    @Mapping(source = "omOrderExpress",target = "expressName")
    @Mapping(source = "omOrderRemark",target = "remark")
    OrderVo omOrderToOrderVo(OmOrder omOrder);

    @Mapping(source = "orderId",target = "omOrderId")
    @Mapping(source = "status",target = "omOrderStatus")
    @Mapping(source = "sum",target = "omOrderSum")
    @Mapping(source = "userId",target = "omOrderUserid")
    @Mapping(source = "consignee",target = "omOrderConsignee")
    @Mapping(source = "phone",target = "omOrderPhone")
    @Mapping(source = "address",target = "omOrderAddress")
    @Mapping(source = "date",target = "omOrderDate")
    @Mapping(source = "updateTime",target = "omOrderUpdatetime")
    @Mapping(source = "addTime",target = "omOrderAddtime")
    @Mapping(source = "couponId",target = "omOrderCouponid")
    @Mapping(source = "type",target = "omOrderType")
    @Mapping(source = "payDate",target = "omOrderPaydate")
    @Mapping(source = "payWay",target = "omOrderPayway")
    @Mapping(source = "orderSn",target = "omOrderSn")
    @Mapping(source = "expressName",target = "omOrderExpress")
    @Mapping(source = "remark",target = "omOrderRemark")
    OmOrder orderVoToOmOrder(OrderVo orderVo);

    List<OrderVo> omOrderToOrderVoList(List<OmOrder> omOrders);
}
