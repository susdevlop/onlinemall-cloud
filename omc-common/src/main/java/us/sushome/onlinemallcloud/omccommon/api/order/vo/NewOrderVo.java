package us.sushome.onlinemallcloud.omccommon.api.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class NewOrderVo implements Serializable {
    OrderVo orderInfo;
    Map<String,Object> payInfo;
}
