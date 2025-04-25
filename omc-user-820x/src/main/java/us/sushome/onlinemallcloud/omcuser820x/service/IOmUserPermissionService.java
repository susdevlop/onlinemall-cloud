package us.sushome.onlinemallcloud.omcuser820x.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omcuser820x.model.OmUserPermission;
import us.sushome.onlinemallcloud.omcuser820x.dao.OmUserPermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * $!{table.comment} 服务类
 * </p>
 *
 * @author sushome
 * @since 2025-01-07
 */
@Service
public class IOmUserPermissionService extends ServiceImpl<OmUserPermissionMapper, OmUserPermission> {
    private static Logger logger = LoggerFactory.getLogger(IOmUserPermissionService.class);

    public List<Integer> getRoleIdListByUserId(String userId,Boolean showAll){
        QueryWrapper<OmUserPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("om_up_userid", userId);
        List<OmUserPermission> omUserPermissions = this.list(queryWrapper);
        logger.info("omUserPermissions"+omUserPermissions);
        List<Integer> roleIdList = new ArrayList<>();
        for (OmUserPermission omUserPermission : omUserPermissions) {
            roleIdList.add(omUserPermission.getOmUpRoleid());
        }
        if(!showAll){
            if(roleIdList.contains(0)){
                roleIdList = new ArrayList<>();
                roleIdList.add(0);
            }
        }

        return roleIdList;
    }

    public List<OmUserPermission> queryAllByUserId(String userId){
        QueryWrapper<OmUserPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("om_up_userid", userId);
        return this.list(queryWrapper);
    }
}