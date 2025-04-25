package us.sushome.onlinemallcloud.omccommon.api;

import lombok.Data;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;

import java.io.Serializable;

@Data
public class ResultVo<T> implements Serializable {
    private CodeMsg message;
    private T data;
}
