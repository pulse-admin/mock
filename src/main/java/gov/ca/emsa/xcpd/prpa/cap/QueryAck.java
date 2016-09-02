package gov.ca.emsa.xcpd.prpa.cap;

import gov.ca.emsa.xcpd.prpa.cap.qbp.QueryId;

public class QueryAck {
	private QueryId queryId;
	private QueryReponseCode queryResponseCode;

	public QueryAck() {
	}

	public QueryId getQueryId() {
		return queryId;
	}

	public void setQueryId(QueryId queryId) {
		this.queryId = queryId;
	}

	public QueryReponseCode getQueryResponseCode() {
		return queryResponseCode;
	}

	public void setQueryResponseCode(QueryReponseCode queryResponseCode) {
		this.queryResponseCode = queryResponseCode;
	}
}