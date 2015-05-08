# FREME e-Publishing service

## Building

### Requirements

* Java >= 1.8
* Maven 3
* Git

### Build

    cd e-Services/e-publishing

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
