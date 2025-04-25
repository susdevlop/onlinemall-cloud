package us.sushome.onlinemallcloud.omcuser820x.service;

import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omcuser820x.dao.OmPermissionToOmPermissionVoMapper;
import us.sushome.onlinemallcloud.omcuser820x.model.OmPermission;
import us.sushome.onlinemallcloud.omcuser820x.dao.OmPermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * $!{table.comment} 服务类
 * </p>
 *
 * @author sushome
 * @since 2025-01-15
 */
@Service
public class IOmPermissionService extends ServiceImpl<OmPermissionMapper, OmPermission> {
    private static Logger logger = LoggerFactory.getLogger(IOmPermissionService.class);

    @Resource
    private OmPermissionToOmPermissionVoMapper omPermissionToOmPermissionVoMapper;

    public List<PermissionVo> convertOmPermissionToOmPermissionVoList(List<OmPermission> omPermissions) {
        return omPermissionToOmPermissionVoMapper.omPermissionListToOmPermissionVoList(omPermissions);
    }
}