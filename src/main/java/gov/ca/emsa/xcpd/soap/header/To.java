package gov.ca.emsa.xcpd.soap.header;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class To {
	@XmlElement(name = "a:To") public String to;
	@XmlAttribute(name = "s:mustUnderstand") public String mustUnderstand;
}
