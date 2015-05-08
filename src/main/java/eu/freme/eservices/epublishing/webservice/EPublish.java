package eu.freme.eservices.epublishing.webservice;

import com.google.gson.Gson;
import eu.freme.eservices.epublishing.EPubCreator;
import eu.freme.eservices.epublishing.EPubCreatorImpl;
import eu.freme.eservices.epublishing.Unzipper;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;

/**
 * <p>e-publishing web services.</p>
 * <p>
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
@Path("epublish/html")
public class EPublish {
	private static final Logger logger = LoggerFactory.getLogger(EPublish.class);
        private static final String tempFolderPath = "/tmp";

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/epub+zip")
	public byte[] htmlToEPub(@FormDataParam("htmlZip") InputStream in, @FormDataParam("metadata") String jMetadata) throws IOException {
                logger.info(jMetadata);
                
                Gson gson = new Gson();
                Metadata metadata = gson.fromJson(jMetadata, Metadata.class);

		// initialize the class that parses the input, and passes data to the EPUB creator
		ZipInputStream zin = new ZipInputStream(in, StandardCharsets.UTF_8);
                String unzippedPath = tempFolderPath + File.separator + "freme_epublishing_" + System.currentTimeMillis();
                Unzipper.unzip(zin, unzippedPath);
                
                // initialize class that will create the EPUB file
		EPubCreator creator = new EPubCreatorImpl(metadata, unzippedPath);
		// create an EPUB (in memory)
		//zip.process(zin, creator);

		// write the EPUB "file", in this case to bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		creator.onEnd(bos);
                
                File tempFolder =  new File(unzippedPath);
                FileUtils.deleteDirectory(tempFolder);

		// return the EPUB file as bytes.
		return bos.toByteArray();
	}
}
