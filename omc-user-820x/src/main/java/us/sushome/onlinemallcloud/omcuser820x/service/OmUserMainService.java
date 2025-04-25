package us.sushome.onlinemallcloud.omcuser820x.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.PermissionVo;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.result.Result;
import us.sushome.onlinemallcloud.omccommon.utils.JwtUtils;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcuser820x.model.OmUser;
import us.sushome.onlinemallcloud.omcuser820x.model.OmUserPermission;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OmUserMainService{
    private static Logger logger = LoggerFactory.getLogger(OmUserMainService.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private IOmUserService iOmUserService;

    @Autowired
    private IOmPermissionService omPermissionService;

    @Autowired
    private IOmUserPermissionService omUserPermissionService;


    public Result<Map<String,Object>> login(String userName, String password, Boolean isRememberMe) {
        if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)){
            Map<String,Object> map = new HashMap<>();
            OmUser user = iOmUserService.findByUserName(userName);
            UserVo userVo = iOmUserService.convertOmUserToUserVo(user);
            if(user != null){
                if (this.bCryptPasswordEncoder.matches(password, user.getOmUserPasswd())) {
                    logger.info("userName"+userName);
                    //logger.info("user.getOmUserRoleid()"+user.getOmUserRoleid());
                    List<Integer> roleIdList = omUserPermissionService.getRoleIdListByUserId(user.getOmUserId(),false);
                    String roleList;
                    logger.info("roleIdList"+roleIdList);
                    if(roleIdList != null && !roleIdList.isEmpty()){
                        roleList = StringUtils.join(roleIdList, ",");
                    }else{
                        OmUserPermission omUserPermission = new OmUserPermission();
                        omUserPermission.setOmUpId(UUID.randomUUID().toString().replace("-",""));
                        omUserPermission.setOmUpUserid(user.getOmUserId());
                        omUserPermission.setOmUpRoleid(1);
                        omUserPermissionService.save(omUserPermission);
                        roleList = "1";
                    }
                    //String token = JwtUtils.generateToken(userName,user.getOmUserId(), roleList, isRememberMe);
                    // 认证成功后，设置认证信息到 Spring Security 上下文中
                    //Authentication authentication = JwtUtils.getAuthentication(token);
                    //SecurityContextHolder.getContext().setAuthentication(authentication);
                    map.put("roleList",roleList);
                    map.put("userInfo",userVo);
                    return Result.success(map);
                }else{
                    return Result.info(CodeMsg.PASSWORD_ERROR);
                }
            }else{
                return Result.info(CodeMsg.USER_NOT_FOUND);
            }
        }else{
            return Result.info(CodeMsg.USER_PASSWORD_EMPTY);
        }
    }

    public void logout(){

        //SecurityContextHolder.clearContext();
    }

    public Result<Map<String,Object>> register(UserVo userVo) {
        String userName = userVo.getUserName();
        String password = userVo.getPassword();
        OmUser user = iOmUserService.findByUserName(userName);
        if(user != null){
            return Result.info(CodeMsg.USER_EXIST);
        }else{
            String cryptPassword = this.bCryptPasswordEncoder.encode(password);
            user = new OmUser();
            user.setOmUserId(UUID.randomUUID().toString().replace("-",""));
            user.setOmUserName(userName);
            user.setOmUserPasswd(cryptPassword);
            user.setOmUserAddress(userVo.getAddress());
            String customName = userVo.getCustomName();
            user.setOmUserCustomname(StringUtils.isEmpty(customName) ? userName : customName);
            user.setOmUserBalance(BigDecimal.valueOf(0));
            user.setOmUserEmail(userVo.getEmail());
            user.setOmUserPhone(userVo.getPhone());
            iOmUserService.save(user);
            if(user.getOmUserId() != null){
                OmUserPermission omUserPermission = new OmUserPermission();
                omUserPermission.setOmUpUserid(user.getOmUserId());
                omUserPermission.setOmUpRoleid(1);
                omUserPermissionService.save(omUserPermission);
            }
            return this.login(userName,password,true);
        }
    }

    public UserVo getUserInfo(String id,String name,String phone,String email) {
        QueryWrapper<OmUser> queryWrapper = new QueryWrapper<OmUser>();
        queryWrapper.eq(!StringUtils.isVoid(id),"om_user_id", id)
                .eq(!StringUtils.isVoid(name),"om_user_name", name)
                .eq(!StringUtils.isVoid(phone),"om_user_phone", phone)
                .eq(!StringUtils.isVoid(email),"om_user_email", email);
        return iOmUserService.convertOmUserToUserVo(iOmUserService.getOne(queryWrapper));
    }

    public UserVo updateUserInfo(UserVo userVo) {
        OmUser user = iOmUserService.convertUserVoToOmUser(userVo);
        iOmUserService.updateById(user);
        return userVo;
    }

    //未使用
    public List<UserVo> getUserInfoList() {
        return iOmUserService.convertOmUserListToUserVoList(iOmUserService.list());
    }


    public List<UserVo> getUserBySelect(UserVo user) {
        QueryWrapper<OmUser> queryWrapper = new QueryWrapper<OmUser>();
        String name = user.getUserName();
        String phone = user.getPhone();
        String email = user.getEmail();
        queryWrapper.eq(!StringUtils.isVoid(name),"om_user_name", name)
                .eq(!StringUtils.isVoid(phone),"om_user_phone", phone)
                .eq(!StringUtils.isVoid(email),"om_user_email", email);
        return iOmUserService.convertOmUserListToUserVoList(iOmUserService.list(queryWrapper));
    }

    public List<PermissionVo> getPermissionsList(){
        return omPermissionService.convertOmPermissionToOmPermissionVoList(omPermissionService.list());
    }

    public List<Integer> getUserPermissionByUserId(String userId) {
        return omUserPermissionService.getRoleIdListByUserId(userId,true);
    }

    public Boolean setUserPermissionByUserId(String userId, List<Integer> roleIds) {
        List<OmUserPermission> newUserRoleList = new ArrayList<>();
        if(roleIds.isEmpty()){
            roleIds.add(1);
        }
        for (Integer i : roleIds) {
            OmUserPermission omUserPermission = new OmUserPermission();
            omUserPermission.setOmUpId(UUID.randomUUID().toString().replace("-",""));
            omUserPermission.setOmUpUserid(userId);
            omUserPermission.setOmUpRoleid(i);
            newUserRoleList.add(omUserPermission);
        }
        QueryWrapper<OmUserPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isVoid(userId),"om_up_userid",userId);
        omUserPermissionService.remove(queryWrapper);
        return omUserPermissionService.saveBatch(newUserRoleList);
    }
}
