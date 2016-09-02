package gov.ca.emsa.xcpd.soap.header.security;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SecurityTokenReference {
	@XmlAttribute(name = "Id", namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd") public String id;
	@XmlAttribute(name = "TokenType", namespace = "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd") public String tokenType;
	@XmlElement(name = "KeyIdentifier", namespace = "https://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd#") public KeyIdentifier keyIdentifier;
}
