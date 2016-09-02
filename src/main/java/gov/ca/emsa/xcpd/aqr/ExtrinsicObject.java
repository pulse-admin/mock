package gov.ca.emsa.xcpd.aqr;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ExtrinsicObject {
	@XmlAttribute public String id;
	@XmlAttribute public String isOpaque;
	@XmlAttribute public String mimeType;
	@XmlAttribute public String objectType;
	@XmlAttribute public String status;
	@XmlAttribute(name = "xmlns:q") public String xmlnsq;
	
	@XmlElement(name = "Slot") public ArrayList<Slot> slots;
	
	@XmlElement(name = "Name") public Name name;
	
	@XmlElement(name = "Description") public Description description;
	
	@XmlElement(name = "Classification") public ArrayList<Classification> classification;
	
	@XmlElement(name = "ExternalIdentifier") public ArrayList<ExternalIdentifier> externalIdentifier;
}
