package gov.ca.emsa.xcpd.prpa;

import gov.ca.emsa.xcpd.prpa.cap.Code;
import gov.ca.emsa.xcpd.prpa.cap.QueryAck;
import gov.ca.emsa.xcpd.prpa.cap.QueryByParameter;
import gov.ca.emsa.xcpd.prpa.cap.Subject;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ControlActProcess {
	@XmlAttribute public String classCode;
	@XmlAttribute public String moodCode;
	private Code code;
	@XmlElement(name = "subject") public ArrayList<Subject> subjects;
	private QueryAck queryAck;
	private QueryByParameter queryByParameter;

	public ControlActProcess() {
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public QueryAck getQueryAck() {
		return queryAck;
	}

	public void setQueryAck(QueryAck queryAck) {
		this.queryAck = queryAck;
	}

	public QueryByParameter getQueryByParameter() {
		return queryByParameter;
	}

	public void setQueryByParameter(QueryByParameter queryByParameter) {
		this.queryByParameter = queryByParameter;
	}
}