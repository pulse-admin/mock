package gov.ca.emsa.xcpd.soap.header.security;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.impl.AssertionImpl;

public class Security {
	
	@XmlElement(name = "Timestamp", namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd") public Timestamp timestamp;
	@XmlElement(name = "KeyInfo", namespace = "http://www.w3.org/2000/09/xmldsig#") public KeyInfo keyInfo;
	@XmlElement public AssertionImpl assertion;
}
