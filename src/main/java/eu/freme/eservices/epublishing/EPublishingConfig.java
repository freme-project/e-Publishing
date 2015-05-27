package eu.freme.eservices.epublishing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("eu.freme.eservices.epublishing")
public class EPublishingConfig {

    @Bean
    public EPublishingService getEntityAPI() {
        return new EPublishingService();
    }
}