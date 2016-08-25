package gov.ca.emsa.xcpd.soap.header;

import javax.xml.bind.annotation.XmlElement;

public class ReplyTo {
	@XmlElement(name = "ReplyTo", namespace = "http://www.w3.org/2005/08/addressing") public String replyTo;
	
	@XmlElement(name = "Address", namespace = "http://www.w3.org/2005/08/addressing") public HeaderAddress address;
}
