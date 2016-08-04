package gov.ca.emsa.domain;


import gov.ca.emsa.pulse.common.domain.Organization;

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
