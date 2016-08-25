package gov.ca.emsa.xcpd.rds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class RetrieveDocumentSetResponse {
	@XmlElement(name = "RegistryResponse") public RegistryResponse registryReponse;
	@XmlElement(name = "DocumentResponse") public ArrayList<DocumentResponse> documentResponse;
}
