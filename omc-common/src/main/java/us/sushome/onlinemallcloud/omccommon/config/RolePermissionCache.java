package us.sushome.onlinemallcloud.omccommon.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.sushome.onlinemallcloud.omccommon.api.user.UserServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RolePermissionCache {

    @Autowired
    private UserServiceApi userServiceApi;

    //使用 List存储需要重新登录的 userid
    //private final List<String> needReLoginUserIdList = new ArrayList<>();


    //public Boolean getIsNeedReLogin(String userId){
    //    System.out.println("needReLoginUserIdList"+needReLoginUserIdList);
    //    if(userId == null){
    //        return false;
    //    }
    //    return needReLoginUserIdList.contains(userId);
    //}
    //
    //public void addNeedReLogin(String userId){
    //    if(!getIsNeedReLogin(userId)){
    //        needReLoginUserIdList.add(userId);
    //    }
    //    System.out.println("needReLoginUserIdList"+needReLoginUserIdList);
    //}

    //public void clearNeedReLoginListByUserId(String userId) {
    //    needReLoginUserIdList.removeIf(userId::equals);
    //}

    private final Map<Integer, List<String>> rolePermissionsMap = new HashMap<>();

    public List<String> getPermissionsByRoleId(Integer roleId) {
        if(rolePermissionsMap.isEmpty()){
            loadPermissionsIntoMemory();
        }

        if (roleId == 0) {//超级管理员
            // 返回所有权限的集合（去重）
            return rolePermissionsMap.values().stream()
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return rolePermissionsMap.computeIfAbsent(roleId, k -> new ArrayList<>());
    }


    public void loadPermissionsIntoMemory(){
        System.out.println("-------开始初始化权限表--------");
        List<PermissionVo> rolePermissions = userServiceApi.getPermissionsList();
        Map<Integer, List<String>> initialData = new HashMap<>();
        for (PermissionVo permissionVo : rolePermissions) {
            Integer roleId = permissionVo.getOmPermissionRoleid();
            Set<String> pathSet = new HashSet<>(initialData.computeIfAbsent(roleId, k -> new ArrayList<>()));
            pathSet.add(permissionVo.getOmPermissionPath());
            initialData.put(roleId,new ArrayList<>(pathSet));
        }
        System.out.println("当前权限表："+rolePermissions.toString());
        System.out.println("------------------------------");
        rolePermissionsMap.clear();
        rolePermissionsMap.putAll(initialData);
    }
}
