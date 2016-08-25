package gov.ca.emsa.xcpd.soap;

import gov.ca.emsa.xcpd.soap.header.DiscoveryResponseSoapHeader;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Envelope", namespace = "http://www.w3.org/2003/05/soap-envelope")
public class DiscoveryResponseSoapEnvelope {
	
	@XmlAttribute(name = "xmlns:xsi") public String xmlnsxsi = "http://www.w3.org/2001/XMLSchema-instance";
	@XmlAttribute(name = "xmlns:xcpd") public String xmlnsxcpd = "urn:ihe:iti:xcpd:2009";
	@XmlAttribute(name = "xmlns:s") public String xsixmlnss = "http://www.w3.org/2003/05/soap-envelope";
	@XmlAttribute(name = "xmlns:a") public String itsVersion = "http://www.w3.org/2005/08/addressing";
	@XmlAttribute(name = "xmlns:schemaLocation") public String schemaLocation = "urn:ihe:iti:xcpd:2009";
	
	@XmlElement(name = "Header", namespace = "http://www.w3.org/2003/05/soap-envelope") public DiscoveryResponseSoapHeader sHeader;
	
	@XmlElement(name = "Body", namespace = "http://www.w3.org/2003/05/soap-envelope") public DiscoveryResponseSoapBody sBody;

}
