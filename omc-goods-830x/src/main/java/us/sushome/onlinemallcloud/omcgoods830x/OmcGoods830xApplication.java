package us.sushome.onlinemallcloud.omcgoods830x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"us.sushome.onlinemallcloud.omccommon","us.sushome.onlinemallcloud.omcgoods830x"})
@EnableFeignClients(basePackages = "us.sushome.onlinemallcloud.omccommon.api")
public class OmcGoods830xApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmcGoods830xApplication.class, args);
    }

}
