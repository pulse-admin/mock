package gov.ca.emsa.xcpd.soap;

import gov.ca.emsa.xcpd.soap.header.QueryRequestSoapHeader;
import gov.ca.emsa.xcpd.soap.header.QueryResponseSoapHeader;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope", namespace = "http://www.w3.org/2003/05/soap-envelope")
public class QueryResponseSoapEnvelope {
	
	@XmlAttribute(name = "xmlns:s") public String xmlnss = "http://www.w3.org/2003/05/soap-envelope";
	@XmlAttribute(name = "xmlns:a") public String xmlnsa = "http://www.w3.org/2005/08/addressing";
	
	@XmlElement(name = "Header", namespace = "http://www.w3.org/2003/05/soap-envelope") public QueryResponseSoapHeader header;
	
	@XmlElement(name = "Body", namespace = "http://www.w3.org/2003/05/soap-envelope") public QueryResponseSoapBody body;
}
