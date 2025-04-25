package us.sushome.onlinemallcloud.omccommon.api.user.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class UserVo implements Serializable {
    //token或SecurityContextHolder.getContext() 中只保存 userid和 userName
    private String userId;

    @NotNull
    private String userName;

    @NotNull
    private String phone;
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String address;

    private String customName;

    private BigDecimal balance;

    private LocalDateTime updateTime;

    private LocalDateTime addTime;
}
