package gov.ca.emsa.xcpd.prpa.cap.subj;

import gov.ca.emsa.xcpd.Addr;
import gov.ca.emsa.xcpd.Name;
import gov.ca.emsa.xcpd.Telecom;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.AdminGenderCode;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.BirthTime;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class PatientPerson {
	private Name name;
	@XmlElement(name = "telecom") public ArrayList<Telecom> telecoms;
	public AdminGenderCode administrativeGenderCode;
	public BirthTime birthTime;
	public Addr addr;
	@XmlElement(name = "asOtherIDs") public ArrayList<AsOtherIDs> asOtherIDs;

	public PatientPerson() {
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

}