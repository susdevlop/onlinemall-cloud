package us.sushome.onlinemallcloud.omcorder840x;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDubbo
@SpringBootApplication(scanBasePackages = {"us.sushome.onlinemallcloud.omccommon","us.sushome.onlinemallcloud.omcorder840x"})
@EnableFeignClients(basePackages = "us.sushome.onlinemallcloud.omccommon.api")
public class OmcOrder840xApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmcOrder840xApplication.class, args);
    }

}
