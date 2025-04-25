package us.sushome.onlinemallcloud.omccommon.api.cache.vo;

import java.io.Serializable;

public class SeckillKeyPrefix extends BaseKeyPrefix implements Serializable {
    private static final long serialVersionUID = 1L;
    public SeckillKeyPrefix(String prefix) {
        super(prefix);
    }
    public SeckillKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    /**
     * 库存为0的商品的前缀
     */
    public static SeckillKeyPrefix GOODS_SK_OVER = new SeckillKeyPrefix("goodsSkOver");
    /**
     * 秒杀接口随机地址
     */
    public static SeckillKeyPrefix SK_PATH = new SeckillKeyPrefix(60*10, "skPath");

    public static SeckillKeyPrefix SK_GOOD_STOCK = new SeckillKeyPrefix(0, "skGoodStock");
}
