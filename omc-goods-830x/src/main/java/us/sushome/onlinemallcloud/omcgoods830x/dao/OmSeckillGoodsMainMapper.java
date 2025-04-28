package us.sushome.onlinemallcloud.omcgoods830x.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmSeckillGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmSeckillGoods;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface OmSeckillGoodsMainMapper extends BaseMapper<OmSeckillGoods> {

    @Select({
            "<script>",
            "select g.*, s.*, sku.*",
            "FROM om_seckill_goods g",
            "LEFT JOIN om_goods s ON g.om_osg_goodid = s.om_goods_id",
            "LEFT JOIN om_goodsku sku ON g.om_osg_goodskuid = sku.om_goodsku_id",
            "<where>",
            "s.om_goods_id is not null AND sku.om_goodsku_id is not null",
            "<if test='showAll == false'>",
                "AND om_osg_status = 1 or om_osg_status = 2", // 显示所有状态为1的商品
            "</if>",
            "<if test='name!= null and name!= \"\"'>",
            "AND g.om_osg_name LIKE CONCAT('%', #{name}, '%')", // 根据商品名称模糊查询
            "</if>",
            "<if test='goodsType!= null'>",
            "AND g.om_osg_goodtype = #{goodsType}", // 精确匹配商品类型
            "</if>",
            "</where>",
            "</script>"
    })
    IPage<OmSeckillGoodInfo> getSeckillGoodList(Page<OmSeckillGoodInfo> page, @Param("name") String name, @Param("goodsType") Integer goodsType,@Param("showAll") Boolean showAll);

    @Select({
            "<script>",
                "select g.*, s.*, sku.*",
                "FROM om_seckill_goods g",
                "LEFT JOIN om_goods s ON g.om_osg_goodid = s.om_goods_id",
                "LEFT JOIN om_goodsku sku ON g.om_osg_goodskuid = sku.om_goodsku_id",
                "<where>",
                    "s.om_goods_id is not null AND sku.om_goodsku_id is not null",
                    "and om_osg_status = 1 or om_osg_status = 2", //
                "</where>",
            "</script>"
    })
    List<OmSeckillGoodInfo> getOpeningSeckillGoodList();


    @Select({
            "<script>",
            "select g.*, s.*, sku.*",
            "FROM om_seckill_goods g",
            "LEFT JOIN om_goods s ON g.om_osg_goodid = s.om_goods_id",
            "LEFT JOIN om_goodsku sku ON g.om_osg_goodskuid = sku.om_goodsku_id",
            "where g.om_osg_id = #{id}",
            "</script>"
    })
    OmSeckillGoodInfo getSeckillGoodById(@Param("id") String id);
}
