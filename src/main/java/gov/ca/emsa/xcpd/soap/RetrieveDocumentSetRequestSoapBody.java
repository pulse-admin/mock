package gov.ca.emsa.xcpd.soap;

import gov.ca.emsa.xcpd.rds.RetrieveDocumentSetRequest;

import javax.xml.bind.annotation.XmlElement;

public class RetrieveDocumentSetRequestSoapBody {
	@XmlElement(name = "RetrieveDocumentSetRequest") public RetrieveDocumentSetRequest documentSetRequest;
}
