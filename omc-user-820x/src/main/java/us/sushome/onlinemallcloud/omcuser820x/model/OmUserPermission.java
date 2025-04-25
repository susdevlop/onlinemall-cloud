package us.sushome.onlinemallcloud.omcuser820x.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户对应的权限表
 * </p>
 *
 * @author sushome
 * @since 2025-01-07
 */
@ToString
@Getter
@Setter
@TableName("om_user_permission")
@ApiModel(value = "OmUserPermission对象", description = "用户对应的权限表")
public class OmUserPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("om_up_id")
    private String omUpId;

    @ApiModelProperty("用户id")
    @TableField("om_up_userid")
    private String omUpUserid;

    @ApiModelProperty("用户拥有的权限 id")
    @TableField("om_up_roleid")
    private Integer omUpRoleid;
}
