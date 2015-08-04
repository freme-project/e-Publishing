package eu.freme.eservices.epublishing;

import com.google.gson.Gson;
import eu.freme.eservices.epublishing.exception.EPubCreationException;
import eu.freme.eservices.epublishing.exception.InvalidZipException;
import eu.freme.eservices.epublishing.exception.MissingMetadataException;
import eu.freme.eservices.epublishing.webservice.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
@RestController
@SuppressWarnings("unused")
public class ServiceRestController {

    @Autowired
    EPublishingService epubAPI;

    @RequestMapping(value = "/e-publishing/html", method = RequestMethod.POST)
    public ResponseEntity<byte[]> htmlToEPub(@RequestParam("htmlZip") MultipartFile file, @RequestParam("metadata") String jMetadata) throws IOException, InvalidZipException, EPubCreationException, MissingMetadataException {
        Gson gson = new Gson();
        Metadata metadata = gson.fromJson(jMetadata, Metadata.class);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "Application/epub+zip");
        return new ResponseEntity<>(epubAPI.createEPUB(metadata, file.getInputStream()), headers, HttpStatus.OK);
    }
}