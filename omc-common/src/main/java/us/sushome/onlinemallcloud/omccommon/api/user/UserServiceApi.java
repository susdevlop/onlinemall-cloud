package us.sushome.onlinemallcloud.omccommon.api.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.result.Result;

import java.util.List;

@FeignClient(value = "omc-user-820x",path = "/openApi/user")
public interface UserServiceApi {
    @PostMapping("/getPermissionsList")
    public List<PermissionVo> getPermissionsList();

    @PostMapping("/getCurrentPermission")
    public Result<List<Integer>> getCurrentPermission(String userId);

    @PostMapping("/getUserInfo")
    public Result<UserVo> getUserInfo(String id,String name,String phone,String email);
}
