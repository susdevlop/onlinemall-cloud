package us.sushome.onlinemallcloud.omccommon.api.cache.vo;

import java.io.Serializable;

public class GoodsKeyPrefix extends BaseKeyPrefix implements Serializable {
    private static final long serialVersionUID = 1L;
    public GoodsKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKeyPrefix GOODS_STOCK = new GoodsKeyPrefix(0,"goodsStock");
}
