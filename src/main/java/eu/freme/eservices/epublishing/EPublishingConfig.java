package eu.freme.eservices.epublishing;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class EPublishingConfig {

    @Bean
    @SuppressWarnings("unused")
    public EPublishingService getEntityAPI() {
        return new EPublishingService();
    }
}