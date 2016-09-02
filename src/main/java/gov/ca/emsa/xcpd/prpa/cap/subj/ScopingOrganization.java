package gov.ca.emsa.xcpd.prpa.cap.subj;

import javax.xml.bind.annotation.XmlAttribute;

import gov.ca.emsa.xcpd.Id;

public class ScopingOrganization {
	@XmlAttribute
	public String classCode;
	@XmlAttribute
	public String determinerCode;
	public Id id;

	public ScopingOrganization() {
	}
}