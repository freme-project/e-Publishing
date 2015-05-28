package eu.freme.eservices.epublishing.webservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

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
    
    @Test
    public void testTitles() throws IOException {
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
    
    @Test
    public void testAuthors() throws IOException {
        String anotherAuthor = "Nick Borth";

        Metadata metadata = getSimpleMetadataForZip();
        metadata.addAuthor(anotherAuthor);

        byte[] epub = getePublishingService().createEPUB(metadata, new FileInputStream(getZipFile()));

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(new ByteArrayInputStream(epub), new FileOutputStream(ePubFile));

        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<Author> bookAuthors = b.getMetadata().getAuthors();
        List<String> bookAuthorsNames = new ArrayList<>();

        for (Author a : bookAuthors) {
            bookAuthorsNames.add(a.getFirstname() + " " + a.getLastname());
        }

        System.out.println(bookAuthorsNames);
        Assert.assertTrue("All authors are in the EPUB.", bookAuthorsNames.containsAll(metadata.getAuthors()));
        Assert.assertTrue("All EPUB authors are in the given authors.", metadata.getAuthors().containsAll(bookAuthorsNames));
    }
    
    @Test
    public void testCoverImage() throws IOException {
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