package gov.ca.emsa.xcpd.prpa.cap.subj;

import gov.ca.emsa.xcpd.Id;
import gov.ca.emsa.xcpd.prpa.cap.qbp.StatusCode;

import javax.xml.bind.annotation.XmlAttribute;

public class RegistrationEvent {
	@XmlAttribute
	public String classCode;
	@XmlAttribute
	public String moodCode;
	private Id id;
	private StatusCode statusCode;
	private SubjectOne subject1;
	private Custodian custodian;

	public RegistrationEvent() {
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public SubjectOne getSubject1() {
		return subject1;
	}

	public void setSubject1(SubjectOne subject1) {
		this.subject1 = subject1;
	}

	public Custodian getCustodian() {
		return custodian;
	}

	public void setCustodian(Custodian custodian) {
		this.custodian = custodian;
	}
}