package gov.ca.emsa.xcpd.prpa.cap.subj;

import gov.ca.emsa.xcpd.Telecom;

import javax.xml.bind.annotation.XmlAttribute;

public class ContactParty {
	@XmlAttribute private String contactParty;
	public Telecom telecom;

	public ContactParty() {
	}

}