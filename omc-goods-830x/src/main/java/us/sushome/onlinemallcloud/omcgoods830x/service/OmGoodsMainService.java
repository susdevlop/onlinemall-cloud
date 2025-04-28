package us.sushome.onlinemallcloud.omcgoods830x.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodsUniteSkuList;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoods;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoodsku;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmSeckillGoods;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OmGoodsMainService{
    private static Logger logger = LoggerFactory.getLogger(OmGoodsMainService.class);

    @Resource
    private IOmGoodsService omGoodsService;

    @Resource
    private IOmGoodskuService omGoodskuService;

    @Resource
    private IOmSeckillGoodsService omSeckillGoodsService;

    public IPage<GoodInfoVo> getGoodInfoList(String goodsName, Integer goodsType, Integer pageNum, Integer pageSize,Boolean skuNotEmpty) {
        IPage<OmGoodsUniteSkuList> goodsUniteSkuList = omGoodsService.getGoodsUniteSkuList(goodsName,goodsType,pageNum,pageSize,skuNotEmpty);
        //List<OmGoodInfo> omGoodsList = goodInfoList.getRecords();
        List<OmGoodsUniteSkuList> omGoodsUniteSkuLists = goodsUniteSkuList.getRecords();
        List<GoodInfoVo> goodInfoVoList = new ArrayList<>();
        for (OmGoodsUniteSkuList item : omGoodsUniteSkuLists) {
            OmGoods omGoods = omGoodsService.omGoodsUniteSkuListToOmGoods(item);
            GoodInfoVo goodInfoVo = omGoodsService.convertOmGoodsToGoodInfoVo(omGoods);
            List<OmGoodsku> omGoodSkuList = item.getSkuListAsObject();
            List<GoodSkuVo> skuList = omGoodskuService.convertOmGoodskuListToGoodSkuVoList(omGoodSkuList);
            goodInfoVo.setSkuList(skuList);
            goodInfoVoList.add(goodInfoVo);
        }
        IPage<GoodInfoVo> iPageGoodInfoVo = new Page<>();
        iPageGoodInfoVo.setRecords(goodInfoVoList);
        iPageGoodInfoVo.setTotal(goodsUniteSkuList.getTotal());
        iPageGoodInfoVo.setSize(goodsUniteSkuList.getSize());
        iPageGoodInfoVo.setCurrent(goodsUniteSkuList.getCurrent());
        return iPageGoodInfoVo;
    }

    public GoodInfoVo getGoodInfoById(String id,Boolean skuNotEmpty) {
        GoodInfoVo goodInfoVo = null;
        List<OmGoodInfo> list = omGoodsService.getGoodInfoById(id);
        if(!list.isEmpty()){
            OmGoodInfo firstItem = list.get(0);
            OmGoods omGoods = omGoodsService.omGoodInfoToOmGoods(firstItem);
            goodInfoVo = omGoodsService.convertOmGoodsToGoodInfoVo(omGoods);
            List<GoodSkuVo> skuList = new ArrayList<>();
            for(OmGoodInfo item : list){
                if(!StringUtils.isVoid(item.getOmGoodskuId())){
                    OmGoodsku omGoodsku = omGoodskuService.omGoodInfoToOmGoodsku(item);
                    GoodSkuVo skuItem = omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
                    skuList.add(skuItem);
                }
            }
            goodInfoVo.setSkuList(skuList);
        }
        if(goodInfoVo != null && skuNotEmpty){//sku为空时返回空列表
            if(goodInfoVo.getSkuList().isEmpty()){
                return null;
            }
        }
        return goodInfoVo;
    }

    public GoodInfoVo getGoodInfoBySkuId(String skuId) {
        OmGoodInfo goodInfo = omGoodsService.getGoodInfoBySkuId(skuId);
        if(goodInfo!=null){
            OmGoods omGoods = omGoodsService.omGoodInfoToOmGoods(goodInfo);
            GoodInfoVo goodInfoVo = omGoodsService.convertOmGoodsToGoodInfoVo(omGoods);
            List<GoodSkuVo> skuList = new ArrayList<>();
            OmGoodsku omGoodsku = omGoodskuService.omGoodInfoToOmGoodsku(goodInfo);
            GoodSkuVo skuItem = omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
            skuList.add(skuItem);
            goodInfoVo.setSkuList(skuList);
            return goodInfoVo;
        }
        return null;
    }

    public GoodSkuVo getGoodSkuBySkuId(String skuId) {
        QueryWrapper<OmGoodsku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", skuId);
        OmGoodsku omGoodsku = omGoodskuService.getOne(queryWrapper);
        return omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
    }

    public CodeMsg updateGoodInfo(GoodInfoVo goodInfo) {
        goodInfo.setUpdateTime(LocalDateTime.now());
        boolean isSuccess = omGoodsService.updateById(omGoodsService.convertGoodInfoVoToOmGoods(goodInfo));
        return isSuccess ? CodeMsg.SUCCESS : CodeMsg.PRAMS_ERROR;
    }

    public CodeMsg addGood(GoodInfoVo goodInfo) {
        goodInfo.setGoodsId(UUID.randomUUID().toString().replace("-",""));
        goodInfo.setAddTime(LocalDateTime.now());
        goodInfo.setUpdateTime(LocalDateTime.now());
        boolean isSuccess = omGoodsService.save(omGoodsService.convertGoodInfoVoToOmGoods(goodInfo));
        return isSuccess ? CodeMsg.SUCCESS : CodeMsg.PRAMS_ERROR;
    }

    public CodeMsg removeGood(String uuid) {
        QueryWrapper<OmGoodsku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("om_goodsku_goodsid", uuid);
        Boolean isRemoveSkuSuccess = omGoodskuService.remove(queryWrapper);
        if(isRemoveSkuSuccess){
            boolean isSuccess = omGoodsService.removeById(uuid);
            return isSuccess ? CodeMsg.SUCCESS : CodeMsg.PRAMS_ERROR;
        }else{
            return CodeMsg.PRAMS_ERROR;
        }
    }

    public GoodSkuVo addGoodSku(GoodSkuVo goodSkuVo) {
        goodSkuVo.setId(UUID.randomUUID().toString().replace("-",""));
        goodSkuVo.setAddTime(LocalDateTime.now());
        goodSkuVo.setUpdateTime(LocalDateTime.now());
        OmGoodsku omGoodsku = omGoodskuService.skuVoToModel(goodSkuVo);
        boolean isSuccess = omGoodskuService.save(omGoodsku);
        if(isSuccess){
            return omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
        }else{
            return null;
        }
    }

    public GoodSkuVo updateGoodSku(GoodSkuVo goodSkuVo) {
        goodSkuVo.setUpdateTime(LocalDateTime.now());
        OmGoodsku omGoodsku = omGoodskuService.skuVoToModel(goodSkuVo);
        boolean isSuccess = omGoodskuService.updateById(omGoodsku);
        if(isSuccess){
            return omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
        }else{
            return null;
        }
    }

    public CodeMsg removeGoodSku(String uuid) {
        boolean isSuccess = omGoodskuService.removeById(uuid);
        return isSuccess ? CodeMsg.SUCCESS : CodeMsg.PRAMS_ERROR;
    }

    public Boolean checkSeckillSkuIllegal(SeckillGoodVo seckillGoodVo,Boolean isEdit){
        QueryWrapper<OmSeckillGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("om_osg_goodskuid",seckillGoodVo.getGoodSkuId());
        Long sameSeckillGoodCount = omSeckillGoodsService.count(queryWrapper);
        if(isEdit){
            if(sameSeckillGoodCount != 1){
                return false;
            }
        }else{
            if(sameSeckillGoodCount > 0){
                return false;
            }
        }
        OmGoodsku skuInfo = omGoodskuService.getById(seckillGoodVo.getGoodSkuId());
        if(skuInfo == null){
            return false;
        }else if(skuInfo.getOmGoodskuInventory() < seckillGoodVo.getStock()){
            return false;
        }
        return true;
    }

    public GoodInfoVo getSeckillGoodInfo(OmSeckillGoods omSeckillGoods){
        GoodInfoVo goodInfoVo = getGoodInfoById(omSeckillGoods.getOmOsgGoodid(),false);
        List<GoodSkuVo> skuList = new ArrayList<>();
        goodInfoVo.getSkuList().forEach(item -> {
            if(item.getId().equals(omSeckillGoods.getOmOsgGoodskuid())){
                skuList.add(item);
            }
        });
        goodInfoVo.setSkuList(skuList);
        return goodInfoVo;
    }


    public SeckillGoodVo addSeckillGood(SeckillGoodVo seckillGoodVo) {
        seckillGoodVo.setId(UUID.randomUUID().toString().replace("-",""));
        seckillGoodVo.setAddTime(LocalDateTime.now());
        seckillGoodVo.setUpdateTime(LocalDateTime.now());
        if(StringUtils.isVoid(seckillGoodVo.getGoodId()) || StringUtils.isVoid(seckillGoodVo.getGoodSkuId())){
            return null;
        }
        if(!checkSeckillSkuIllegal(seckillGoodVo,false)){
            return null;
        }

        OmSeckillGoods omSeckillGoods = omSeckillGoodsService.seckillVoToModel(seckillGoodVo);
        boolean isSuccess = omSeckillGoodsService.save(omSeckillGoods);
        if(isSuccess){
            GoodInfoVo goodInfoVo = getSeckillGoodInfo(omSeckillGoods);
            SeckillGoodVo result = omSeckillGoodsService.convertOmSeckillGoodsToSeckillGoodVo(omSeckillGoods);
            result.setGoodInfo(goodInfoVo);
            return result;
        }else{
            return null;
        }
    }

    public SeckillGoodVo updateSeckillGood(SeckillGoodVo seckillGoodVo) {
        if(StringUtils.isVoid(seckillGoodVo.getGoodId()) || StringUtils.isVoid(seckillGoodVo.getGoodSkuId())){
            return null;
        }
        if(!checkSeckillSkuIllegal(seckillGoodVo,true)){
            return null;
        }
        OmSeckillGoods omSeckillGoods = omSeckillGoodsService.seckillVoToModel(seckillGoodVo);
        boolean isSuccess = omSeckillGoodsService.updateById(omSeckillGoods);
        if(isSuccess){
            GoodInfoVo goodInfoVo = getSeckillGoodInfo(omSeckillGoods);
            SeckillGoodVo result = omSeckillGoodsService.convertOmSeckillGoodsToSeckillGoodVo(omSeckillGoods);
            result.setGoodInfo(goodInfoVo);
            return result;
        }else{
            return null;
        }
    }

    public CodeMsg removeSeckillGood(String uuid) {
        boolean isSuccess = omSeckillGoodsService.removeById(uuid);
        return isSuccess ? CodeMsg.SUCCESS : CodeMsg.PRAMS_ERROR;
    }

    public List<SeckillGoodVo> getSKListBySKInfoList(List<OmSeckillGoodInfo> list){
        List<SeckillGoodVo> resultList = new ArrayList<>();
        for(OmSeckillGoodInfo item : list){
            OmSeckillGoods omSeckillGoods = omSeckillGoodsService.convertOmSeckillGoodInfoToOmSeckillGoods(item);
            SeckillGoodVo seckillGoodVo = omSeckillGoodsService.convertOmSeckillGoodsToSeckillGoodVo(omSeckillGoods);
            OmGoods omGoods = omGoodsService.convertOmSeckillGoodInfoVoToOmGoods(item);
            GoodInfoVo goodInfoVo = omGoodsService.convertOmGoodsToGoodInfoVo(omGoods);
            OmGoodsku omGoodsku = omGoodskuService.convertOmSeckillGoodInfoToOmGoodsku(item);
            GoodSkuVo goodSkuVo = omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
            List<GoodSkuVo> skuList = new ArrayList<>();
            if(StringUtils.isNotEmpty(goodSkuVo.getId())){
                skuList.add(goodSkuVo);
            }
            goodInfoVo.setSkuList(skuList);
            seckillGoodVo.setGoodInfo(goodInfoVo);
            resultList.add(seckillGoodVo);
        }
        return resultList;
    }

    public IPage<SeckillGoodVo> getSeckillGoodList(String goodsName, Integer goodsType, Integer pageNum, Integer pageSize,Boolean showAll) {
        IPage<OmSeckillGoodInfo> pageList = omSeckillGoodsService.getSeckillGoodsList(goodsName,goodsType,pageNum,pageSize,showAll);
        List<OmSeckillGoodInfo> list = pageList.getRecords();
        List<SeckillGoodVo> resultList = getSKListBySKInfoList(list);
        IPage<SeckillGoodVo> iPageSeckillGoodVo = new Page<>();
        iPageSeckillGoodVo.setRecords(resultList);
        iPageSeckillGoodVo.setTotal(pageList.getTotal());
        iPageSeckillGoodVo.setSize(pageList.getSize());
        iPageSeckillGoodVo.setCurrent(pageList.getCurrent());
        iPageSeckillGoodVo.setPages(pageList.getPages());
        return iPageSeckillGoodVo;
    }

    public List<SeckillGoodVo> getOpeningSeckillGoodList() {
        List<OmSeckillGoodInfo> list = omSeckillGoodsService.getOpeningSeckillGoodList();
        return getSKListBySKInfoList(list);
    }

    public SeckillGoodVo getSeckillGoodInfoById(String id) {
        OmSeckillGoodInfo omSeckillGoodInfo = omSeckillGoodsService.getSeckillGoodInfoById(id);
        OmSeckillGoods omSeckillGoods = omSeckillGoodsService.convertOmSeckillGoodInfoToOmSeckillGoods(omSeckillGoodInfo);
        SeckillGoodVo seckillGoodVo = omSeckillGoodsService.convertOmSeckillGoodsToSeckillGoodVo(omSeckillGoods);
        OmGoods omGoods = omGoodsService.convertOmSeckillGoodInfoVoToOmGoods(omSeckillGoodInfo);
        GoodInfoVo goodInfoVo = omGoodsService.convertOmGoodsToGoodInfoVo(omGoods);
        OmGoodsku omGoodsku = omGoodskuService.convertOmSeckillGoodInfoToOmGoodsku(omSeckillGoodInfo);
        GoodSkuVo goodSkuVo = omGoodskuService.convertOmGoodskuToGoodSkuVo(omGoodsku);
        List<GoodSkuVo> skuList = new ArrayList<>();
        if(StringUtils.isNotEmpty(goodSkuVo.getId())){
            skuList.add(goodSkuVo);
        }
        goodInfoVo.setSkuList(skuList);
        seckillGoodVo.setGoodInfo(goodInfoVo);
        return seckillGoodVo;
    }

    public Boolean decrementGoodSkuInventory(String skuId, Integer num) {
        LambdaUpdateWrapper<OmGoodsku> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OmGoodsku::getOmGoodskuId,skuId)
                .ge(OmGoodsku::getOmGoodskuInventory,num)
                .setSql("om_goodsku_inventory = om_goodsku_inventory - " + num);
        return omGoodskuService.update(updateWrapper);
    }

    //根据decrActionList一次性扣减库存，不考虑 version
    @Transactional
    public Boolean decrementGoodSkuInventoryByDecrActionList(List<Map<String,Object>> decrActionList){
        boolean updateResult = true;
        for (Map<String, Object> stringObjectMap : decrActionList) {
            if (updateResult) {
                String skuId = (String) stringObjectMap.get("skuId");
                Integer count = (Integer) stringObjectMap.get("count");
                Boolean result = decrementGoodSkuInventory(skuId, count);
                if (!result) {//失败更新
                    updateResult = false;
                }
            }
        }
        if(!updateResult){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return updateResult;
    }

    //获取普通商品 sku 库存
    public Integer getSkuInventoryById(String id){
        OmGoodsku omGoodsku = omGoodskuService.getById(id);
        if(omGoodsku!=null){
            return omGoodsku.getOmGoodskuInventory();
        }else{
            return null;
        }
    }
    //获取抢购商品库存
    public Integer getSeckillGoodInventoryById(String id){
        OmSeckillGoods omSeckillGoods = omSeckillGoodsService.getById(id);
        if(omSeckillGoods!=null){
            return omSeckillGoods.getOmOsgStock();
        }else{
            return null;
        }
    }

    //抢购商品减扣库存
    @Transactional
    public Boolean decrementSeckillGoodInventory(String id){
        LambdaUpdateWrapper<OmSeckillGoods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OmSeckillGoods::getOmOsgId,id)
               .ge(OmSeckillGoods::getOmOsgStock,1)
               .setSql("om_osg_stock = om_osg_stock - " + 1);
        Boolean result = omSeckillGoodsService.update(updateWrapper);
        if(!result){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    //抢购商品库存加1
    @Transactional
    public Boolean incrementSeckillGoodInventory(String id){
        OmSeckillGoods omSeckillGoods = omSeckillGoodsService.getById(id);
        LambdaUpdateWrapper<OmSeckillGoods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OmSeckillGoods::getOmOsgId,id)
                .eq(OmSeckillGoods::getOmOsgVersion,omSeckillGoods.getOmOsgVersion())
                .set(OmSeckillGoods::getOmOsgStock, omSeckillGoods.getOmOsgStock() + 1)
                .set(OmSeckillGoods::getOmOsgVersion, omSeckillGoods.getOmOsgVersion() + 1);
        return omSeckillGoodsService.update(updateWrapper);
    }

    public Boolean incrementGoodSkuInventory(String skuId, Integer num) {
        OmGoodsku omGoodsku = omGoodskuService.getById(skuId);
        LambdaUpdateWrapper<OmGoodsku> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OmGoodsku::getOmGoodskuId,skuId)
                .eq(OmGoodsku::getOmGoodskuVersion,omGoodsku.getOmGoodskuVersion())
                .set(OmGoodsku::getOmGoodskuInventory, omGoodsku.getOmGoodskuInventory() + num)
                .set(OmGoodsku::getOmGoodskuVersion, omGoodsku.getOmGoodskuVersion() + 1);
        return omGoodskuService.update(updateWrapper);
    }

    //恢复普通订单库存
    @Transactional
    public Boolean restoreStockByDecrActionList(List<Map<String,Object>> decrActionList) {
        boolean updateResult = true;
        for (Map<String, Object> stringObjectMap : decrActionList) {
            if (updateResult) {
                String skuId = (String) stringObjectMap.get("skuId");
                Integer count = (Integer) stringObjectMap.get("count");
                Boolean result = incrementGoodSkuInventory(skuId, count);
                if (!result) {//失败更新
                    updateResult = false;
                }
            }
        }
        if(!updateResult){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return updateResult;
    }
}
