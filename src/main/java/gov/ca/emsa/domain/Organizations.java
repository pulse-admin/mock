package gov.ca.emsa.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Organizations")
public class Organizations {

    @XmlElement(name="Event")
    private List<Organization> organizations;
    
    public List<Organization> getOrganizations(){
    	return organizations;
    }

}
