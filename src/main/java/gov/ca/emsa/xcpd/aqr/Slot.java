package gov.ca.emsa.xcpd.aqr;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Slot {
	@XmlAttribute public String name;
	@XmlElement(name = "ValueList") public ValueList valueList;
}
