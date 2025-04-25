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
 * 
 * </p>
 *
 * @author sushome
 * @since 2025-01-15
 */
@ToString
@Getter
@Setter
@TableName("om_permission")
@ApiModel(value = "OmPermission对象", description = "")
public class OmPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("om_permission_id")
    private String omPermissionId;

    @ApiModelProperty("权限的描述")
    @TableField("om_permission_desc")
    private String omPermissionDesc;

    @ApiModelProperty("roleid 假如是 21，那么 20为 20～29的所有权限")
    @TableField("om_permission_roleid")
    private Integer omPermissionRoleid;

    @TableField("om_permission_path")
    private String omPermissionPath;
}
