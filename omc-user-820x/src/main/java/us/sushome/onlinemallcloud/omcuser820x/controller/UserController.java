package us.sushome.onlinemallcloud.omcuser820x.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sushome.onlinemallcloud.omccommon.api.user.UserServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.config.RolePermissionCache;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.result.Result;
import us.sushome.onlinemallcloud.omcuser820x.service.OmUserMainService;
//import us.sushome.onlinemallcloud.omccommon.utils.file.QiniuUploadUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/openApi/user")
public class UserController implements UserServiceApi {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    OmUserMainService omUserMainService;

    @SaCheckPermission("/openApi/user")
    @GetMapping("/adminTest")
    public String adminTest() {
        return "adminTest";
    }

    @GetMapping("/userTest")
    public String userTest() {
        return "userTest";
    }

    @PostMapping("/login")
    public Result login(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String userName = jsonObject.get("userName").toString();
        String userPasswd = jsonObject.get("userPasswd").toString();
        Boolean isRememberMe = jsonObject.getBoolean("remember");
        if(isRememberMe == null){
            isRememberMe = false;
        }
        Result<Map<String,Object>> result = omUserMainService.login(userName, userPasswd, isRememberMe);
        if(result.getCode() == CodeMsg.SUCCESS.getCode()){
            Map<String,Object> map = result.getData();
            UserVo userVo = (UserVo) map.get("userInfo");
            String roleList = map.get("roleList").toString();
            StpUtil.login(userVo.getUserId());
            StpUtil.getSession().set("roleList", roleList);
        }
        return result;
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserVo userVo) {
        logger.info("register user: {}", userVo);
        Result<Map<String,Object>> result = omUserMainService.register(userVo);
        if(result.getCode() == CodeMsg.SUCCESS.getCode()){
            Map<String,Object> map = result.getData();
            UserVo _userVo = (UserVo) map.get("userInfo");
            String roleList = map.get("roleList").toString();
            StpUtil.login(_userVo.getUserId());
            StpUtil.getSession().set("roleList", roleList);
        }
        return result;
    }

    @PostMapping("/logout")
    public Result<String> logout(){
        omUserMainService.logout();
        return Result.success("登出成功");
    }

    //@PreAuthorize("@Pm.check('/openApi/user/getUserInfo')")
    @PostMapping("/getUserInfo")
    public Result<UserVo> getUserInfo(String id,String name,String phone,String email){
        return Result.success(omUserMainService.getUserInfo(id,name,phone,email));
    }

    @PostMapping("/getMyUserInfo")
    public Result<UserVo> getMyUserInfo(){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UserVo userVo = (UserVo) authentication.getPrincipal();
        return Result.success(omUserMainService.getUserInfo(null,null,null,null));
    }

    //@PreAuthorize("@Pm.check('/openApi/user/getUserInfoList')")
    @PostMapping("/getUserInfoList")
    public Result<List<UserVo>> getUserInfoList(){
        return Result.success(omUserMainService.getUserInfoList());
    }

    //@PreAuthorize("@Pm.check('/openApi/user/getUserBySelect')")
    @PostMapping("/getUserBySelect")
    public Result<List<UserVo>> getUserBySelect(@RequestBody UserVo userVo){
        return Result.success(omUserMainService.getUserBySelect(userVo));
    }

    //@PreAuthorize("@Pm.check('/openApi/user/getUserPermissionByUserId')")
    @PostMapping("/getUserPermissionByUserId")
    public Result<List<Integer>> getUserPermissionByUserId(@RequestBody Map<String, String> requestBody){
        String userId = requestBody.get("userId");
        if(userId == null){
            return Result.success(new ArrayList<>());
        }
        return Result.success(omUserMainService.getUserPermissionByUserId(userId));
    }

    @Override
    @PostMapping("/getCurrentPermission")
    public Result<List<Integer>> getCurrentPermission(String userId){
        return Result.success(omUserMainService.getUserPermissionByUserId(userId));
    }

    //@PreAuthorize("@Pm.check('/openApi/user/getPermissionsList')")
    @PostMapping("/getPermissionsList")
    public Result<List<PermissionVo>> getPermissionsList(String roleId){
        return Result.success(omUserMainService.getPermissionsList());
    }

    @Override
    @GetMapping("/getPermissionsList")
    public List<PermissionVo> getPermissionsList() {
        return omUserMainService.getPermissionsList();
    }

    //@PreAuthorize("@Pm.check('/openApi/user/setUserPermissionByUserId')")
    @PostMapping("/setUserPermissionByUserId")
    public Result<Boolean> setUserPermissionByUserId(@RequestBody Map<String, Object> requestBody){

        String userId = (String) requestBody.get("userId");
        if(userId == null){
            return Result.success(false);
        }
        Object roleIdsObj = requestBody.get("roleIds");
        List<String> roleIds = null;
        if (roleIdsObj instanceof List<?>) {
            roleIds = (List<String>) roleIdsObj;  // 安全地转换为 List<String>
        }
        List<Integer> roleIdsInt = new ArrayList<>();
        for (String roleId : roleIds) {
            roleIdsInt.add(Integer.parseInt(roleId));
        }
        logger.info("roleIds"+ roleIds);
        Boolean isSuccess = omUserMainService.setUserPermissionByUserId(userId,roleIdsInt);
        if(isSuccess){
            //rolePermissionCache.addNeedReLogin(userId);
        }
        return Result.success(isSuccess);
    }



    //@PreAuthorize("@Pm.check('/openApi/user/getUploadToken')")
    //@PostMapping("/getUploadToken")
    //public Result<String> getUploadToken(){
    //    return Result.success(QiniuUploadUtils.getUpToken());
    //}
}
