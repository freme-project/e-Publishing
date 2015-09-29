/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * 					Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * 					Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.eservices.epublishing.webservice;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * <p>
 * A base class for tests needing a client and a local server.</p>
 *
 * <p>
 * Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class ClientAndLocalServerBase {

    // Acts as a server and as a client on this URL

    protected final String baseURL = "http://0.0.0.0:8080";
    protected Client client;
    protected WebTarget target;
    //protected Main main;
    protected Run webService;

    @Before
    public void setUp() throws Exception {
		// server
        webService = new Run();
        webService.run();

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
        webService.close();
    }
}
