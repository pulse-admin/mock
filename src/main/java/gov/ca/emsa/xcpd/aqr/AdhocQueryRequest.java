package gov.ca.emsa.xcpd.aqr;

import javax.xml.bind.annotation.XmlElement;

public class AdhocQueryRequest {
	@XmlElement(name = "ResponseOption") public ReponseOption responseOption;
	@XmlElement(name = "AdhocQuery") public AdhocQuery adhocQuery;

}
