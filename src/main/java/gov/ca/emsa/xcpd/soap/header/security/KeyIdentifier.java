package gov.ca.emsa.xcpd.soap.header.security;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class KeyIdentifier {
	@XmlAttribute(name = "ValueType", namespace = "https://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd#") public String valueType;
	@XmlValue public String value;
}
