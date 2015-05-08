package eu.freme.eservices.epublishing.webservice;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class Identifier {
    
    private String scheme, value;

    public Identifier() {}
    
    public Identifier(String scheme, String value) {
        this.scheme = scheme;
        this.value = value;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}