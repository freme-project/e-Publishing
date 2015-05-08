package eu.freme.eservices.epublishing.webservice;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.junit.Assert;

/**
 * <p>
 * Basic e-publishing test</p>
 *
 * <p>
 * Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class EPubTest extends ClientAndLocalServerBase {

    @Test
    public void TestAlice() throws IOException {
        String title = "Alice in Utopia";
        String author = "Joske Vermeulen";
        String author2 = "Marieke Vermalen";
        String description = "Dit is een heel mooi boekske.";
        String identifier = "urn:ean:1234-7956-1356-1123";

        File zippedHTMLFile = new File("src/test/resources/alice.zip");
        System.out.println("Converting " + zippedHTMLFile + " to EPUB format.");

        Metadata metadata = new Metadata();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        titles.add(title);
        authors.add(author);
        authors.add(author2);
        metadata.setTitles(titles);
        metadata.setAuthors(authors);
        metadata.setDescription(description);
        metadata.setIdentifier(identifier);

        Gson gson = new Gson();

        // create body part containing the ePub file
        FileDataBodyPart ePubFilePart = new FileDataBodyPart("htmlZip", zippedHTMLFile);

        // the form, to which parameters and binary content can be added
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(ePubFilePart);			// add ePub
        multiPart.field("metadata", gson.toJson(metadata));

        // now send it to the service
        Response response = target.path("epublish/html").request().post(Entity.entity(multiPart, multiPart.getMediaType()));
        InputStream in = response.readEntity(InputStream.class);

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(in, new FileOutputStream(ePubFile));
        
        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<String> bookTitles = b.getMetadata().getTitles();
        List<Author> bookAuthors = b.getMetadata().getAuthors();
        List<String> bookAuthorsNames = new ArrayList<String>();
        
        bookAuthors.stream().forEach((a) -> {
            bookAuthorsNames.add(a.getFirstname() + " " + a.getLastname());
        });

        Assert.assertTrue(bookTitles.containsAll(titles));
        Assert.assertTrue(titles.containsAll(bookTitles));
        System.out.println(bookAuthorsNames);
        Assert.assertTrue(bookAuthorsNames.containsAll(authors));
        Assert.assertTrue(authors.containsAll(bookAuthorsNames));
    }

    @Test
    public void TestSections() throws IOException {
        String title = "Alice in Utopia";
        String author = "Joske Vermeulen";

        File zippedHTMLFile = new File("src/test/resources/alice.zip");
        System.out.println("Converting " + zippedHTMLFile + " to EPUB format.");

        Metadata metadata = new Metadata();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<Section> toc = new ArrayList<>();
        titles.add(title);
        authors.add(author);
        metadata.setTitles(titles);
        metadata.setAuthors(authors);
        toc.add(new Section("Chapter 1", "01.xhtml"));
        toc.add(new Section("Chapter 2", "02.xhtml"));
        metadata.setTableOfContents(toc);
        
        Gson gson = new Gson();

        // create body part containing the ePub file
        FileDataBodyPart ePubFilePart = new FileDataBodyPart("htmlZip", zippedHTMLFile);

        // the form, to which parameters and binary content can be added
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(ePubFilePart);			// add ePub
        multiPart.field("metadata", gson.toJson(metadata));

        // now send it to the service
        Response response = target.path("epublish/html").request().post(Entity.entity(multiPart, multiPart.getMediaType()));
        InputStream in = response.readEntity(InputStream.class);

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        System.out.println("Writing result to " + ePubFile);
        IOUtils.copy(in, new FileOutputStream(ePubFile));
        
        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));
        List<String> bookTitles = b.getMetadata().getTitles();
        List<Author> bookAuthors = b.getMetadata().getAuthors();
        List<String> bookAuthorsNames = new ArrayList<String>();
        
        bookAuthors.stream().forEach((a) -> {
            bookAuthorsNames.add(a.getFirstname() + " " + a.getLastname());
        });

        Assert.assertTrue("All titles are in the EPUB.", bookTitles.containsAll(titles));
        Assert.assertTrue("All EPUB titles are in the given titles.", titles.containsAll(bookTitles));
        System.out.println(bookAuthorsNames);
        Assert.assertTrue("All authors are in the EPUB.", bookAuthorsNames.containsAll(authors));
        Assert.assertTrue("All EPUB authors are in the given authors.", authors.containsAll(bookAuthorsNames));
    }
    
    @Test
    public void TestCoverImage() throws IOException {
        String title = "Alice in Utopia";
        String author = "Joske Vermeulen";
        String coverImage = "cover.jpeg";

        File zippedHTMLFile = new File("src/test/resources/alice.zip");
        File coverFile = new File("src/test/resources/" + coverImage);

        Metadata metadata = new Metadata();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<Section> toc = new ArrayList<>();
        titles.add(title);
        authors.add(author);
        metadata.setTitles(titles);
        metadata.setAuthors(authors);
        toc.add(new Section("Chapter 1", "01.xhtml"));
        toc.add(new Section("Chapter 2", "02.xhtml"));
        metadata.setTableOfContents(toc);
        metadata.setCoverImage(coverImage);
        
        Gson gson = new Gson();

        // create body part containing the ePub file
        FileDataBodyPart ePubFilePart = new FileDataBodyPart("htmlZip", zippedHTMLFile);

        // the form, to which parameters and binary content can be added
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(ePubFilePart);			// add ePub
        multiPart.field("metadata", gson.toJson(metadata));

        // now send it to the service
        Response response = target.path("epublish/html").request().post(Entity.entity(multiPart, multiPart.getMediaType()));
        InputStream in = response.readEntity(InputStream.class);

        // write it to a (temporary) file
        File ePubFile = File.createTempFile("alice", ".epub");
        IOUtils.copy(in, new FileOutputStream(ePubFile));
        
        //read file and perform checks
        EpubReader r = new EpubReader();
        Book b = r.readEpub(new FileInputStream(ePubFile));

        Resource cover = b.getCoverImage();
        Assert.assertEquals("Covers have the same size.", cover.getSize(), coverFile.length());
    }
}
