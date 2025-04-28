package us.sushome.onlinemallcloud.omcgoods830x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmSeckillGoods;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OmSeckillGoodsToSeckillGoodVoMapper {


    @Mapping(source = "omOsgId", target = "id")
    @Mapping(source = "omOsgGoodid", target = "goodId")
    @Mapping(source = "omOsgGoodtype", target = "goodsType")
    @Mapping(source = "omOsgGoodskuid", target = "goodSkuId")
    @Mapping(source = "omOsgName", target = "name")
    @Mapping(source = "omOsgPrice", target = "price")
    @Mapping(source = "omOsgStock", target = "stock")
    @Mapping(source = "omOsgStarttime", target = "startTime")
    @Mapping(source = "omOsgEndtime", target = "endTime")
    @Mapping(source = "omOsgUpdatetime", target = "addTime")
    @Mapping(source = "omOsgAddtime", target = "updateTime")
    @Mapping(source = "omOsgStatus", target = "status")
    @Mapping(source = "omOsgVersion", target = "version")
    SeckillGoodVo omSeckillGoodsToSeckillGoodVo(OmSeckillGoods omSeckillGoods);

    @Mapping(source = "id", target = "omOsgId")
    @Mapping(source = "goodId", target = "omOsgGoodid")
    @Mapping(source = "goodsType", target = "omOsgGoodtype")
    @Mapping(source = "goodSkuId", target = "omOsgGoodskuid")
    @Mapping(source = "name", target = "omOsgName")
    @Mapping(source = "price", target = "omOsgPrice")
    @Mapping(source = "stock", target = "omOsgStock")
    @Mapping(source = "startTime", target = "omOsgStarttime")
    @Mapping(source = "endTime", target = "omOsgEndtime")
    @Mapping(source = "addTime", target = "omOsgAddtime")
    @Mapping(source = "updateTime", target = "omOsgUpdatetime")
    @Mapping(source = "status", target = "omOsgStatus")
    @Mapping(source = "version", target = "omOsgVersion")
    OmSeckillGoods seckillGoodVoToOmSeckillGoods(SeckillGoodVo seckillGoodVo);

    OmSeckillGoods seckillGoodInfoToOmSeckillGoods(OmSeckillGoodInfo omSeckillGoodInfo);

    List<SeckillGoodVo> omSeckillGoodsToSeckillGoodVoList(List<OmSeckillGoods> omSeckillGoods);

    List<OmSeckillGoods> seckillGoodVoToOmSeckillGoodsList(List<SeckillGoodVo> seckillGoodVo);
}
