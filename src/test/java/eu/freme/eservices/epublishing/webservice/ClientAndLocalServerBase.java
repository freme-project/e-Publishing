package eu.freme.eservices.epublishing.webservice;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * <p>A base class for tests needing a client and a local server.</p>
 *
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class ClientAndLocalServerBase {
	// Acts as a server and as a client on this URL
	protected final String baseURL = "http://0.0.0.0:8080";
	protected Client client;
	protected WebTarget target;
	protected Main main;

	@Before
	public void setUp() throws Exception {
		// server
		main = new Main();
		main.createGrizzlyServer(baseURL);

		// client
		client = ClientBuilder.newClient();
		client.register(MultiPartFeature.class).register(MoxyJsonFeature.class);
		target = client.target(baseURL);
	}

	@After
	public void tearDown() throws Exception {
		// client
		client.close();

		// server
		main.close();
	}
}
