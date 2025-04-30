package us.sushome.onlinemallcloud.omccommon.saToken;

import cn.dev33.satoken.same.SaSameUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(SaSameUtil.SAME_TOKEN,SaSameUtil.getToken());
    }
}
