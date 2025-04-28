package us.sushome.onlinemallcloud.omcgoods830x.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmGoodsMainMapper;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmGoodsToGoodInfoVoMapper;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodsUniteSkuList;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoods;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmGoodsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * $!{table.comment} 服务类
 * </p>
 *
 * @author sushome
 * @since 2025-02-21
 */
@Service
public class IOmGoodsService extends ServiceImpl<OmGoodsMapper, OmGoods> {
    private static Logger logger = LoggerFactory.getLogger(IOmGoodsService.class);

    @Resource
    private OmGoodsMainMapper omGoodsMainMapper;

    @Resource
    private OmGoodsToGoodInfoVoMapper omGoodsToGoodInfoVoMapper;

    public GoodInfoVo convertOmGoodsToGoodInfoVo(OmGoods omGoods) {
        return omGoodsToGoodInfoVoMapper.omGoodsToGoodInfoVo(omGoods);
    }

    public OmGoods convertGoodInfoVoToOmGoods(GoodInfoVo goodInfoVo) {
        return omGoodsToGoodInfoVoMapper.omGoodInfoVoToOmGoods(goodInfoVo);
    }

    public List<GoodInfoVo> convertOmGoodsListToGoodInfoVoList(List<OmGoods> omGoodsList) {
        return omGoodsToGoodInfoVoMapper.omGoodsToGoodInfoVoList(omGoodsList);
    }

    public OmGoods convertOmSeckillGoodInfoVoToOmGoods(OmSeckillGoodInfo omSeckillGoodInfo) {
        return omGoodsToGoodInfoVoMapper.omSeckillGoodInfoToOmGoods(omSeckillGoodInfo);
    }

    private OmGoods getOmGoods(String omGoodsId, String omGoodsName, Integer omGoodsType, String omGoodsMedialist, String omGoodsContent, LocalDateTime omGoodsAddtime, LocalDateTime omGoodsUpdatetime, String omGoodsShipaddress) {
        OmGoods omGoods = new OmGoods();
        omGoods.setOmGoodsId(omGoodsId);
        omGoods.setOmGoodsName(omGoodsName);
        omGoods.setOmGoodsType(omGoodsType);
        omGoods.setOmGoodsMedialist(omGoodsMedialist);
        omGoods.setOmGoodsContent(omGoodsContent);
        omGoods.setOmGoodsAddtime(omGoodsAddtime);
        omGoods.setOmGoodsUpdatetime(omGoodsUpdatetime);
        omGoods.setOmGoodsShipaddress(omGoodsShipaddress);
        return omGoods;
    }

    public OmGoods omGoodsUniteSkuListToOmGoods(OmGoodsUniteSkuList item) {
        return getOmGoods(item.getOmGoodsId(), item.getOmGoodsName(), item.getOmGoodsType(), item.getOmGoodsMedialist(), item.getOmGoodsContent(), item.getOmGoodsAddtime(), item.getOmGoodsUpdatetime(), item.getOmGoodsShipaddress());
    }

    public OmGoods omGoodInfoToOmGoods(OmGoodInfo item) {
        return getOmGoods(item.getOmGoodsId(), item.getOmGoodsName(), item.getOmGoodsType(), item.getOmGoodsMedialist(), item.getOmGoodsContent(), item.getOmGoodsAddtime(), item.getOmGoodsUpdatetime(), item.getOmGoodsShipaddress());
    }

    public IPage<OmGoodsUniteSkuList> getGoodsUniteSkuList(String goodsName,Integer goodsType,int pageNum, int pageSize,Boolean skuNotEmpty) {
        Page<OmGoodInfo> page = new Page<>(pageNum, pageSize);
        return omGoodsMainMapper.getGoodsUniteSkuList(page, goodsName, goodsType,skuNotEmpty);
    }

    public List<OmGoodInfo> getGoodInfoById(String id){
        return omGoodsMainMapper.getGoodInfoById(id);
    }

    public OmGoodInfo getGoodInfoBySkuId(String skuId) {
        return omGoodsMainMapper.getGoodInfoBySkuId(skuId);
    }
}