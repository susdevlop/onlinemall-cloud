package us.sushome.onlinemallcloud.omcgoods830x.service;

import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmGoodskuToGoodSkuVoMapper;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoodsku;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmGoodskuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * $!{table.comment} 服务类
 * </p>
 *
 * @author sushome
 * @since 2025-04-14
 */
@Service
public class IOmGoodskuService extends ServiceImpl<OmGoodskuMapper, OmGoodsku> {
    private static Logger logger = LoggerFactory.getLogger(IOmGoodskuService.class);

    @Resource
    private OmGoodskuToGoodSkuVoMapper omGoodskuToGoodSkuVoMapper;

    public GoodSkuVo convertOmGoodskuToGoodSkuVo(OmGoodsku omGoodsku) {
        return omGoodskuToGoodSkuVoMapper.OmGoodskuToGoodSkuVo(omGoodsku);
    }

    public List<GoodSkuVo> convertOmGoodskuListToGoodSkuVoList(List<OmGoodsku> omGoodskuList) {
        return omGoodskuToGoodSkuVoMapper.OmGoodskuToGoodSkuVoList(omGoodskuList);
    }

    public OmGoodsku convertOmSeckillGoodInfoToOmGoodsku(OmSeckillGoodInfo omSeckillGoodInfo){
        return omGoodskuToGoodSkuVoMapper.OmSeckillGoodInfoToOmGoodsku(omSeckillGoodInfo);
    }

    public OmGoodsku skuVoToModel(GoodSkuVo goodSkuVo){
        OmGoodsku omGoodsku = new OmGoodsku();
        if(goodSkuVo != null){
            if(!StringUtils.isVoid(goodSkuVo.getId())){
                omGoodsku.setOmGoodskuId(goodSkuVo.getId());
            }else{
                omGoodsku.setOmGoodskuId(null);
            }
            omGoodsku.setOmGoodskuGoodsid(goodSkuVo.getGoodsId());
            omGoodsku.setOmGoodskuName(goodSkuVo.getName());
            omGoodsku.setOmGoodskuPrice(goodSkuVo.getPrice());
            omGoodsku.setOmGoodskuInventory(goodSkuVo.getInventory());
            omGoodsku.setOmGoodskuMedia(goodSkuVo.getMedia());
            omGoodsku.setOmGoodskuUpdatetime(goodSkuVo.getUpdateTime());
            omGoodsku.setOmGoodskuAddtime(goodSkuVo.getAddTime());
            omGoodsku.setOmGoodskuVersion(goodSkuVo.getVersion());
        }
        return omGoodsku;
    }

    public OmGoodsku omGoodInfoToOmGoodsku(OmGoodInfo item){
        OmGoodsku currentSku = new OmGoodsku();
        currentSku.setOmGoodskuId(item.getOmGoodskuId());
        currentSku.setOmGoodskuGoodsid(item.getOmGoodskuGoodsid());
        currentSku.setOmGoodskuName(item.getOmGoodskuName());
        currentSku.setOmGoodskuPrice(item.getOmGoodskuPrice());
        currentSku.setOmGoodskuInventory(item.getOmGoodskuInventory());
        currentSku.setOmGoodskuMedia(item.getOmGoodskuMedia());
        currentSku.setOmGoodskuUpdatetime(item.getOmGoodskuUpdatetime());
        currentSku.setOmGoodskuAddtime(item.getOmGoodskuAddtime());
        currentSku.setOmGoodskuVersion(item.getOmGoodskuVersion());
        return currentSku;
    }
}