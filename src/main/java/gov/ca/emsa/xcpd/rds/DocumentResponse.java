package gov.ca.emsa.xcpd.rds;

import javax.xml.bind.annotation.XmlElement;

public class DocumentResponse {
	@XmlElement(name = "RepositoryUniqueId") public String repositoryUniqueId;
	@XmlElement(name = "DocumentUniqueId") public String documentUniqueId;
	@XmlElement public String mimeType;
	@XmlElement(name = "Document") public String document;
}
