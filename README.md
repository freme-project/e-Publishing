# FREME e-Publishing service

**This repository is deprecated. It has moved to [e-services repository](https://github.com/freme-project/e-services).**

## Building

### Requirements

* Java >= 1.8
* Maven 3
* Git

### Build

    cd e-Publishing

Normal build and packaging:

    mvn package

Without running the JUnit tests:

    mvn -DskipTests package

## Running

After building, one can invoke the standalone jar, to be found in the `target` subdirectory. To get help, just run

    java -jar e-publishing-<version>-jar-with-dependencies.jar -h

Output:

    usage: java -jar <this jar file>
     -b,--baseurl <arg>   Base URL for our webservices, default:
                          freme.test.iminds.be
     -h,--help            Prints this message

### Example: starting the service at domain `localhost` port `8900`

    java -jar e-publishing-0.0.1-SNAPSHOT-jar-with-dependencies.jar -b http://localhost:8900

Output:

    2015-04-22 16:46:34,886 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(33): Creating Grizzly Server
    Apr 22, 2015 4:46:35 PM org.glassfish.grizzly.http.server.NetworkListener start
    INFO: Started listener bound to [localhost:8900]
    Apr 22, 2015 4:46:35 PM org.glassfish.grizzly.http.server.HttpServer start
    INFO: [HttpServer] Started.
    2015-04-22 16:46:35,775 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(48): Created Grizzly Server
    2015-04-22 16:46:35,777 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(49): base uri: http://localhost:8900
    2015-04-22 16:46:35,777 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(50): WADL: http://localhost:8900/application.wadl
    Hit enter to stop...

Grizzly logs by default to the std error, which can be confusing. To work around this, start with the `-Djava.util.logging.config.file=logging.properties`
vm parameter:

    java -Djava.util.logging.config.file=logging.properties -jar e-publishing-0.0.1-SNAPSHOT-jar-with-dependencies.jar -b http://localhost:8900

Now the output is something like:

    2015-04-22 16:57:04,630 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(33): Creating Grizzly Server
    2015-04-22 16:57:05,531 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(48): Created Grizzly Server
    2015-04-22 16:57:05,532 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(49): base uri: http://localhost:8900
    2015-04-22 16:57:05,533 INFO [main] be.ugent.mmlab.freme.epublishing.webservice.Main(50): WADL: http://localhost:8900/application.wadl
    Hit enter to stop...


The file `logging.properties` can be found in the source directory `src/main/resources/logging.properties` or in the 
generated target directory `target/classes/logging.properties`.

## License

Copyright 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
Institut für Angewandte Informatik e. V. an der Universität Leipzig,
Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This project uses 3rd party tools. You can find the list of 3rd party tools including their authors and licenses [here](LICENSE-3RD-PARTY).
