package us.sushome.onlinemallcloud.omcgoods830x.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmSeckillGoodsMainMapper;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmSeckillGoodsToSeckillGoodVoMapper;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmSeckillGoods;
import us.sushome.onlinemallcloud.omcgoods830x.dao.OmSeckillGoodsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
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
public class IOmSeckillGoodsService extends ServiceImpl<OmSeckillGoodsMapper, OmSeckillGoods> {
    private static Logger logger = LoggerFactory.getLogger(IOmSeckillGoodsService.class);

    @Resource
    private OmSeckillGoodsToSeckillGoodVoMapper omSeckillGoodsToSeckillGoodVoMapper;

    @Resource
    private OmSeckillGoodsMainMapper omSeckillGoodsMainMapper;

    public SeckillGoodVo convertOmSeckillGoodsToSeckillGoodVo(OmSeckillGoods omSeckillGoods) {
        return omSeckillGoodsToSeckillGoodVoMapper.omSeckillGoodsToSeckillGoodVo(omSeckillGoods);
    }

    public List<SeckillGoodVo> convertOmSeckillGoodsListToSeckillGoodVoList(List<OmSeckillGoods> omSeckillGoodsList) {
        return omSeckillGoodsToSeckillGoodVoMapper.omSeckillGoodsToSeckillGoodVoList(omSeckillGoodsList);
    }

    public OmSeckillGoods convertOmSeckillGoodInfoToOmSeckillGoods(OmSeckillGoodInfo omSeckillGoodInfo) {
        return omSeckillGoodsToSeckillGoodVoMapper.seckillGoodInfoToOmSeckillGoods(omSeckillGoodInfo);
    }

    public OmSeckillGoods seckillVoToModel(SeckillGoodVo seckillGoodVo) {
        return omSeckillGoodsToSeckillGoodVoMapper.seckillGoodVoToOmSeckillGoods(seckillGoodVo);
    }

    public IPage<OmSeckillGoodInfo> getSeckillGoodsList(String goodsName, Integer goodsType, int pageNum, int pageSize,Boolean showAll) {
        //showAll 为 true 时返回所有 false 时返回已开启的秒杀商品
        Page<OmSeckillGoodInfo> page = new Page<>(pageNum, pageSize);
        return omSeckillGoodsMainMapper.getSeckillGoodList(page, goodsName, goodsType,showAll);
    }

    public List<OmSeckillGoodInfo> getOpeningSeckillGoodList(){
        return omSeckillGoodsMainMapper.getOpeningSeckillGoodList();
    }

    public OmSeckillGoodInfo getSeckillGoodInfoById(String id) {
        return omSeckillGoodsMainMapper.getSeckillGoodById(id);
    }
}