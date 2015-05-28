package eu.freme.eservices.epublishing.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
@SpringBootApplication
@ComponentScan(basePackages = "eu.freme.eservices.epublishing")
public class Run {

    private static final Logger logger = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {
        logger.info("Starting e-Publishing service");
        SpringApplication.run(Run.class);
    }
}
