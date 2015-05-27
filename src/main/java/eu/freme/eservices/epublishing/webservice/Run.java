package eu.freme.eservices.epublishing.webservice;

import eu.freme.eservices.epublishing.EPublishingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
@SpringBootApplication
public class Run {

    private static final Logger logger = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {
        logger.info("Starting e-Publishing service");
        SpringApplication.run(EPublishingConfig.class);
    }
}
