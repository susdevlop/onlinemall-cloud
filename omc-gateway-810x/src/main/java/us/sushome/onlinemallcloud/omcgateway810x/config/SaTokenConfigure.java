package us.sushome.onlinemallcloud.omcgateway810x.config;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;


@Configuration
public class SaTokenConfigure {
    private static Logger log = LoggerFactory.getLogger(SaTokenConfigure.class);
    @Bean
    public SaReactorFilter saReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**") // 拦截的路径
                .addExclude("/favicon.ico","/openApi/user/login","/openApi/user/register", "/actuator", "/actuator/**")
                .setAuth(obj -> {
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());
                    // 打印路径
                    ServerHttpRequest request = SaReactorSyncHolder.getExchange().getRequest();
                    RequestPath path = request.getPath();
                    log.info("---------- sa全局认证, 请求url:{}", path);
                })
                .setError(e -> {
                    log.error("---------- sa全局异常", e);
                    return SaResult.error(e.getMessage());
                });
    }


}
