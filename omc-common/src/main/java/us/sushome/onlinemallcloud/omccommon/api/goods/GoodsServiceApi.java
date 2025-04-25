package us.sushome.onlinemallcloud.omccommon.api.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;

import java.util.List;
import java.util.Map;

public interface GoodsServiceApi{

    IPage<GoodInfoVo> getGoodInfoList(String goodsName, Integer goodsType, Integer pageNum, Integer pageSize,Boolean skuNotEmpty);

    GoodInfoVo getGoodInfoById(String id,Boolean skuNotEmpty);

    GoodInfoVo getGoodInfoBySkuId(String skuId);

    GoodSkuVo getGoodSkuBySkuId(String skuId);

    CodeMsg updateGoodInfo(GoodInfoVo goodInfo);

    CodeMsg addGood(GoodInfoVo goodInfo);

    CodeMsg removeGood(String uuid);

    GoodSkuVo addGoodSku(GoodSkuVo goodSkuVo);

    GoodSkuVo updateGoodSku(GoodSkuVo goodSkuVo);

    CodeMsg removeGoodSku(String uuid);

    SeckillGoodVo addSeckillGood(SeckillGoodVo seckillGoodVo);

    SeckillGoodVo updateSeckillGood(SeckillGoodVo seckillGoodVo);

    CodeMsg removeSeckillGood(String uuid);

    IPage<SeckillGoodVo> getSeckillGoodList(String goodsName, Integer goodsType,Integer pageNum, Integer pageSize,Boolean showAll);

    List<SeckillGoodVo> getOpeningSeckillGoodList();

    SeckillGoodVo getSeckillGoodInfoById(String id);

    @Transactional
    Boolean decrementGoodSkuInventoryByDecrActionList(List<Map<String,Object>> decrActionList);

    Integer getSkuInventoryById(String id);

    Integer getSeckillGoodInventoryById(String id);

    @Transactional
    Boolean decrementSeckillGoodInventory(String id);

    Boolean incrementSeckillGoodInventory(String id);

    @Transactional
    Boolean restoreStockByDecrActionList(List<Map<String,Object>> decrActionList);
}
