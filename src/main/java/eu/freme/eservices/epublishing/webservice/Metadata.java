package eu.freme.eservices.epublishing.webservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class Metadata {
    
    private List<String> titles, subjects, sources, relations, types, descriptions, rights;
    private String language, coverImage, epubVersion;
    private Calendar publicationDate;
    private List<Section> tableOfContents;
    private Identifier identifier;
    private List<Person> creators, contributors;
    
    public Metadata() {
        
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
    public List<Person> getCreators() {
        return creators;
    }

    public void setCreators(List<Person> creators) {
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

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getRights() {
        return rights;
    }

    public void setRights(List<String> rights) {
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

    public String getEPUBVersion() {
        return epubVersion;
    }

    public void setEPUBVersion(String epubVersion) {
        this.epubVersion = epubVersion;
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public void setContributors(List<Person> contributors) {
        this.contributors = contributors;
    }
    
    public List<Person> getAuthors() {
        ArrayList<Person> authors = new ArrayList<>();
        authors.addAll(getCreators("author"));
        authors.addAll(getContributors("author"));
        
        return authors;
    }
    
    public List<Person> getCreators(String role) {
        ArrayList<Person> people = new ArrayList<>();
        
        for (Person c : creators) {
            if (c.hasRole(role)) {
                people.add(c);
            }
        }
        
        return people;
    }
    
    public List<Person> getContributors(String role) {
        ArrayList<Person> people = new ArrayList<>();
        
        for (Person c : contributors) {
            if (c.hasRole(role)) {
                people.add(c);
            }
        }
        
        return people;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }
}