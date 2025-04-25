package us.sushome.onlinemallcloud.omcuser820x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omcuser820x.model.OmUser;

import java.util.List;

@Mapper(componentModel = "spring")  // 让 MapStruct 生成 Spring Bean
public interface OmUserToUserVoMapper {

    @Mapping(source = "omUserId", target = "userId")
    @Mapping(source = "omUserName", target = "userName")
    @Mapping(source = "omUserPhone", target = "phone")
    @Mapping(source = "omUserEmail", target = "email")
    @Mapping(source = "omUserPasswd", target = "password",ignore = true)
    @Mapping(source = "omUserAddress", target = "address")
    @Mapping(source = "omUserCustomname",target = "customName")
    @Mapping(source = "omUserBalance",target = "balance")
    @Mapping(source = "omUserUpdatetime", target = "updateTime")
    @Mapping(source = "omUserAddtime", target = "addTime")
    UserVo omUserToUserVo(OmUser omUser);

    @Mapping(source = "userId", target = "omUserId")
    @Mapping(source = "userName", target = "omUserName")
    @Mapping(source = "phone", target = "omUserPhone")
    @Mapping(source = "email", target = "omUserEmail")
    @Mapping(source = "password", target = "omUserPasswd",ignore = true)
    @Mapping(source = "address", target = "omUserAddress")
    @Mapping(source = "customName",target = "omUserCustomname")
    @Mapping(source = "balance",target = "omUserBalance")
    @Mapping(source = "updateTime", target = "omUserUpdatetime")
    @Mapping(source = "addTime", target = "omUserAddtime")
    OmUser userVoToOmUser(UserVo userVo);

    List<UserVo> omUserListToUserVoList(List<OmUser> omUserList);
}