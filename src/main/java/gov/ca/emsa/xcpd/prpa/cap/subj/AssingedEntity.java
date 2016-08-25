package gov.ca.emsa.xcpd.prpa.cap.subj;

import javax.xml.bind.annotation.XmlAttribute;

import gov.ca.emsa.xcpd.Id;
import gov.ca.emsa.xcpd.prpa.cap.Code;

public class AssingedEntity {
	@XmlAttribute private String classCode;
	private Id id;
	private Code code;

	public AssingedEntity() {
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}
}