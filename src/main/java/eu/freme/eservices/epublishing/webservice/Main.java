package eu.freme.eservices.epublishing.webservice;

import org.apache.commons.cli.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

/**
 * <p>Main entry point for starting the web e-Publishing service</p>
 * <p>
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class Main implements Closeable {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpServer server = null;

	/**
	 * Creates and starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 *
	 * @param baseUri base URI for the server
	 */
	public void createGrizzlyServer(String baseUri) {
		if (server == null) {
			logger.info("Creating Grizzly Server");
			// create a resource config
			final ResourceConfig rc = new ResourceConfig()
					// scans for JAX-RS resources and providers in this list of packages
					.packages("eu.freme.eservices.epublishing.webservice")
					.register(MultiPartFeature.class)

					// Comment this out if you ever need to process JSON:
					// because we register also the MultiPartFeature class, we also need to explicitly register moxy. See the note in
					// https://jersey.java.net/nonav/documentation/latest/user-guide.html#json.moxy
					.register(MoxyJsonFeature.class);

			// create and start a new instance of grizzly http server
			// exposing the Jersey application at baseUri
			server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
			logger.info("Created Grizzly Server");
			logger.info("base uri: {}", baseUri);
			logger.info("WADL: {}{}", baseUri, "/application.wadl");
		} else {
			logger.info("Server already created.");
		}
	}

	@Override
	public void close() throws IOException {
		if (server != null) {
			logger.info("Destroying Grizzly Server");
			server.shutdownNow();
			server = null;
			logger.info("Destroyed Grizzly Server");
		}
	}

	/**
	 * Do the command line overhead, including help and error handling.
	 *
	 * Exits in case of help or error.
	 *
	 * @return The processed command line, ready for consumption.
	 */
	private static CommandLine getCommandLine(String[] args, String defaultBaseURL) {
		Options options = new Options();
		Option helpOption = new Option("h", "help", false, "Prints this message");
		options.addOption(helpOption);

		options.addOption("b", "baseurl", true, "Base URL for our webservices, default: " + defaultBaseURL);
		// add other options here...

		CommandLine ret = null;
		try {
			CommandLineParser parser = new BasicParser();
			ret = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Command line parsing failed. Reason: " + e.getMessage() + "\n");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar <this jar file>", options);
			System.exit(2);
		}

		if (ret.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar <this jar file>", options);
			System.exit(0);
		}

		return ret;
	}


	public static void main(String[] args) throws IOException {
		String defaultBaseURL = "freme.test.iminds.be";
		CommandLine line = getCommandLine(args, defaultBaseURL);
		String baseURL = line.getOptionValue("b", defaultBaseURL);
		try (Main main = new Main()) {
			main.createGrizzlyServer(baseURL);

			System.out.println("Hit enter to stop...");
			int byteRead = System.in.read();
			System.out.println(" Thanks for giving me byte " + byteRead);
		}
	}
}
