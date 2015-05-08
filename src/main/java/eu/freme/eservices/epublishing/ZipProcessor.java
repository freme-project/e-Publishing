package eu.freme.eservices.epublishing;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>Reads a ZIP inputstream and passes the contents to an EPubCreator</p>
 *
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class ZipProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ZipProcessor.class);

	public void process(final ZipInputStream in, final EPubCreator creator) throws IOException {
		ZipEntry entry;
		while ((entry = in.getNextEntry()) != null) {
			String name = entry.getName();
			if (entry.isDirectory()) {
				logger.debug(" Found directory {}. Skipping!", name);
				continue;
			}

			long size = entry.getSize();
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int)size)) {
				IOUtils.copy(in, bos);

				if (name.endsWith(".html") || name.endsWith(".xhtml") || name.endsWith(".css") || name.endsWith(".txt")) {    // TODO: check if there is an easier way?
					logger.debug("Found text file {}", name);
					creator.onText(name, bos.toString(StandardCharsets.UTF_8.name()));
				} else {
					logger.debug("Found binary file {}", name);
					creator.onBinary(name, bos.toByteArray());
				}
			}
		}
	}
}
