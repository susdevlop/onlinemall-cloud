package us.sushome.onlinemallcloud.omcuser820x.dao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omcuser820x.model.OmPermission;

import java.util.List;

@Mapper(componentModel = "spring")  // 让 MapStruct 生成 Spring Bean
public interface OmPermissionToOmPermissionVoMapper {

    @Mapping(source = "omPermissionId",target = "omPermissionId")
    @Mapping(source = "omPermissionDesc",target = "omPermissionDesc")
    @Mapping(source = "omPermissionRoleid",target = "omPermissionRoleid")
    @Mapping(source = "omPermissionPath",target = "omPermissionPath")
    PermissionVo omPermissionToOmPermissionVo(OmPermission omPermission);

    List<PermissionVo> omPermissionListToOmPermissionVoList(List<OmPermission> omPermissions);
}
