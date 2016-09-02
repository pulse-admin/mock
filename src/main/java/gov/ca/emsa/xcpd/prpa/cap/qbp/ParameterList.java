package gov.ca.emsa.xcpd.prpa.cap.qbp;

import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectAdminGender;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectBirthTime;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectId;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectName;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class ParameterList {
	private LivingSubjectAdminGender livingSubjectAdministrativeGender;
	private LivingSubjectBirthTime livingSubjectBirthTime;
	private LivingSubjectName livingSubjectName;
	@XmlElement(name="LivingSubjectId")
	public ArrayList<LivingSubjectId> livingSubjectId;

	public ParameterList() {
	}

	public LivingSubjectAdminGender getLivingSubjectAdministrativeGender() {
		return livingSubjectAdministrativeGender;
	}

	public void setLivingSubjectAdministrativeGender(
			LivingSubjectAdminGender livingSubjectAdministrativeGender) {
		this.livingSubjectAdministrativeGender = livingSubjectAdministrativeGender;
	}

	public LivingSubjectBirthTime getLivingSubjectBirthTime() {
		return livingSubjectBirthTime;
	}

	public void setLivingSubjectBirthTime(
			LivingSubjectBirthTime livingSubjectBirthTime) {
		this.livingSubjectBirthTime = livingSubjectBirthTime;
	}

	public LivingSubjectName getLivingSubjectName() {
		return livingSubjectName;
	}

	public void setLivingSubjectName(LivingSubjectName livingSubjectName) {
		this.livingSubjectName = livingSubjectName;
	}

}