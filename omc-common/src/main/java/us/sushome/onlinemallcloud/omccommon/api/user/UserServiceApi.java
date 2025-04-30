package us.sushome.onlinemallcloud.omccommon.api.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.saToken.FeignInterceptor;

import java.util.List;

@FeignClient(value = "omc-user-820x",path = "/internal/user",configuration = FeignInterceptor.class)
public interface UserServiceApi {
    @GetMapping("/getPermissionsListFromFeign")
    List<PermissionVo> getPermissionsList();

    @GetMapping("/getUserInfoFromFeign")
    UserVo getUserInfo(@RequestParam String id);

    @PostMapping("/updateUserInfo")
    UserVo updateUserInfo(@RequestBody UserVo userVo);
}
