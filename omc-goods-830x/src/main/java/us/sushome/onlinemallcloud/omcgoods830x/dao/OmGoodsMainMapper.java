package us.sushome.onlinemallcloud.omcgoods830x.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodInfo;
import us.sushome.onlinemallcloud.omcgoods830x.domain.OmGoodsUniteSkuList;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoods;

import java.util.List;

@Mapper
public interface OmGoodsMainMapper  extends BaseMapper<OmGoods> {

    @Select("select g.*,s.* from om_goods g left join om_goodsku s on g.om_goods_id = s.om_goodsku_goodsid " +
                "where g.om_goods_id = #{id}")
    List<OmGoodInfo> getGoodInfoById(@Param("id") String id);

    @Select("select g.*,s.* from om_goods g left join om_goodsku s on g.om_goods_id = s.om_goodsku_goodsid " +
            "where s.om_goodsku_id = #{skuId}")
    OmGoodInfo getGoodInfoBySkuId(@Param("skuId") String skuId);

    //@Select({
    //        "<script>",
    //        "select g.*, s.*",
    //        "from om_goods g",
    //        "left join om_goodsku s on g.om_goods_id = s.om_goodsku_goodsid",
    //        "<where>",
    //        "<if test='goodsName != null and goodsName != \"\"'>",
    //        "AND g.om_goods_name LIKE CONCAT('%', #{goodsName}, '%')", // 根据商品名称模糊查询
    //        "</if>",
    //        "<if test='goodsType != null'>",
    //        "AND g.om_goods_type = #{goodsType}", // 精确匹配商品类型
    //        "</if>",
    //        "</where>",
    //        "</script>"
    //})
    @Select({
            "<script>",
                "SELECT g.*,JSON_ARRAYAGG(" +
                        "CASE WHEN s.om_goodsku_id IS NOT NULL THEN "+
                        "JSON_OBJECT('om_goodsku_id', s.om_goodsku_id,"+
                        "'om_goodsku_goodsid', s.om_goodsku_goodsid," +
                        "'om_goodsku_name', s.om_goodsku_name," +
                        "'om_goodsku_price', s.om_goodsku_price," +
                        "'om_goodsku_inventory', s.om_goodsku_inventory," +
                        "'om_goodsku_media', s.om_goodsku_media," +
                        "'om_goodsku_updatetime', s.om_goodsku_updatetime," +
                        "'om_goodsku_addtime', s.om_goodsku_addtime)" +
                        "ELSE NULL END"+
                        ") AS sku_list",
                "FROM om_goods g",
                "LEFT JOIN om_goodsku s ON g.om_goods_id = s.om_goodsku_goodsid",
                "<where>",
                "<if test='goodsName != null and goodsName != \"\"'>",
                    "AND g.om_goods_name LIKE CONCAT('%', #{goodsName}, '%')", // 根据商品名称模糊查询
                "</if>",
                "<if test='goodsType != null'>",
                    "AND g.om_goods_type = #{goodsType}", // 精确匹配商品类型
                "</if>",
                "<if test='skuNotEmpty != null and skuNotEmpty'>",
                    "AND s.om_goodsku_id is not null",
                "</if>",
                "</where>",
                "GROUP BY g.om_goods_id",
            "</script>"
    })
    IPage<OmGoodsUniteSkuList> getGoodsUniteSkuList(Page<OmGoodInfo> page, @Param("goodsName") String goodsName, @Param("goodsType") Integer goodsType,@Param("skuNotEmpty") Boolean skuNotEmpty);
}


//select g.*, s.* from om_goods g left join om_goodsku s on g.om_goods_id = s.om_goodsku_goodsid LIMIT 20,10
//

