package gov.ca.emsa.xcpd.prpa;


public class Acknowledgement {
	private TargetMessage tm;
	private TypeCode typeCode;

	public Acknowledgement() {
	}

	public TypeCode getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(TypeCode typeCode) {
		this.typeCode = typeCode;
	}

	public TargetMessage getTargetMessage() {
		return tm;
	}

	public void setTargetMessage(TargetMessage tm) {
		this.tm = tm;
	}
}