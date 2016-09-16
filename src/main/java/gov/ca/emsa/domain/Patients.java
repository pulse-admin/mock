package gov.ca.emsa.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Patients")
public class Patients {

    @XmlElement(name="Event")
    private List<Patient> patients;
    
    public List<Patient> getPatients(){
    	return patients;
    }

}
