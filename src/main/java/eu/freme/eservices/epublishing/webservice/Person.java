package eu.freme.eservices.epublishing.webservice;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pieter Heyvaert <pheyvaer.heyvaert@ugent.be>
 */
public class Person {

    private String firstName, lastName;
    private List<String> roles;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        if (roles == null) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add("author");

            return temp;
        } else {
            return roles;
        }
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
