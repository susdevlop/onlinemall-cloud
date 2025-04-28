package us.sushome.onlinemallcloud.omccommon.api.goods;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "omc-goods-830x",path = "/openApi/goods")
public interface GoodsServiceApi{

    @GetMapping("/getGoodInfoBySkuIdFromFeign")
    GoodInfoVo getGoodInfoBySkuId(String skuId);

    @GetMapping("getOpeningSeckillGoodListFromFeign")
    List<SeckillGoodVo> getOpeningSeckillGoodList();

    @GetMapping("/getSeckillGoodInfoByIdFromFeign")
    SeckillGoodVo getSeckillGoodInfoById(String id);

    @Transactional
    @PostMapping("/decrementGoodSkuInventoryByDecrActionListFromFeign")
    Boolean decrementGoodSkuInventoryByDecrActionList(List<Map<String,Object>> decrActionList);

    @GetMapping("/getSkuInventoryByIdFromFeign")
    Integer getSkuInventoryById(String id);

    @GetMapping("/getSeckillGoodInventoryByIdFromFeign")
    Integer getSeckillGoodInventoryById(String id);

    @Transactional
    @GetMapping("/decrementSeckillGoodInventoryFromFeign")
    Boolean decrementSeckillGoodInventory(String id);

    @GetMapping("/incrementSeckillGoodInventoryFromFeign")
    Boolean incrementSeckillGoodInventory(String id);

    @Transactional
    @PostMapping("/restoreStockByDecrActionListFromFeign")
    Boolean restoreStockByDecrActionList(List<Map<String,Object>> decrActionList);
}
