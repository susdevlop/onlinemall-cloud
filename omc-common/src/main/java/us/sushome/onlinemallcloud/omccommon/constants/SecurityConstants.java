package us.sushome.onlinemallcloud.omccommon.constants;

//@Configuration

public class SecurityConstants {

    public static String AUTH_LOGIN_URL = "/openApi/user/login";
    public static String JWT_SECRET_KEY = "p2s5v8y/B?E(H+MbQeThVmYq3t6w9z$C&F)J@NcRfUjXnZr4u7x!A%D*G-KaPdS";
    public static String MQTT_JWT_SECRET_KEY = "c3VzX21xdHRfand0X3VzZV92ZXJ5X3N0cm9nZV9sZW5ndGg=";
    public static String TOKEN_PREFIX = "Bearer";
    public static String TOKEN_HEADER = "Authorization";
    public static String TOKEN_TYPE = "JWT";
    public static String TOKEN_ROLE_CLAIM = "role";
    public static String TOKEN_USER_ID_CLAIM = "USER_ID";
    public static String TOKEN_BUILD_TIME_CLAIM = "BUILD_TIME";
    public static String TOKEN_ISSUER = "security";
    public static String TOKEN_AUDIENCE = "security-all";
    public static long EXPIRATION_TIME = 7200;
    public static long EXPIRATION_REMEMBER_TIME = 86400;//1 day
}
