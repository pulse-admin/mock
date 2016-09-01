package gov.ca.emsa.xcpd.soap.header;

import javax.xml.bind.annotation.XmlElement;


public class DiscoveryResponseSoapHeader {
	
	@XmlElement(name = "Action", namespace = "http://www.w3.org/2005/08/addressing") public Action action;
	
	@XmlElement(name = "RelatesTo", namespace = "http://www.w3.org/2005/08/addressing") public RelatesTo relatesTo;
	
	@XmlElement(name = "CorrelationTimeToLive", namespace = "urn:ihe:iti:xcpd:2009") public CorrelationTimeToLive cttl;

}
