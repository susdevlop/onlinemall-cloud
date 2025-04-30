package us.sushome.onlinemallcloud.omccommon.api.goods;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.saToken.FeignInterceptor;

import java.util.List;
import java.util.Map;

@FeignClient(value = "omc-goods-830x",path = "/internal/goods",configuration = FeignInterceptor.class)
public interface GoodsServiceApi{

    @GetMapping("/getGoodInfoBySkuIdFromFeign")
    GoodInfoVo getGoodInfoBySkuId(@RequestParam String skuId);

    @GetMapping("getOpeningSeckillGoodListFromFeign")
    List<SeckillGoodVo> getOpeningSeckillGoodList();

    @GetMapping("/getSeckillGoodInfoByIdFromFeign")
    SeckillGoodVo getSeckillGoodInfoById(@RequestParam String id);

    @PostMapping("/decrementGoodSkuInventoryByDecrActionListFromFeign")
    Boolean decrementGoodSkuInventoryByDecrActionList(@RequestBody List<Map<String,Object>> decrActionList);

    @GetMapping("/getSkuInventoryByIdFromFeign")
    Integer getSkuInventoryById(@RequestParam String id);

    @GetMapping("/getSeckillGoodInventoryByIdFromFeign")
    Integer getSeckillGoodInventoryById(@RequestParam String id);

    @GetMapping("/decrementSeckillGoodInventoryFromFeign")
    Boolean decrementSeckillGoodInventory(@RequestParam String id);

    @GetMapping("/incrementSeckillGoodInventoryFromFeign")
    Boolean incrementSeckillGoodInventory(@RequestParam String id);

    @PostMapping("/restoreStockByDecrActionListFromFeign")
    Boolean restoreStockByDecrActionList(@RequestBody List<Map<String,Object>> decrActionList);
}
