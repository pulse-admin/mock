package gov.ca.emsa.xcpd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Id{
	@XmlAttribute public String root;
	@XmlAttribute public String extension;
	@XmlAttribute public String nullFlavor;
}
