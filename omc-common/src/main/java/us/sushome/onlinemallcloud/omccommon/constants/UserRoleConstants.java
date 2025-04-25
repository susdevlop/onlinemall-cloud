package us.sushome.onlinemallcloud.omccommon.constants;

/**
 * RoleConstants
 *
 * @author star
 */
public final class UserRoleConstants {

    private UserRoleConstants() {
        throw new IllegalStateException("Cannot create instance of static constant class");
    }


    public static final String ROLE_ADMIN = "0";

    public static final String ROLE_USER = "1";

    public static final String GUEST_USER = "-2"; //暂时此处用于某些情况下没有权限的人的权限配置，其余的未来应当在数据库中取

}
