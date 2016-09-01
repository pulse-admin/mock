package gov.ca.emsa.xcpd.prpa.cap.subj;

import gov.ca.emsa.xcpd.prpa.cap.Code;

import javax.xml.bind.annotation.XmlAttribute;

public class QueryMatchObservation {
	@XmlAttribute private String classCode;
	@XmlAttribute private String moodCode;
	private Code code;
	private SubjectOfOneValue sofv;

	public QueryMatchObservation() {
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public SubjectOfOneValue getSofv() {
		return sofv;
	}

	public void setSofv(SubjectOfOneValue sofv) {
		this.sofv = sofv;
	}
}