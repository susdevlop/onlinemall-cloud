package us.sushome.onlinemallcloud.omccommon.api.cache.vo;

import java.io.Serializable;

public class OrderKeyPrefix extends BaseKeyPrefix implements Serializable {
    private static final long serialVersionUID = 1L;
    public OrderKeyPrefix(String prefix) {
        super(prefix);
    }
    public OrderKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKeyPrefix ORDER = new OrderKeyPrefix(60*60*24*30,"ORDER");
}
