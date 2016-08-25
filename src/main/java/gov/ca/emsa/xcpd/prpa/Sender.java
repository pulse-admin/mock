package gov.ca.emsa.xcpd.prpa;

import gov.ca.emsa.xcpd.Device;

import javax.xml.bind.annotation.XmlAttribute;

public class Sender {
	@XmlAttribute public String typeCode;
	public Device device;

}