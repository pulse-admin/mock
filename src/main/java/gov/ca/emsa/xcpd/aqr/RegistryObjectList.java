package gov.ca.emsa.xcpd.aqr;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class RegistryObjectList {
	@XmlElement(name = "ExtrinsicObject") public ExtrinsicObject extrinsicObject;
	
	@XmlElement(name = "ObjectRef") public ArrayList<ObjectRef> objectRef;
}
