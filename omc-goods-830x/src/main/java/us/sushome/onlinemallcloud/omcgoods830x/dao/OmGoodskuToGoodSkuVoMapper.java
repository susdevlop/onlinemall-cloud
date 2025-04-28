package us.sushome.onlinemallcloud.omcgoods830x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoodsku;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OmGoodskuToGoodSkuVoMapper {

    @Mapping(target = "id", source = "omGoodskuId")
    @Mapping(target = "goodsId", source = "omGoodskuGoodsid")
    @Mapping(target = "name", source = "omGoodskuName")
    @Mapping(target = "price", source = "omGoodskuPrice")
    @Mapping(target = "inventory", source = "omGoodskuInventory")
    @Mapping(target = "media", source = "omGoodskuMedia")
    @Mapping(target = "addTime", source = "omGoodskuAddtime")
    @Mapping(target = "updateTime", source = "omGoodskuUpdatetime")
    @Mapping(target = "version", source = "omGoodskuVersion")
    GoodSkuVo OmGoodskuToGoodSkuVo(OmGoodsku omGoodsku);

    OmGoodsku OmSeckillGoodInfoToOmGoodsku(OmSeckillGoodInfo omSeckillGoodInfo);

    List<GoodSkuVo> OmGoodskuToGoodSkuVoList(List<OmGoodsku> omGoodskus);
}
