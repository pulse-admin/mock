package gov.ca.emsa.xcpd.soap.header;

import javax.xml.bind.annotation.XmlAttribute;

public class Action {
	
	@XmlAttribute(name = "mustUnderstand", namespace = "http://www.w3.org/2003/05/soap-envelope") public String mustUnderstand;
	
	public String action;

}
