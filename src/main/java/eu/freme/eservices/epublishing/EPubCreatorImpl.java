package eu.freme.eservices.epublishing;

import eu.freme.eservices.epublishing.webservice.Section;
import java.io.File;
import java.io.FileInputStream;
import nl.siegmann.epublib.bookprocessor.HtmlCleanerBookProcessor;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.BookProcessor;
import nl.siegmann.epublib.epub.BookProcessorPipeline;
import nl.siegmann.epublib.epub.EpubWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import nl.siegmann.epublib.domain.Date;
import nl.siegmann.epublib.domain.Identifier;
import nl.siegmann.epublib.domain.TOCReference;

/**
 * <p>
 * A class that generates an EPUB file from the contents of a number of
 * files.</p>
 *
 * <p>
 * Use this class only once per EPUB to create (not thread-safe)!</p>
 *
 * <p>
 * Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class EPubCreatorImpl implements EPubCreator {

    private final Book book;
    private final Metadata metadata;
    private final eu.freme.eservices.epublishing.webservice.Metadata ourMetadata;
    private final String unzippedPath;

    public EPubCreatorImpl(final eu.freme.eservices.epublishing.webservice.Metadata ourMetadata, final String unzippedPath) throws IOException {
        book = new Book();
        this.metadata = new Metadata();
        this.unzippedPath = unzippedPath;
        this.ourMetadata = ourMetadata;

        addAuthors(ourMetadata.getAuthors());
        addIllustrators(ourMetadata.getIllustrators());

        if (metadata.getLanguage() != null) {
            this.metadata.setLanguage(ourMetadata.getLanguage());
        }

        //TODO
        if (ourMetadata.getIdentifier() != null) {
            this.metadata.addIdentifier(new Identifier("test", ourMetadata.getIdentifier()));
        }

        if (ourMetadata.getTitles() != null) {
            this.metadata.setTitles(ourMetadata.getTitles());
        }

        if (ourMetadata.getSubjects() != null) {
            this.metadata.setSubjects(ourMetadata.getSubjects());
        }

        if (ourMetadata.getPublicationDate() != null) {
            this.metadata.addDate(new Date(ourMetadata.getPublicationDate().getTime()));
        }

        //TODO
        if (ourMetadata.getSource() != null) {
        }

        if (ourMetadata.getType() != null) {
            this.metadata.addType(ourMetadata.getType());
        }

        if (ourMetadata.getDescription() != null) {
            this.metadata.addDescription(ourMetadata.getDescription());
        }

        if (ourMetadata.getRights() != null) {
            ArrayList<String> rights = new ArrayList<>();
            rights.add(ourMetadata.getRights());
            this.metadata.setRights(rights);
        }

        if (ourMetadata.getTableOfContents() == null) {
            ourMetadata.setTableOfContents(createBestEffortTableOfContents(null));
        }
        
        createSections(ourMetadata.getTableOfContents(), null);

        if (ourMetadata.getCoverImage() != null) {
            addCoverImage(ourMetadata.getCoverImage());
        }

        copyUnaddedFilesFromZipToEpub(null);
    }

    private void addCoverImage(String coverImage) throws IOException {
        book.setCoverImage(new Resource(new FileInputStream(unzippedPath + File.separator + coverImage), coverImage));
    }

    private void addIllustrators(ArrayList<String> illustrators) {
        if (illustrators != null) {
            illustrators.stream().forEach((illustrator) -> {
                metadata.addContributor(new Author(illustrator));
            });
        }
    }

    private void addAuthors(ArrayList<String> authors) {
        if (authors != null) {
            authors.stream().forEach((author) -> {
                metadata.addAuthor(new Author(author));
            });
        }
    }

    private void createSections(ArrayList<Section> toc, TOCReference parentSection) throws IOException {
        for (Section section : toc) {
            Resource resource = new Resource(new FileInputStream(unzippedPath + File.separator + section.getResource()), section.getResource());

            TOCReference bookSection;
            if (parentSection == null) {
                bookSection = book.addSection(section.getTitle(), resource);
                //System.out.println(section.getTitle());
            } else {
                bookSection = book.addSection(parentSection, section.getTitle(), resource);
            }

            if (section.getSubsections() != null) {
                createSections(section.getSubsections(), bookSection);
            }
        }
    }

    private ArrayList<Section> createBestEffortTableOfContents(String parent) throws IOException {
        ArrayList<Section> sections = new ArrayList<>();
        File folder;

        if (parent == null || parent.equals("")) {
            folder = new File(unzippedPath);
        } else {
            folder = new File(unzippedPath + File.pathSeparator + parent);
        }

        File[] listOfFiles = folder.listFiles();

        Arrays.sort(listOfFiles, (File o1, File o2) -> o1.getName().compareTo(o2.getName()));

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile() && (listOfFile.getName().endsWith(".html") || listOfFile.getName().endsWith(".xhtml"))) {
                Section s;
                if (parent == null || parent.equals("")) {
                    s = new Section(listOfFile.getName().substring(0, listOfFile.getName().lastIndexOf(".")), listOfFile.getName());
                    //book.addSection(listOfFile.getName().substring(0, listOfFile.getName().lastIndexOf(".")), new Resource(new FileInputStream(listOfFile), listOfFile.getName()));
                } else {
                    s = new Section(listOfFile.getName().substring(0, listOfFile.getName().lastIndexOf(".")), parent + File.separator + listOfFile.getName());
                    //book.addSection(listOfFile.getName().substring(0, listOfFile.getName().lastIndexOf(".")), new Resource(new FileInputStream(listOfFile), parent + File.separator + listOfFile.getName()));
                }
                
                sections.add(s);
            } else if (listOfFile.isDirectory()) {
                if (parent == null || parent.equals("")) {
                    sections.addAll(createBestEffortTableOfContents(listOfFile.getName()));
                } else {
                    sections.addAll(createBestEffortTableOfContents(parent + File.separator + listOfFile.getName()));
                }
            }
        }
        
        return sections;
    }

    private void copyUnaddedFilesFromZipToEpub(String parent) throws IOException {
        File folder;

        if (parent == null || parent.equals("")) {
            folder = new File(unzippedPath);
        } else {
            folder = new File(unzippedPath + File.pathSeparator + parent);
        }

        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile() && !isFileAlreadyAdded(listOfFile)) {
                if (parent == null || parent.equals("")) {
                    book.addResource(new Resource(new FileInputStream(listOfFile), listOfFile.getName()));
                } else {
                    book.addResource(new Resource(new FileInputStream(listOfFile), parent + File.separator + listOfFile.getName()));
                }
            } else if (listOfFile.isDirectory()) {
                if (parent == null || parent.equals("")) {
                    copyUnaddedFilesFromZipToEpub(listOfFile.getName());
                } else {
                    copyUnaddedFilesFromZipToEpub(parent + File.separator + listOfFile.getName());
                }
            }
        }
    }
    
    private boolean isFileAlreadyAdded(File file) {
        String name = file.getAbsolutePath().replace(unzippedPath + File.separator, "");
        return Section.hasSectionWithResource(ourMetadata.getTableOfContents(), name) || name.equals(ourMetadata.getCoverImage());
    }

    @Override
    public void onText(String name, String contents) throws IOException {
        String href = getBaseName(name);
        Resource resource = new Resource(new StringReader(contents), href);
        if (name.endsWith("html")) {
            book.addSection("TO DO: chapter title", resource);
        } else if (name.endsWith(".css")) {
            book.getResources().add(resource);
        }
    }

    @Override
    public void onBinary(String name, byte[] contents) throws IOException {
        String href = getBaseName(name);
        Resource resource = new Resource(contents, href);
        book.addResource(resource);
    }

    @Override
    public void onEnd(OutputStream out) throws IOException {
        book.setMetadata(metadata);
        BookProcessor[] bookProcessors = {new HtmlCleanerBookProcessor() /*, AnotherBookProcessor, ... */};
        BookProcessor bookProcessorPipeline = new BookProcessorPipeline(Arrays.asList(bookProcessors));
        EpubWriter writer = new EpubWriter(bookProcessorPipeline);
        writer.write(book, out);
    }

    private String getBaseName(final String name) {
        int slashIndex = name.indexOf(File.separator);
        if (slashIndex == -1) {
            return name;
        } else {
            return name.substring(name.lastIndexOf(File.separator));
        }
    }
}