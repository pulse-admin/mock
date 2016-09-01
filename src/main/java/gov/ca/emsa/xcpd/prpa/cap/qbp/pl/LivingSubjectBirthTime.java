package gov.ca.emsa.xcpd.prpa.cap.qbp.pl;

import javax.xml.bind.annotation.XmlAttribute;

public class LivingSubjectBirthTime {
	public LivingSubjBirthTimeValue value;
	public String semanticsText;

	public LivingSubjectBirthTime() {
	}

	public void setValue(LivingSubjBirthTimeValue lsbtv) {
		this.value = lsbtv;
	}
}