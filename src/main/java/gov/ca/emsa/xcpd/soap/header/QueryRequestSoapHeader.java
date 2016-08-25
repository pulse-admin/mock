package gov.ca.emsa.xcpd.soap.header;

import javax.xml.bind.annotation.XmlElement;

public class QueryRequestSoapHeader {
	@XmlElement(name = "Action", namespace = "http://www.w3.org/2005/08/addressing") public Action action;
	
	@XmlElement(name = "MessageID", namespace = "http://www.w3.org/2005/08/addressing") public MessageId messageId;
	
	@XmlElement(name = "ReplyTo", namespace = "http://www.w3.org/2005/08/addressing") public ReplyTo replyTo;
	
	@XmlElement(name = "To", namespace = "http://www.w3.org/2005/08/addressing") public To to;
	
}
