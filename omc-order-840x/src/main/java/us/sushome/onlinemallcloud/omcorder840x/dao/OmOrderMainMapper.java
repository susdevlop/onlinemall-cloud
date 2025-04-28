package us.sushome.onlinemallcloud.omcorder840x.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import us.sushome.onlinemallcloud.omcorder840x.domain.OmOrderInfo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrder;

import java.time.LocalDateTime;

@Mapper
public interface OmOrderMainMapper extends BaseMapper<OmOrder> {


    @Select({
        "<script>",
            "SELECT o.*,d.* FROM om_order o LEFT JOIN om_orderdetail d ON o.om_order_id = d.om_orderdetail_orderid",
            "<where>",
                "<if test='orderId != null and orderId != \"\"'>",
                    "AND o.om_order_id = #{orderId}",
                "</if>",
                "<if test='userId != null and userId != \"\"'>",
                    "AND o.om_order_userid = #{userId}",
                "</if>",
                "<if test='status!= null'>",
                    "<choose>",
                        "<when test='status == 8'>",  // 当status=8时查询所有售后状态
                            "AND o.om_order_status IN (4,6,7,20,24,30,34,37,40,44,47)",
                        "</when>",
                        "<otherwise>",
                            "AND o.om_order_status = #{status}",
                        "</otherwise>",
                    "</choose>",
                "</if>",
                "<if test='type!= null'>",
                    "AND o.om_order_type = #{type}",
                "</if>",
                "<if test='phone!= null and phone!= \"\"'>",
                    "AND o.om_order_phone = #{phone}",
                "</if>",
                //"<if test='payDate!= null'>",
                //    "AND o.om_order_paydate = #{payDate}",
                //"</if>",
                "<if test='payDateStart != null and payDateEnd != null'>",
                    "AND o.om_order_paydate BETWEEN #{payDateStart} AND #{payDateEnd}",
                "</if>",
                "<if test='goodName!= null and goodName!= \"\"'>",
                    "AND d.om_orderdetail_goodname = #{goodName}",
                "</if>",
            "</where>",
            "ORDER BY o.om_order_addtime DESC",
        "</script>"
    })
    IPage<OmOrderInfo> getOrderInfoList(Page<OmOrderInfo> page, @Param("orderId") String orderId,@Param("userId") String userId,
        @Param("status") Integer status, @Param("type") Integer type,@Param("phone") String phone,
        @Param("payDateStart") LocalDateTime payDateStart,@Param("payDateEnd") LocalDateTime payDateEnd,@Param("goodName") String goodName);

    @Select({
        "<script>",
            "SELECT o.*,d.* FROM om_order o LEFT JOIN om_orderdetail d ON o.om_order_id = d.om_orderdetail_orderid",
            "<where>",
                "o.om_order_id is not null and d.om_orderdetail_id is not null and o.om_order_date = #{dateStr} AND d.om_orderdetail_seckillid = #{seckillId} AND o.om_order_userid = #{userId}",
            "</where>",
        "</script>"
    })
    OmOrderInfo getTodayOrderByUserOrder(@Param("dateStr") String dateStr,@Param("seckillId") String seckillId,@Param("userId") String userId);
}
