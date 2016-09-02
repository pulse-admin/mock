package gov.ca.emsa.xcpd.soap.header.security;

import javax.xml.bind.annotation.XmlElement;

public class KeyInfo {
	@XmlElement(namespace = "https://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd#") public SecurityTokenReference securityTokenReference;
}
