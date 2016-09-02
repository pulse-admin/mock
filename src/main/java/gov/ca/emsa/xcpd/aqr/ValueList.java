package gov.ca.emsa.xcpd.aqr;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class ValueList {
	@XmlElement(name = "Value") public ArrayList<String> value; 
}
