package eu.freme.eservices.epublishing;

import eu.freme.eservices.epublishing.exception.EPubCreationException;
import eu.freme.eservices.epublishing.exception.InvalidZipException;
import eu.freme.eservices.epublishing.webservice.Metadata;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class EPublishingService {
    
    private static final String tempFolderPath = System.getProperty("java.io.tmpdir");

    public byte[] createEPUB(Metadata metadata, InputStream in) throws InvalidZipException, EPubCreationException, IOException {
        // initialize the class that parses the input, and passes data to the EPUB creator
        String unzippedPath = tempFolderPath + File.separator + "freme_epublishing_" + System.currentTimeMillis();
        try (ZipInputStream zin = new ZipInputStream(in, StandardCharsets.UTF_8)) {
            Unzipper.unzip(zin,unzippedPath);
        } catch (IOException ex) {
            Logger.getLogger(EPublishingService.class.getName()).log(Level.SEVERE, null, ex);
            throw new InvalidZipException("Something went wrong with the provided Zip file. Make sure you are providing a valid Zip file.");
        }

        // initialize class that will create the EPUB file
        EPubCreator creator;
        try {
            creator = new EPubCreatorImpl(metadata, unzippedPath);
        } catch (IOException ex) {
            Logger.getLogger(EPublishingService.class.getName()).log(Level.SEVERE, null, ex);
            throw new EPubCreationException("Something went wrong during the creation of the EPUB. Make sure you provide the correct information.");
        }

        // write the EPUB "file", in this case to bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        creator.onEnd(bos);

        File tempFolder = new File(unzippedPath);
        FileUtils.deleteDirectory(tempFolder);
        
        return bos.toByteArray();
    }
}
