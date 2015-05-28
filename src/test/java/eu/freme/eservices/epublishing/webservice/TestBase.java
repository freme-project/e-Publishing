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
        metadata.setAuthors(authors);
        toc.add(new Section("Chapter 1", "01.xhtml"));
        toc.add(new Section("Chapter 2", "02.xhtml"));
        metadata.setTableOfContents(toc);
        metadata.setCoverImage(getCoverImagePathInZip());
        
        return metadata;
    }
}