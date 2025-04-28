package us.sushome.onlinemallcloud.omcorder840x.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmOrderMainMapper;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmOrderToOrderVoMapper;
import us.sushome.onlinemallcloud.omcorder840x.domain.OmOrderInfo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrder;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmOrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * $!{table.comment} 服务类
 * </p>
 *
 * @author sushome
 * @since 2025-03-26
 */
@Service
public class IOmOrderService extends ServiceImpl<OmOrderMapper, OmOrder> {
    private static Logger logger = LoggerFactory.getLogger(IOmOrderService.class);

    @Resource
    private OmOrderMainMapper omOrderMainMapper;

    @Resource
    private OmOrderToOrderVoMapper omOrderToOrderVoMapper;

    public OrderVo convertOmOrderToOrderVo(OmOrder omOrder) {
        return omOrderToOrderVoMapper.omOrderToOrderVo(omOrder);
    }

    public OmOrder convertOrderVoToOmOrder(OrderVo orderVo){
        return omOrderToOrderVoMapper.orderVoToOmOrder(orderVo);
    }

    public List<OrderVo> convertOmOrderListToOrderVoList(List<OmOrder> omOrderList) {
        return omOrderToOrderVoMapper.omOrderToOrderVoList(omOrderList);
    }

    public IPage<OmOrderInfo> getOrderInfoList(String orderId, String userId, Integer status, Integer type, String phone, LocalDate payDate, String goodName, Integer pageNum, Integer pageSize){
        Page<OmOrderInfo> page = new Page<>(pageNum, pageSize);
        LocalDateTime payDateStart = (payDate != null) ?
                payDate.atStartOfDay() : null;  // 00:00:00
        LocalDateTime payDateEnd = (payDate != null) ?
                payDate.atTime(LocalTime.MAX) : null;  // 23:59:59.999999999
        return omOrderMainMapper.getOrderInfoList(page, orderId, userId, status, type, phone, payDateStart,payDateEnd, goodName);
    }

    public OmOrderInfo getTodayOrderByUserOrder(String dateStr,String seckillId, String userId){
        return omOrderMainMapper.getTodayOrderByUserOrder(dateStr,seckillId, userId);
    }
}