package eu.freme.eservices.epublishing;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@SpringBootApplication
//@ComponentScan("eu.freme.eservices.epublishing")
@Component
public class EPublishingConfig {

    @Bean
    public EPublishingService getEntityAPI() {
        return new EPublishingService();
    }
}