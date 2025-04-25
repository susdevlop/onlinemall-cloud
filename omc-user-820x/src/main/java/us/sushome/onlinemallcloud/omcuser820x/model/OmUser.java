package us.sushome.onlinemallcloud.omcuser820x.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
 * @since 2025-03-25
 */
@ToString
@Getter
@Setter
@TableName("om_user")
@ApiModel(value = "OmUser对象", description = "")
public class OmUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("uuid")
    @TableId("om_user_id")
    private String omUserId;

    @TableField("om_user_name")
    private String omUserName;

    @TableField("om_user_phone")
    private String omUserPhone;

    @TableField("om_user_email")
    private String omUserEmail;

    @TableField("om_user_passwd")
    private String omUserPasswd;

    @TableField("om_user_address")
    private String omUserAddress;

    @TableField("om_user_customname")
    private String omUserCustomname;

    @TableField("om_user_balance")
    private BigDecimal omUserBalance;

    @TableField("om_user_updatetime")
    private LocalDateTime omUserUpdatetime;

    @TableField("om_user_addtime")
    private LocalDateTime omUserAddtime;
}
