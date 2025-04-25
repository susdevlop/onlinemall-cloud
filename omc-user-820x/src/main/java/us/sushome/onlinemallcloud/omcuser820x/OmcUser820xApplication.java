package us.sushome.onlinemallcloud.omcuser820x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Import;
//import us.sushome.onlinemallcloud.omccommon.config.MybatisPlusConfig;

@SpringBootApplication(scanBasePackages = {"us.sushome.onlinemallcloud.omccommon","us.sushome.onlinemallcloud.omcuser820x"})
public class OmcUser820xApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmcUser820xApplication.class, args);
    }
}
