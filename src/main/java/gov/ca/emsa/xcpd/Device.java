package gov.ca.emsa.xcpd;

import gov.ca.emsa.xcpd.prpa.AsAgent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
public class Device{
	@XmlAttribute public String classCode;
	@XmlAttribute public String determinerCode;
	
	public Id id;
	
	public Telecom telecom;
	
	public AsAgent asAgent;

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Telecom getTelecom() {
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}
	
	
}
