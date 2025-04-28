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


    //public static final String ROLE_ADMIN = "0";
    //
    //public static final String ROLE_USER = "1";

    public static final String GUEST_USER = "-2"; //暂时此处用于某些情况下没有权限的人的权限配置，其余的未来应当在数据库中取


    public static final Integer ADMIN_ROLE_ID = 0;
    public static final String ADMIN_ROLE_NAME = "admin";

    public static final Integer USER_ROLE_ID = 1;
    public static final String USER_ROLE_NAME = "user";

    public static final Integer USER_MANAGER_ROLE_ID = 20;
    public static final String USER_MANAGER_ROLE_NAME = "user_manager";

    public static final Integer GOODS_MANAGER_ROLE_ID = 30;
    public static final String GOODS_MANAGER_ROLE_NAME = "goods_manager";

    public static final Integer ORDER_MANAGER_ROLE_ID = 40;
    public static final String ORDER_MANAGER_ROLE_NAME = "order_manager";
}
