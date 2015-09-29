/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
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

import eu.freme.eservices.epublishing.EPublishingService;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public abstract class TestBase {
    
    private final EPublishingService ePublishingService;
    private final File zipFile, coverImage;
    private final String zipFilePath = "src/test/resources/alice.zip";
    private final String coverImagePath = "src/test/resources/cover.jpeg";
    private final String coverImagePathInZip = "cover.jpeg";

    public TestBase() {
        this.ePublishingService = new EPublishingService();
        this.zipFile = new File(zipFilePath);
        this.coverImage = new File(coverImagePath);
    }

    protected EPublishingService getePublishingService() {
        return ePublishingService;
    }

    protected File getZipFile() {
        return zipFile;
    }

    protected File getCoverImage() {
        return coverImage;
    } 

    protected String getCoverImagePathInZip() {
        return coverImagePathInZip;
    }
    
    protected Metadata getSimpleMetadataForZip() {
        String title = "Alice in Utopia";
        String author = "Joske Vermeulen";

        Metadata metadata = new Metadata();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<Section> toc = new ArrayList<>();
        titles.add(title);
        authors.add(author);
        metadata.setTitles(titles);
        //metadata.setAuthors(authors);
        toc.add(new Section("Chapter 1", "01.xhtml"));
        toc.add(new Section("Chapter 2", "02.xhtml"));
        metadata.setTableOfContents(toc);
        metadata.setCoverImage(getCoverImagePathInZip());
        
        return metadata;
    }
}