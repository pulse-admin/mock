package gov.ca.emsa.xcpd.prpa.cap;

import gov.ca.emsa.xcpd.prpa.cap.qbp.MatchCriterionList;
import gov.ca.emsa.xcpd.prpa.cap.qbp.ParameterList;
import gov.ca.emsa.xcpd.prpa.cap.qbp.QueryId;
import gov.ca.emsa.xcpd.prpa.cap.qbp.ResponseModalityCode;
import gov.ca.emsa.xcpd.prpa.cap.qbp.ResponsePriorityCode;
import gov.ca.emsa.xcpd.prpa.cap.qbp.StatusCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class QueryByParameter {
	
	public QueryId queryId;
	
	public StatusCode statusCode;
	
	public ResponsePriorityCode responsePriorityCode;
	
	public ResponseModalityCode responseModalityCode;
	
	public MatchCriterionList matchCriterionList;
	
	public ParameterList parameterList;

	public QueryId getQueryId() {
		return queryId;
	}

	public void setQueryId(QueryId queryId) {
		this.queryId = queryId;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public ParameterList getParameterList() {
		return parameterList;
	}

	public void setParameterList(ParameterList parameterList) {
		this.parameterList = parameterList;
	}
	
	

}
