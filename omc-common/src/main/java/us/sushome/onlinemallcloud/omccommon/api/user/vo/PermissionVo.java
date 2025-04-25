package us.sushome.onlinemallcloud.omccommon.api.user.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionVo implements Serializable {
    @NotNull
    private String omPermissionId;

    @NotNull
    private String omPermissionDesc;

    @NotNull
    private Integer omPermissionRoleid;

    @NotNull
    private String omPermissionPath;
}
