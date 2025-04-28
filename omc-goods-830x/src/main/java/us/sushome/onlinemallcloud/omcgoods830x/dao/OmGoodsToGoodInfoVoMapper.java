package us.sushome.onlinemallcloud.omcgoods830x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoods;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OmGoodsToGoodInfoVoMapper {


    @Mapping(target = "goodsId", source = "omGoodsId")
    @Mapping(target = "name", source = "omGoodsName")
    @Mapping(target = "goodsType", source = "omGoodsType")
    @Mapping(target = "mediaList", source = "omGoodsMedialist")
    @Mapping(target = "content", source = "omGoodsContent")
    @Mapping(target = "addTime", source = "omGoodsAddtime")
    @Mapping(target = "updateTime", source = "omGoodsUpdatetime")
    @Mapping(target = "shipAddress", source = "omGoodsShipaddress")
    GoodInfoVo omGoodsToGoodInfoVo(OmGoods omGoods);

    @Mapping(target = "omGoodsId", source = "goodsId")
    @Mapping(target = "omGoodsName", source = "name")
    @Mapping(target = "omGoodsType", source = "goodsType")
    @Mapping(target = "omGoodsMedialist", source = "mediaList")
    @Mapping(target = "omGoodsContent", source = "content")
    @Mapping(target = "omGoodsAddtime", source = "addTime")
    @Mapping(target = "omGoodsUpdatetime", source = "updateTime")
    @Mapping(target = "omGoodsShipaddress", source = "shipAddress")
    OmGoods omGoodInfoVoToOmGoods(GoodInfoVo goodInfoVo);

    OmGoods omSeckillGoodInfoToOmGoods(OmSeckillGoodInfo omSeckillGoodInfo);

    List<OmGoods> goodInfoVoListToOmGoodsList(List<GoodInfoVo> goodInfoVoList);

    List<GoodInfoVo> omGoodsToGoodInfoVoList(List<OmGoods> omGoodsList);
}
