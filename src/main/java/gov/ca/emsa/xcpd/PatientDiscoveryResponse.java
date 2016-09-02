package gov.ca.emsa.xcpd;

import gov.ca.emsa.xcpd.prpa.AcceptAckCode;
import gov.ca.emsa.xcpd.prpa.Acknowledgement;
import gov.ca.emsa.xcpd.prpa.ControlActProcess;
import gov.ca.emsa.xcpd.prpa.CreationTime;
import gov.ca.emsa.xcpd.prpa.InteractionId;
import gov.ca.emsa.xcpd.prpa.ProcessingCode;
import gov.ca.emsa.xcpd.prpa.ProcessingModeCode;
import gov.ca.emsa.xcpd.prpa.Receiver;
import gov.ca.emsa.xcpd.prpa.Sender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

// cross gateway patient discovery response message
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientDiscoveryResponse {
	
	@XmlAttribute(name = "xmlns:xsi") public String xmlnsxsi = "http://www.w3.org/2001/XMLSchema-instance";
	@XmlAttribute(name = "xmlns") public String xmlns = "urn:hl7-org:v3";
	@XmlAttribute(name = "xsi:schemaLocation") public String xsiSchema = "PRPA_IN201306UV02.xsd";
	@XmlAttribute(name = "ITSVersion") public String itsVersion = "XML_1.0";
	
	public Id id;
	
	public CreationTime creationTime;
	
	public InteractionId interactionId;
	
	public ProcessingCode processingCode;
	
	public ProcessingModeCode processingModeCode;

	public AcceptAckCode acceptAckCode;
	
	public Receiver receiver;
	
	public Sender sender;
	
	public Acknowledgement acknowledgement;
	
	public ControlActProcess controlActProcess;

}
