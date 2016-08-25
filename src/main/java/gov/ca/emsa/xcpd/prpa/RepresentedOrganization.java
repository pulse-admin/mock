package gov.ca.emsa.xcpd.prpa;

import javax.xml.bind.annotation.XmlAttribute;

import gov.ca.emsa.xcpd.Id;

public class RepresentedOrganization {
	@XmlAttribute public String classCode;
	@XmlAttribute public String determinerCode;
	
	public Id id;
}
