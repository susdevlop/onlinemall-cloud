package us.sushome.onlinemallcloud.omcgoods830x.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoods;
import us.sushome.onlinemallcloud.omcgoods830x.model.OmGoodsku;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class OmGoodsUniteSkuList extends OmGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skuList;

    public List<OmGoodsku> getSkuListAsObject() {
        if (this.skuList == null || this.skuList.isEmpty() || this.skuList.equals("[null]")) {
            return Collections.emptyList();
        }
        return JSON.parseArray(this.skuList, OmGoodsku.class);
    }
}
