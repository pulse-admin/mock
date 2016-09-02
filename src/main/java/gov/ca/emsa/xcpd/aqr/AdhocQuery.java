package gov.ca.emsa.xcpd.aqr;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class AdhocQuery {
	@XmlAttribute public String id;
	@XmlElement(name = "Slot") public ArrayList<Slot> slots;
}
