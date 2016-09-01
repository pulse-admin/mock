package gov.ca.emsa.xcpd.prpa.cap.subj;

import javax.xml.bind.annotation.XmlAttribute;

public class SubjectOfOneValue {
	@XmlAttribute(name = "xsi:type") private String xsiType;
	@XmlAttribute private String value;

	public SubjectOfOneValue() {
	}
	
}