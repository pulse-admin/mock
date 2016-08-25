package gov.ca.emsa.xcpd.aqr;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Classification {
	@XmlElement(name = "Slot") public Slot slot;
	@XmlElement(name = "Name") public Name name;
	@XmlElement(name = "Description") public Description description;
	
	@XmlAttribute public String classificationScheme;
	@XmlAttribute public String classifiedObject;
	@XmlAttribute public String id;
	@XmlAttribute public String nodeRepresentation;
	@XmlAttribute public String objectType;
}
