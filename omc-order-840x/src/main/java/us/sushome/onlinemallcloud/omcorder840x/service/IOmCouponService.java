package us.sushome.onlinemallcloud.omcorder840x.service;

import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.CouponVo;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmCouponToCouponVoMapper;
import us.sushome.onlinemallcloud.omcorder840x.model.OmCoupon;
import us.sushome.onlinemallcloud.omcorder840x.dao.OmCouponMapper;
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
 * @since 2025-03-26
 */
@Service
public class IOmCouponService extends ServiceImpl<OmCouponMapper, OmCoupon> {
    private static Logger logger = LoggerFactory.getLogger(IOmCouponService.class);

    @Resource
    private OmCouponToCouponVoMapper omCouponToCouponVoMapper;

    public CouponVo convertOmCouponToCouponVo(OmCoupon omCoupon) {
        return omCouponToCouponVoMapper.omCouponToCouponVo(omCoupon);
    }

    public List<CouponVo> convertOmCouponListToCouponVoList(List<OmCoupon> omCouponList) {
        return omCouponToCouponVoMapper.omCouponToCouponVoList(omCouponList);
    }
}