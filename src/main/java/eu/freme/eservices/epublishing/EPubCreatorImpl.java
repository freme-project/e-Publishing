package eu.freme.eservices.epublishing;

import eu.freme.eservices.epublishing.exception.MissingMetadataException;
import eu.freme.eservices.epublishing.webservice.Person;
import eu.freme.eservices.epublishing.webservice.Section;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.BookProcessor;
import nl.siegmann.epublib.epub.BookProcessorPipeline;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import nl.siegmann.epublib.bookprocessor.Epub2HtmlCleanerBookProcessor;
import nl.siegmann.epublib.bookprocessor.Epub3HtmlCleanerBookProcessor;
import nl.siegmann.epublib.epub.Epub2Writer;
import nl.siegmann.epublib.epub.Epub3Writer;
import nl.siegmann.epublib.epub.EpubWriter;

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
    private final EpubWriter epubWriter;

    public EPubCreatorImpl(final eu.freme.eservices.epublishing.webservice.Metadata ourMetadata, final String unzippedPath) throws IOException, MissingMetadataException {
        book = new Book();
        this.metadata = new Metadata();
        this.unzippedPath = unzippedPath;
        this.ourMetadata = ourMetadata;

        addCreators(ourMetadata.getCreators());
        addContributors(ourMetadata.getContributors());

        if (ourMetadata.getLanguage() != null) {
            this.metadata.setLanguage(ourMetadata.getLanguage());
        }

        if (ourMetadata.getIdentifier() != null) {
            String scheme = ourMetadata.getIdentifier().getScheme();
            
            if (scheme == null) {
                scheme = "";
            }
            
            this.metadata.addIdentifier(new Identifier(scheme, ourMetadata.getIdentifier().getValue()));
        } else {
            throw new MissingMetadataException("Identifier is missing.");
        }

        if (ourMetadata.getTitles() != null && !ourMetadata.getTitles().isEmpty()) {
            this.metadata.setTitles(ourMetadata.getTitles());
        } else {
            throw new MissingMetadataException("At least one title is required.");
        }

        if (ourMetadata.getSubjects() != null) {
            this.metadata.setSubjects(ourMetadata.getSubjects());
        }

        if (ourMetadata.getPublicationDate() != null) {
            this.metadata.addDate(new Date(ourMetadata.getPublicationDate().getTime()));
        }
  
        if (ourMetadata.getSources() != null) {
            this.metadata.setSources(ourMetadata.getSources());
        }

        if (ourMetadata.getTypes() != null) {
            this.metadata.setTypes(ourMetadata.getTypes());
        }

        if (ourMetadata.getDescriptions() != null) {
            this.metadata.setDescriptions(ourMetadata.getDescriptions());
        }
        
        if (ourMetadata.getRelations() != null) {
            this.metadata.setRelations(ourMetadata.getRelations());
        }

        if (ourMetadata.getRights() != null) {
            this.metadata.setRights(ourMetadata.getRights());
        }

        if (ourMetadata.getTableOfContents() == null) {
            ourMetadata.setTableOfContents(createBestEffortTableOfContents(null));
        }
        
        createSections(ourMetadata.getTableOfContents(), null);

        if (ourMetadata.getCoverImage() != null) {
            addCoverImage(ourMetadata.getCoverImage());
        }

        copyUnaddedFilesFromZipToEpub(null);
        
        if (ourMetadata.getEPUBVersion() != null && ourMetadata.getEPUBVersion().equals("2")) {
            BookProcessor[] bookProcessors = {new Epub2HtmlCleanerBookProcessor()};
            BookProcessor bookProcessorPipeline = new BookProcessorPipeline(Arrays.asList(bookProcessors));
            epubWriter = new Epub2Writer(bookProcessorPipeline);
        } else {
            BookProcessor[] bookProcessors = {new Epub3HtmlCleanerBookProcessor()};
            BookProcessor bookProcessorPipeline = new BookProcessorPipeline(Arrays.asList(bookProcessors));
            epubWriter = new Epub3Writer(bookProcessorPipeline);
        }
    }

    private void addCoverImage(String coverImage) throws IOException {
        book.setCoverImage(new Resource(new FileInputStream(unzippedPath + File.separator + coverImage), coverImage));
    }

    private void addCreators(List<Person> creators) {
        if (creators != null) {
            for (Person creator : creators) {
                metadata.addCreator(new CreatorContributor(creator.getFirstName(), creator.getLastName(), creator.getRoles()));
            }
        }
    }
    
     private void addContributors(List<Person> contributors) {
        if (contributors != null) {
            for (Person contributor : contributors) {
                metadata.addContributor(new CreatorContributor(contributor.getFirstName(), contributor.getLastName(), contributor.getRoles()));
            }
        }
    }

    private void createSections(List<Section> toc, TOCReference parentSection) throws IOException {
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

    private List<Section> createBestEffortTableOfContents(String parent) throws IOException {
        List<Section> sections = new ArrayList<>();
        File folder;

        if (parent == null || parent.equals("")) {
            folder = new File(unzippedPath);
        } else {
            folder = new File(unzippedPath + File.pathSeparator + parent);
        }

        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            Arrays.sort(listOfFiles, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });


            //Arrays.sort(listOfFiles, (File o1, File o2) -> o1.getName().compareTo(o2.getName()));

            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile() && (listOfFile.getName().endsWith(".html") || listOfFile.getName().endsWith(".xhtml"))) {
                    Section s;
                    if (parent == null || parent.equals("")) {
                        s = new Section(listOfFile.getName().substring(0, listOfFile.getName().lastIndexOf(".")), listOfFile.getName());
                    } else {
                        s = new Section(listOfFile.getName().substring(0, listOfFile.getName().lastIndexOf(".")), parent + File.separator + listOfFile.getName());
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
        }
        
        return sections;
    }

    private void copyUnaddedFilesFromZipToEpub(String parent) throws IOException {
        File folder;

        if (parent == null || parent.equals("")) {
            folder = new File(unzippedPath);
        } else {
            folder = new File(unzippedPath + File.separator + parent);
        }

        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
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
        epubWriter.write(book, out);
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