package gov.ca.emsa.xcpd.prpa.cap.subj;

import javax.xml.bind.annotation.XmlAttribute;

public class SubjectOne {
	@XmlAttribute public String typeCode;
	private Patient patient;

	public SubjectOne() {
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
}