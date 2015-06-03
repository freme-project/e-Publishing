package eu.freme.eservices.epublishing.webservice;

import java.util.List;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class Section {
    
    private List<Section> subsections;
    private String title, resource;
    
    public Section() {}

    public Section(String title, String resource) {
        this.title = title;
        this.resource = resource;
    }
    
    public void addSection(Section section) {
        subsections.add(section);
    }

    public List<Section> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<Section> subsections) {
        this.subsections = subsections;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    } 
    
    public static boolean hasSectionWithResource(List<Section> sections, String resource) {
        if (sections == null) {
            return false;
        }
        
        int i = 0;
        boolean sectionFound = false;
        
        while (i < sections.size() && !sectionFound) {
            Section section = sections.get(i);
            
            sectionFound = section.getResource().equals(resource);
            
            if (sections.get(i).getSubsections() != null) {
                if (hasSectionWithResource(sections, resource)) {
                    sectionFound = true;
                }
            }
            
            i ++;
        }
        
        return sectionFound;
    }
}