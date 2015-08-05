package eu.freme.eservices.epublishing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="")
public class MissingMetadataException extends Exception {
    
    public MissingMetadataException(String message) {
        super(message);
    }
}
