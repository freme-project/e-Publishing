package eu.freme.eservices.epublishing;

import eu.freme.eservices.epublishing.webservice.Metadata;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class EPublishingService {
    
    private static final String tempFolderPath = System.getProperty("java.io.tmpdir");

    public byte[] createEPUB(Metadata metadata, InputStream in) throws IOException {
        // initialize the class that parses the input, and passes data to the EPUB creator
        String unzippedPath = tempFolderPath + File.separator + "freme_epublishing_" + System.currentTimeMillis();
        try (ZipInputStream zin = new ZipInputStream(in, StandardCharsets.UTF_8)) {
            Unzipper.unzip(zin,unzippedPath);
        }

        // initialize class that will create the EPUB file
        EPubCreator creator = new EPubCreatorImpl(metadata, unzippedPath);

        // write the EPUB "file", in this case to bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        creator.onEnd(bos);

        File tempFolder = new File(unzippedPath);
        FileUtils.deleteDirectory(tempFolder);
        
        return bos.toByteArray();
    }
}
