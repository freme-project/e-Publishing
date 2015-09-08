/**
 * Copyright (C) 2015 Deutsches Forschungszentrum für Künstliche Intelligenz (http://freme-project.eu)
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

import eu.freme.eservices.epublishing.exception.EPubCreationException;
import eu.freme.eservices.epublishing.exception.InvalidZipException;
import eu.freme.eservices.epublishing.exception.MissingMetadataException;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.CreatorContributor;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class EPUBCreationTest extends TestBase {
    
    @Test
    public void testTOC() {
        
    }
    
    @Test
    public void testWithoutTOC() {
        
    }
    
    @Ignore
    @Test
    public void testWithoutTitle() throws FileNotFoundException, InvalidZipException, EPubCreationException, IOException, MissingMetadataException {
        Metadata metadata = getSimpleMetadataForZip();
        metadata.setTitles(new ArrayList<String>());
        
        byte[] epub = getePublishingService().createEPUB(metadata, new FileInputStream(getZipFile()));

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(new ByteArrayInputStream(epub), new FileOutputStream(ePubFile));

        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<String> bookTitles = b.getMetadata().getTitles();

        Assert.assertTrue("All titles are in the EPUB.", bookTitles.containsAll(metadata.getTitles()));
        Assert.assertTrue("All EPUB titles are in the given titles.", metadata.getTitles().containsAll(bookTitles));  
    }
    
    @Ignore
    @Test
    public void testWithoutAuthor() throws FileNotFoundException, InvalidZipException, EPubCreationException, IOException, MissingMetadataException {
        Metadata metadata = getSimpleMetadataForZip();
        //metadata.setAuthors(new ArrayList<String>());
        
        byte[] epub = getePublishingService().createEPUB(metadata, new FileInputStream(getZipFile()));

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(new ByteArrayInputStream(epub), new FileOutputStream(ePubFile));

        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<CreatorContributor> bookAuthors = b.getMetadata().getAuthors();
        List<String> bookAuthorsNames = new ArrayList<>();

        for (CreatorContributor a : bookAuthors) {
            bookAuthorsNames.add(a.getFirstname() + " " + a.getLastname());
        }

        System.out.println(bookAuthorsNames);
        Assert.assertTrue("All authors are in the EPUB.", bookAuthorsNames.containsAll(metadata.getAuthors()));
        Assert.assertTrue("All EPUB authors are in the given authors.", metadata.getAuthors().containsAll(bookAuthorsNames));
    }
    
    @Ignore
    @Test
    public void testTitles() throws IOException, InvalidZipException, EPubCreationException, MissingMetadataException {
        String anotherTitle = "Alice in Europe";
        
        Metadata metadata = getSimpleMetadataForZip();
        metadata.addTitle(anotherTitle);

        byte[] epub = getePublishingService().createEPUB(metadata, new FileInputStream(getZipFile()));

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(new ByteArrayInputStream(epub), new FileOutputStream(ePubFile));

        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<String> bookTitles = b.getMetadata().getTitles();

        Assert.assertTrue("All titles are in the EPUB.", bookTitles.containsAll(metadata.getTitles()));
        Assert.assertTrue("All EPUB titles are in the given titles.", metadata.getTitles().containsAll(bookTitles));    
    }
    
    @Ignore
    @Test
    public void testAuthors() throws IOException, InvalidZipException, EPubCreationException, MissingMetadataException {
        String anotherAuthor = "Nick Borth";

        Metadata metadata = getSimpleMetadataForZip();
        //metadata.addAuthor(anotherAuthor);

        byte[] epub = getePublishingService().createEPUB(metadata, new FileInputStream(getZipFile()));

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(new ByteArrayInputStream(epub), new FileOutputStream(ePubFile));

        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<CreatorContributor> bookAuthors = b.getMetadata().getAuthors();
        List<String> bookAuthorsNames = new ArrayList<>();

        for (CreatorContributor a : bookAuthors) {
            bookAuthorsNames.add(a.getFirstname() + " " + a.getLastname());
        }

        System.out.println(bookAuthorsNames);
        Assert.assertTrue("All authors are in the EPUB.", bookAuthorsNames.containsAll(metadata.getAuthors()));
        Assert.assertTrue("All EPUB authors are in the given authors.", metadata.getAuthors().containsAll(bookAuthorsNames));
    }
    
    @Ignore
    @Test
    public void testCoverImage() throws IOException, InvalidZipException, EPubCreationException, MissingMetadataException {
        byte[] epub = getePublishingService().createEPUB(getSimpleMetadataForZip(), new FileInputStream(getZipFile()));

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        IOUtils.copy(new ByteArrayInputStream(epub), new FileOutputStream(ePubFile));

        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));

        Resource cover = b.getCoverImage();
        Assert.assertEquals("Covers have the same size.", cover.getSize(), getCoverImage().length());
    }
}