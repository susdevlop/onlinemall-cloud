package us.sushome.onlinemallcloud.omccommon.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.constants.SecurityConstants;
import us.sushome.onlinemallcloud.omccommon.constants.UserRoleConstants;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Jwt 工具类，用于生成、解析与验证 token
 *
 * @author star
 **/
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final byte[] secretKey = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);

    private JwtUtils() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    //创建前端可用的 mqtt 密码token,已弃用
    public static String generateMqttTokenByUserId(String userId) {
        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim(SecurityConstants.TOKEN_USER_ID_CLAIM,userId)
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.MQTT_JWT_SECRET_KEY)
                .compact();
    }

    /**
     * 根据用户名和用户角色生成 token
     *
     * @param userName   用户名
     * @param role      用户角色
     * @param isRemember 是否记住我
     * @return 返回生成的 token
     */
    public static String generateToken(String userName, String userId,String role, boolean isRemember) {
        System.out.println("userName: " + userName);
        System.out.println("role: " + role);
        System.out.println("isRemember: " + isRemember);
        byte[] jwtSecretKey = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);
        // 过期时间
        long expiration = isRemember ? SecurityConstants.EXPIRATION_REMEMBER_TIME : SecurityConstants.EXPIRATION_TIME;
        // 生成 token
        String token = Jwts.builder()
                // 生成签证信息
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey), SignatureAlgorithm.HS256)
                .setSubject(userName)
                .claim(SecurityConstants.TOKEN_ROLE_CLAIM, role)
                .claim(SecurityConstants.TOKEN_USER_ID_CLAIM,userId)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setIssuedAt(new Date())
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                // 设置有效时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
        return token;
    }

    /**
     * 验证 token 是否有效
     *
     * <p>
     * 如果解析失败，说明 token 是无效的
     *
     * @param token token 信息
     * @return 如果返回 true，说明 token 有效
     */
    public static boolean validateToken(String token) {
        try {
            getTokenBody(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Request to parse expired JWT : {} failed : {}", token, e.getMessage());
            throw new RuntimeException("token已过期");
        } catch (UnsupportedJwtException e) {
            logger.warn("Request to parse unsupported JWT : {} failed : {}", token, e.getMessage());
            throw new RuntimeException("token不受支持");
        } catch (MalformedJwtException e) {
            logger.warn("Request to parse invalid JWT : {} failed : {}", token, e.getMessage());
            throw new RuntimeException("token无效");
        } catch (IllegalArgumentException e) {
            logger.warn("Request to parse empty or null JWT : {} failed : {}", token, e.getMessage());
            throw new RuntimeException("token为空或null");
        }
    }


    public static String getPermissionUserId(String token) {
        Claims claims = getTokenBody(token);
        // 获取用户角色字符串
        return (String) claims.get(SecurityConstants.TOKEN_USER_ID_CLAIM);
    }

    /**
     * 根据 token 获取用户认证信息
     *
     * @param token token 信息
     * @return 返回用户认证信息
     */
    public static Authentication getAuthentication(String token) {
        logger.info("SecurityContextHolder.getContext().getAuthentication()"+ SecurityContextHolder.getContext().getAuthentication());
        Claims claims = getTokenBody(token);
        // 获取用户角色字符串
        String roleId = (String) claims.get(SecurityConstants.TOKEN_ROLE_CLAIM);
        System.out.println("===========");
        System.out.println("获取用户角色字符串:"+roleId);
        System.out.println("===========");

        List<SimpleGrantedAuthority> authorities;
        // 如果 roleId 不为空，解析为 List<SimpleGrantedAuthority>
        if (roleId != null && !roleId.isEmpty()) {
            // 将 roleId 以逗号分隔，并映射为 SimpleGrantedAuthority
            authorities = Arrays.stream(roleId.split(","))
                    .map(role -> new SimpleGrantedAuthority(role.trim()))
                    .collect(Collectors.toList());
        } else {
            // 如果没有角色，默认使用 GUEST_USER 权限
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(UserRoleConstants.GUEST_USER)
            );
        }

        // 获取用户名
        String userName = claims.getSubject();
        // 获取用户ID
        String userId = (String) claims.get(SecurityConstants.TOKEN_USER_ID_CLAIM);
        UserVo userVo = new UserVo();
        userVo.setUserName(userName);
        userVo.setUserId(userId);
        return new UsernamePasswordAuthenticationToken(userVo, token, authorities);
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build() // 创建 JwtParser 实例
                .parseClaimsJws(token) // 解析 JWT 令牌
                .getBody(); // 获取 Claims
    }
}
