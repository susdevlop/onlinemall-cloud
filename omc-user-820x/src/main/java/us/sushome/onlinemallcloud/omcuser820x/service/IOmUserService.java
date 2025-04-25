package us.sushome.onlinemallcloud.omcuser820x.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omcuser820x.dao.OmUserToUserVoMapper;
import us.sushome.onlinemallcloud.omcuser820x.model.OmUser;
import us.sushome.onlinemallcloud.omcuser820x.dao.OmUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * $!{table.comment} 服务类
 * </p>
 *
 * @author sushome
 * @since 2025-03-25
 */
@Service
public class IOmUserService extends ServiceImpl <OmUserMapper, OmUser> {
    private static Logger logger = LoggerFactory.getLogger(IOmUserService.class);


    @Resource
    private OmUserToUserVoMapper omUserToUserVoMapper;

    public OmUser findByUserName(String userName) {
        QueryWrapper<OmUser> queryWrapper = new QueryWrapper<OmUser>();
        queryWrapper.eq("om_user_name", userName); // user_name 是数据库表中的列名
        return this.getOne(queryWrapper);
    }

    public UserVo convertOmUserToUserVo(OmUser omUser) {
        return omUserToUserVoMapper.omUserToUserVo(omUser);
    }

    public OmUser convertUserVoToOmUser(UserVo userVo) {
        return omUserToUserVoMapper.userVoToOmUser(userVo);
    }

    public List<UserVo> convertOmUserListToUserVoList(List<OmUser> omUserList) {
        return omUserToUserVoMapper.omUserListToUserVoList(omUserList);
    }
}