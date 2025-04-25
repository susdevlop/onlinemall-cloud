package us.sushome.onlinemallcloud.omcuser820x.dao;

import org.apache.ibatis.annotations.Mapper;
import us.sushome.onlinemallcloud.omcuser820x.model.OmUserPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户对应的权限表 Mapper 接口
 * </p>
 *
 * @author sushome
 * @since 2025-01-07
 */
@Mapper
public interface OmUserPermissionMapper extends BaseMapper<OmUserPermission> {

}
