package us.sushome.onlinemallcloud.omcgoods830x.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.sushome.onlinemallcloud.omccommon.api.goods.GoodsServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omcgoods830x.service.OmGoodsMainService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/goods")
public class GoodsInternalController implements GoodsServiceApi {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    OmGoodsMainService omGoodsMainService;

    @GetMapping("/getGoodInfoBySkuIdFromFeign")
    @Override
    public GoodInfoVo getGoodInfoBySkuId(@RequestParam String skuId) {
        return omGoodsMainService.getGoodInfoBySkuId(skuId);
    }

    @GetMapping("getOpeningSeckillGoodListFromFeign")
    @Override
    public List<SeckillGoodVo> getOpeningSeckillGoodList() {
        return omGoodsMainService.getOpeningSeckillGoodList();
    }

    @GetMapping("/getSeckillGoodInfoByIdFromFeign")
    @Override
    public SeckillGoodVo getSeckillGoodInfoById(@RequestParam String id){
        return omGoodsMainService.getSeckillGoodInfoById(id);
    }

    @PostMapping("/decrementGoodSkuInventoryByDecrActionListFromFeign")
    @Override
    public Boolean decrementGoodSkuInventoryByDecrActionList(@RequestBody List<Map<String, Object>> decrActionList) {
        System.out.println("decrActionList:"+decrActionList);
        return omGoodsMainService.decrementGoodSkuInventoryByDecrActionList(decrActionList);
    }

    @GetMapping("/getSkuInventoryByIdFromFeign")
    @Override
    public Integer getSkuInventoryById(@RequestParam String id) {
        return omGoodsMainService.getSkuInventoryById(id);
    }

    @GetMapping("/getSeckillGoodInventoryByIdFromFeign")
    @Override
    public Integer getSeckillGoodInventoryById(@RequestParam String id) {
        return omGoodsMainService.getSeckillGoodInventoryById(id);
    }

    @GetMapping("/decrementSeckillGoodInventoryFromFeign")
    @Override
    public Boolean decrementSeckillGoodInventory(@RequestParam String id) {
        return omGoodsMainService.decrementSeckillGoodInventory(id);
    }

    @GetMapping("/incrementSeckillGoodInventoryFromFeign")
    @Override
    public Boolean incrementSeckillGoodInventory(@RequestParam String id) {
        return omGoodsMainService.incrementSeckillGoodInventory(id);
    }

    @PostMapping("/restoreStockByDecrActionListFromFeign")
    @Override
    public Boolean restoreStockByDecrActionList(@RequestBody List<Map<String, Object>> decrActionList) {
        return omGoodsMainService.restoreStockByDecrActionList(decrActionList);
    }
}
