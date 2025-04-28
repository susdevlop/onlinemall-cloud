package us.sushome.onlinemallcloud.omcgoods830x.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.sushome.onlinemallcloud.omccommon.api.goods.GoodsServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.result.Result;
import us.sushome.onlinemallcloud.omcgoods830x.service.OmGoodsMainService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/openApi/goods")
public class GoodsController implements GoodsServiceApi {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    OmGoodsMainService omGoodsMainService;

    public IPage<GoodInfoVo> getGoodInfoListByBody(String body,Boolean skuNotEmpty){
        JSONObject jsonObject = JSONObject.parseObject(body);
        String goodsName = jsonObject.getString("goodsName");
        Integer goodsType = jsonObject.getInteger("goodsType");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        if(pageNum == null) {
            pageNum = 1;
        }
        if(pageSize == null) {
            pageSize = 10;
        }
        return omGoodsMainService.getGoodInfoList(goodsName, goodsType, pageNum, pageSize,skuNotEmpty);
    }

    @PostMapping("/getGoodInfoList")
    public Result<Map<String, Object>> getGoodInfoList(@RequestBody String body) {
        IPage<GoodInfoVo> goodInfoVoIPage = getGoodInfoListByBody(body,true);
        return Result.list(goodInfoVoIPage);
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/getAllGoodInfoList")
    public Result<Map<String, Object>> getAllGoodInfoList(@RequestBody String body) {
        IPage<GoodInfoVo> goodInfoVoIPage = getGoodInfoListByBody(body,false);
        return Result.list(goodInfoVoIPage);
    }

    @PostMapping("/getGoodInfoById")
    public Result<GoodInfoVo> getGoodInfoById(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("id");
        if(id == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.getGoodInfoById(id,true));
        }
    }
    //未使用
    @SaCheckPermission("/openApi/goods")
    @PostMapping("/getAllGoodInfoById")
    public Result<GoodInfoVo> getAllGoodInfoById(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("id");
        if(id == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.getGoodInfoById(id,false));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/updateGoodInfo")
    public Result<Boolean> updateGoodInfo(@RequestBody GoodInfoVo goodInfoVo) {
        if(goodInfoVo == null || goodInfoVo.getGoodsId() == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.updateGoodInfo(goodInfoVo));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/addGood")
    public Result<Boolean> addGood(@RequestBody GoodInfoVo goodInfoVo) {
        if(goodInfoVo == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.addGood(goodInfoVo));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/removeGood")
    public Result<Boolean> removeGood(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String goodsId = jsonObject.getString("goodsId");
        if(goodsId == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.removeGood(goodsId));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/addGoodSku")
    public Result<GoodSkuVo> addGoodSku(@RequestBody GoodSkuVo goodSkuVo) {
        if(goodSkuVo == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.addGoodSku(goodSkuVo));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/updateGoodSku")
    public Result<GoodSkuVo> updateGoodSku(@RequestBody GoodSkuVo goodSkuVo) {
        if(goodSkuVo == null || goodSkuVo.getGoodsId() == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.updateGoodSku(goodSkuVo));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/removeGoodSku")
    public Result<Boolean> removeGoodSku(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String goodsId = jsonObject.getString("goodsSkuId");
        if(goodsId == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.removeGoodSku(goodsId));
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/addSeckillGood")
    public Result<SeckillGoodVo> addSeckillGood(@RequestBody SeckillGoodVo seckillGoodVo) {
        if(seckillGoodVo == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            SeckillGoodVo result = omGoodsMainService.addSeckillGood(seckillGoodVo);
            if(result == null) {
                return Result.error(CodeMsg.PRAMS_ERROR);
            }else{
                return Result.success(result);
            }
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/updateSeckillGood")
    public Result<SeckillGoodVo> updateSeckillGood(@RequestBody SeckillGoodVo seckillGoodVo) {
        if(seckillGoodVo == null || seckillGoodVo.getId() == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            SeckillGoodVo result = omGoodsMainService.updateSeckillGood(seckillGoodVo);
            if(result == null) {
                return Result.error(CodeMsg.PRAMS_ERROR);
            }else{
                return Result.success(result);
            }
        }
    }

    @SaCheckPermission("/openApi/goods")
    @PostMapping("/removeSeckillGood")
    public Result<Boolean> removeSeckillGood(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("id");
        if(id == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.removeSeckillGood(id));
        }
    }

    public IPage<SeckillGoodVo> getSeckillGoodListByBody(String body,Boolean showAll){
        JSONObject jsonObject = JSONObject.parseObject(body);
        String goodsName = jsonObject.getString("goodsName");
        Integer goodsType = jsonObject.getInteger("goodsType");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return omGoodsMainService.getSeckillGoodList(goodsName, goodsType, pageNum, pageSize,showAll);
    }

    @PostMapping("/getSeckillGoodList")
    public Result<Map<String, Object>> getSeckillGoodList(@RequestBody String body) {
        IPage<SeckillGoodVo> list = getSeckillGoodListByBody(body,false);
        return Result.list(list);
    }

    @PostMapping("/getAllSeckillGoodList")
    public Result<Map<String, Object>> getAllSeckillGoodList(@RequestBody String body) {
        IPage<SeckillGoodVo> list = getSeckillGoodListByBody(body,true);
        return Result.list(list);
    }

    @PostMapping("/getSeckillGoodInfoById")
    public Result<SeckillGoodVo> getSeckillGoodInfoByHttpId(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("seckillId");
        if(id == null) {
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omGoodsMainService.getSeckillGoodInfoById(id));
        }
    }

    @GetMapping("/getGoodInfoBySkuIdFromFeign")
    @Override
    public GoodInfoVo getGoodInfoBySkuId(String skuId) {
        return omGoodsMainService.getGoodInfoBySkuId(skuId);
    }

    @GetMapping("getOpeningSeckillGoodListFromFeign")
    @Override
    public List<SeckillGoodVo> getOpeningSeckillGoodList() {
        return omGoodsMainService.getOpeningSeckillGoodList();
    }

    @GetMapping("/getSeckillGoodInfoByIdFromFeign")
    @Override
    public SeckillGoodVo getSeckillGoodInfoById(String id){
        return omGoodsMainService.getSeckillGoodInfoById(id);
    }

    @PostMapping("/decrementGoodSkuInventoryByDecrActionListFromFeign")
    @Override
    public Boolean decrementGoodSkuInventoryByDecrActionList(List<Map<String, Object>> decrActionList) {
        return omGoodsMainService.decrementGoodSkuInventoryByDecrActionList(decrActionList);
    }

    @GetMapping("/getSkuInventoryByIdFromFeign")
    @Override
    public Integer getSkuInventoryById(String id) {
        return omGoodsMainService.getSkuInventoryById(id);
    }

    @GetMapping("/getSeckillGoodInventoryByIdFromFeign")
    @Override
    public Integer getSeckillGoodInventoryById(String id) {
        return omGoodsMainService.getSeckillGoodInventoryById(id);
    }

    @GetMapping("/decrementSeckillGoodInventoryFromFeign")
    @Override
    public Boolean decrementSeckillGoodInventory(String id) {
        return omGoodsMainService.decrementSeckillGoodInventory(id);
    }

    @GetMapping("/incrementSeckillGoodInventoryFromFeign")
    @Override
    public Boolean incrementSeckillGoodInventory(String id) {
        return omGoodsMainService.incrementSeckillGoodInventory(id);
    }

    @PostMapping("/restoreStockByDecrActionListFromFeign")
    @Override
    public Boolean restoreStockByDecrActionList(List<Map<String, Object>> decrActionList) {
        return omGoodsMainService.restoreStockByDecrActionList(decrActionList);
    }
}
