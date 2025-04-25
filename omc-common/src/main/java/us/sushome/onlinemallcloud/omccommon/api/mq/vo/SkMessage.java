package us.sushome.onlinemallcloud.omccommon.api.mq.vo;

import lombok.Data;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;

import java.io.Serializable;


//未使用
@Data
public class SkMessage implements Serializable {
    private UserVo user;

    private String goodsId;

}
