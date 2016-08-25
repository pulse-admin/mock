package gov.ca.emsa.xcpd.prpa.cap.subj;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import gov.ca.emsa.xcpd.Id;
import gov.ca.emsa.xcpd.prpa.cap.qbp.StatusCode;

public class Patient {
	@XmlAttribute
	public String classCode;
	private Id id;
	private StatusCode statusCode;
	@XmlElement(name = "patientPerson") private PatientPerson patientPerson;
	private ProviderOrganization providerOrganization;
	private SubjectOfOne subjectOf1;

	public Patient() {
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

	public PatientPerson getPatientPerson() {
		return patientPerson;
	}

	public void setPp(PatientPerson patientPerson) {
		this.patientPerson = patientPerson;
	}

	public ProviderOrganization getProviderOrganization() {
		return providerOrganization;
	}

	public void setProviderOrganization(ProviderOrganization providerOrganization) {
		this.providerOrganization = providerOrganization;
	}

	public SubjectOfOne getSubjectOf1() {
		return subjectOf1;
	}

	public void setSubjectOf1(SubjectOfOne subjectOf1) {
		this.subjectOf1 = subjectOf1;
	}
}