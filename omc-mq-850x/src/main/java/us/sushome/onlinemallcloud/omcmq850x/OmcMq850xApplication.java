package us.sushome.onlinemallcloud.omcmq850x;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDubbo
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"us.sushome.onlinemallcloud.omccommon",
                "us.sushome.onlinemallcloud.omcmq850x"})
@EnableFeignClients(basePackages = "us.sushome.onlinemallcloud.omccommon.api")
public class OmcMq850xApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmcMq850xApplication.class, args);
    }

}
