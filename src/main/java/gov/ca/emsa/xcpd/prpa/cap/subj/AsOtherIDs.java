package gov.ca.emsa.xcpd.prpa.cap.subj;

import gov.ca.emsa.xcpd.Id;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class AsOtherIDs {
	@XmlAttribute public String classCode;
	
	public Id id;
	
	public ScopingOrganization scopingOrganization;
}
