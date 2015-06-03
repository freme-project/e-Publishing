package eu.freme.eservices.epublishing.webservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class Metadata {
    
    private List<String> titles, authors, illustrators, creators, subjects;
    private String language, source, type, description, rights, coverImage;
    private Calendar publicationDate;
    private List<Section> tableOfContents;
    private Identifier identifier;
    
    public Metadata() {
        
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getIllustrators() {
        return illustrators;
    }

    public void setIllustrators(List<String> illustrators) {
        this.illustrators = illustrators;
    }

    public List<String> getCreators() {
        return creators;
    }

    public void setCreators(List<String> creators) {
        this.creators = creators;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Calendar publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public List<Section> getTableOfContents() {
        return tableOfContents;
    }

    public void setTableOfContents(List<Section> tableOfContents) {
        this.tableOfContents = tableOfContents;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
    
    public void addTitle(String title) {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        titles.add(title);
    }
    
    public void addAuthor(String author) {
        if (authors == null) {
            authors = new ArrayList<>();
        }
        authors.add(author);
    }
}