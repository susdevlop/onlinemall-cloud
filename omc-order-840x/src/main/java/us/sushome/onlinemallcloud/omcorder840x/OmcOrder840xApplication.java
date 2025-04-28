package us.sushome.onlinemallcloud.omcorder840x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"us.sushome.onlinemallcloud.omccommon","us.sushome.onlinemallcloud.omcorder840x"})
public class OmcOrder840xApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmcOrder840xApplication.class, args);
    }

}
