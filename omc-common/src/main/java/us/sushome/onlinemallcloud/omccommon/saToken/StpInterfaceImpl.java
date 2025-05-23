package us.sushome.onlinemallcloud.omccommon.saToken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.sushome.onlinemallcloud.omccommon.api.user.UserServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omccommon.config.RolePermissionCache;
import us.sushome.onlinemallcloud.omccommon.result.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static us.sushome.onlinemallcloud.omccommon.constants.UserRoleConstants.*;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private RolePermissionCache rolePermissionCache;

    @Autowired
    private UserServiceApi userServiceApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String roleList = (String) StpUtil.getSession().get("roleList");
        Set<String> roleSet = new HashSet<>();
        for (String s : roleList.split(",")) {
            List<String> rl = rolePermissionCache.getPermissionsByRoleId(Integer.parseInt(s));
            roleSet.addAll(rl);
        }
        // 返回此 loginId 拥有的权限列表
        List<String> list = roleSet.stream().toList();
        System.out.println(loginId+"拥有的权限列表：" + list);
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String roleList = (String) StpUtil.getSession().get("roleList");
        List<Integer> roleIdList = new ArrayList<>();
        for (String s : roleList.split(",")) {
            roleIdList.add(Integer.parseInt(s));
        }
        // 示例角色，根据实际业务逻辑返回
        if(roleIdList.contains(ADMIN_ROLE_ID)){
            return List.of(ADMIN_ROLE_NAME,USER_MANAGER_ROLE_NAME,GOODS_MANAGER_ROLE_NAME,ORDER_MANAGER_ROLE_NAME);
        }else if(roleIdList.contains(USER_ROLE_ID)){
            return List.of(USER_ROLE_NAME);
        }else if(roleIdList.contains(USER_MANAGER_ROLE_ID)){
            return List.of(USER_MANAGER_ROLE_NAME);
        }else if(roleIdList.contains(GOODS_MANAGER_ROLE_ID)){
            return List.of(GOODS_MANAGER_ROLE_NAME);
        }else if(roleIdList.contains(ORDER_MANAGER_ROLE_ID)){
            return List.of(ORDER_MANAGER_ROLE_NAME);
        }else {
            return List.of(USER_ROLE_NAME);
        }
    }
}