package us.sushome.onlinemallcloud.omcorder840x.service;

import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderDetailVo;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmOrderDetailToOrderDetailVoMapper;
import us.sushome.onlinemallcloud.omcorder840x.domain.OmOrderInfo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrderdetail;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmOrderdetailMapper;
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
 * @since 2025-04-02
 */
@Service
public class IOmOrderdetailService extends ServiceImpl<OmOrderdetailMapper, OmOrderdetail> {
    private static Logger logger = LoggerFactory.getLogger(IOmOrderdetailService.class);

    @Resource
    private OmOrderDetailToOrderDetailVoMapper omOrderDetailToOrderDetailVoMapper;

    public OrderDetailVo convertOmOrderDetailToOrderDetailVo(OmOrderdetail omOrderdetail) {
        return omOrderDetailToOrderDetailVoMapper.omOrderDetailToOrderDetailVo(omOrderdetail);
    }

    public OmOrderdetail convertOrderDetailVoToOmOrderDetail(OrderDetailVo orderDetailVo) {
        return omOrderDetailToOrderDetailVoMapper.orderDetailVoToOmOrderDetail(orderDetailVo);
    }

    public List<OrderDetailVo> convertOmOrderDetailListToOrderDetailVoList(List<OmOrderdetail> omOrderdetailList) {
        return omOrderDetailToOrderDetailVoMapper.omOrderDetailListToOrderDetailVoList(omOrderdetailList);
    }

    public List<OmOrderdetail> convertOrderDetailVoListToOmOrderDetailList(List<OrderDetailVo> orderDetailVoList) {
        return omOrderDetailToOrderDetailVoMapper.orderDetailVoListToOmOrderDetailList(orderDetailVoList);
    }

    public OrderDetailVo omOrderInfoToOrderDetailVo(OmOrderInfo omOrderInfo) {
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderId(omOrderInfo.getOmOrderdetailOrderid());
        orderDetailVo.setSkuId(omOrderInfo.getOmOrderdetailSkuid());
        orderDetailVo.setGoodName(omOrderInfo.getOmOrderdetailGoodname());
        orderDetailVo.setCount(omOrderInfo.getOmOrderdetailCount());
        orderDetailVo.setUniPrice(omOrderInfo.getOmOrderdetailUnitprice());
        orderDetailVo.setSumPrice(omOrderInfo.getOmOrderdetailSumprice());
        orderDetailVo.setUpdateTime(omOrderInfo.getOmOrderdetailUpdatetime());
        orderDetailVo.setAddTime(omOrderInfo.getOmOrderdetailAddtime());
        return orderDetailVo;
    }
}