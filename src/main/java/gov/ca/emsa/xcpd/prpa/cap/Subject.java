package gov.ca.emsa.xcpd.prpa.cap;

import gov.ca.emsa.xcpd.prpa.cap.subj.RegistrationEvent;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class Subject {
	@XmlAttribute public String typeCode;
	
	public RegistrationEvent registrationEvent;

}
