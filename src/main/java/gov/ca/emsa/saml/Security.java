package gov.ca.emsa.saml;

import javax.xml.bind.annotation.XmlElement;

import org.opensaml.saml2.core.Assertion;

public class Security {
	
	@XmlElement(name = "Assertion") public Assertion assertion;

}
