package us.sushome.onlinemallcloud.omcuser820x.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
        import us.sushome.onlinemallcloud.omccommon.api.user.UserServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omcuser820x.service.OmUserMainService;

import java.util.List;

@RestController
@RequestMapping("/internal/user")
public class UserInternalController implements UserServiceApi {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    OmUserMainService omUserMainService;

    @GetMapping("/adminTest")
    public String adminTest() {
        return "adminTest";
    }

    @GetMapping("/userTest")
    public String userTest() {
        return "userTest";
    }

    @Override
    @GetMapping("/getPermissionsListFromFeign")
    public List<PermissionVo> getPermissionsList() {
        return omUserMainService.getPermissionsList();
    }

    @Override
    @GetMapping("/getUserInfoFromFeign")
    public UserVo getUserInfo(@RequestParam String id){
        return omUserMainService.getUserInfo(id,null,null,null);
    }

    @Override
    @PostMapping("/updateUserInfo")
    public UserVo updateUserInfo(@RequestBody UserVo userVo){
        System.out.println("userVo"+userVo);
        return omUserMainService.updateUserInfo(userVo);
    }
}
