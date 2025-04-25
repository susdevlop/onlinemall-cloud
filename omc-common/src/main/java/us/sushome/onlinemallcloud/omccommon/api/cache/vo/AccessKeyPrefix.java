package us.sushome.onlinemallcloud.omccommon.api.cache.vo;

import java.io.Serializable;

public class AccessKeyPrefix extends BaseKeyPrefix implements Serializable {
    private static final long serialVersionUID = 1L;
    public AccessKeyPrefix(String prefix){
        super(prefix);
    }

    public AccessKeyPrefix(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static AccessKeyPrefix withExpire(int expireSeconds){
        return new AccessKeyPrefix(expireSeconds,"access");
    }
}
