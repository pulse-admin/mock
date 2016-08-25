package gov.ca.emsa.xcpd.prpa;

import gov.ca.emsa.xcpd.Device;

import javax.xml.bind.annotation.XmlAttribute;

public class Receiver {
	@XmlAttribute public String typeCode;
	public Device device;

	public Receiver() {
	}
	
}