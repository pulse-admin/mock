package gov.ca.emsa.saml.impl;

import org.opensaml.common.SAMLObjectBuilder;

import org.opensaml.xml.XMLObjectBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.ca.emsa.saml.SAMLInput;
import gov.ca.emsa.saml.SamlGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.util.XMLHelper;

@Service
public class SamlGeneratorImpl implements SamlGenerator {

	private XMLObjectBuilderFactory builderFactory;

	public XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException{

		if(builderFactory == null){
			DefaultBootstrap.bootstrap();
			builderFactory = Configuration.getBuilderFactory();
		}

		return builderFactory;
	}
	
	public String createSAML(SAMLInput input) throws MarshallingException{

		Assertion assertion = buildDefaultAssertion(input);
		AssertionMarshaller marshaller = new AssertionMarshaller();
		org.w3c.dom.Element plaintextElement = marshaller.marshall(assertion);
		String originalAssertionString = XMLHelper.nodeToString(plaintextElement);

		return originalAssertionString;
	}

	/**
	 * Builds a SAML Attribute of type String
	 * @param name
	 * @param value
	 * @param builderFactory
	 * @return
	 * @throws ConfigurationException
	 */
	public Attribute buildStringAttribute(String name, String value, XMLObjectBuilderFactory builderFactory) throws ConfigurationException
	{
		SAMLObjectBuilder attrBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
		Attribute attrFirstName = (Attribute) attrBuilder.buildObject();
		attrFirstName.setName(name);

		// Set custom Attributes
		XMLObjectBuilder stringBuilder = getSAMLBuilder().getBuilder(XSString.TYPE_NAME);
		XSString attrValueFirstName = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		attrValueFirstName.setValue(value);

		attrFirstName.getAttributeValues().add(attrValueFirstName);
		return attrFirstName;
	}

	/**
	 * Helper method which includes some basic SAML fields which are part of almost every SAML Assertion.
	 *
	 * @param input
	 * @return
	 */
	public Assertion buildDefaultAssertion(SAMLInput input)
	{
		try
		{
			// Create the NameIdentifier
			SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(NameID.DEFAULT_ELEMENT_NAME);
			NameID nameId = (NameID) nameIdBuilder.buildObject();
			nameId.setValue(input.getStrNameID());
			nameId.setNameQualifier(input.getStrNameQualifier());
			nameId.setFormat(NameID.UNSPECIFIED);

			// Create the SubjectConfirmation

			SAMLObjectBuilder confirmationMethodBuilder = (SAMLObjectBuilder)  getSAMLBuilder().getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
			SubjectConfirmationData confirmationMethod = (SubjectConfirmationData) confirmationMethodBuilder.buildObject();
			DateTime now = new DateTime();
			confirmationMethod.setNotBefore(now);
			confirmationMethod.setNotOnOrAfter(now.plusMinutes(2));

			SAMLObjectBuilder subjectConfirmationBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
			SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder.buildObject();
			subjectConfirmation.setSubjectConfirmationData(confirmationMethod);

			// Create the Subject
			SAMLObjectBuilder subjectBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Subject.DEFAULT_ELEMENT_NAME);
			Subject subject = (Subject) subjectBuilder.buildObject();

			subject.setNameID(nameId);
			subject.getSubjectConfirmations().add(subjectConfirmation);

			// Create Authentication Statement
			SAMLObjectBuilder authStatementBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
			AuthnStatement authnStatement = (AuthnStatement) authStatementBuilder.buildObject();
			//authnStatement.setSubject(subject);
			//authnStatement.setAuthenticationMethod(input.getStrAuthMethod());
			DateTime now2 = new DateTime();
			authnStatement.setAuthnInstant(now2);
			authnStatement.setSessionIndex(input.getSessionId());
			authnStatement.setSessionNotOnOrAfter(now2.plus(input.getMaxSessionTimeoutInMinutes()));

			SAMLObjectBuilder authContextBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
			AuthnContext authnContext = (AuthnContext) authContextBuilder.buildObject();

			SAMLObjectBuilder authContextClassRefBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
			AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authContextClassRefBuilder.buildObject();
			authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:Password"); // TODO not sure exactly about this

			authnContext.setAuthnContextClassRef(authnContextClassRef);
			authnStatement.setAuthnContext(authnContext);

			// Builder Attributes
			SAMLObjectBuilder attrStatementBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
			AttributeStatement attrStatement = (AttributeStatement) attrStatementBuilder.buildObject();

			Map attributes = input.getAttributes();
			if(attributes != null){
				Iterator keySet = attributes.keySet().iterator();
				while (keySet.hasNext())
				{
					String key = keySet.next().toString();
					String val = "";
					if(attributes.get(key) != null) {
						val = attributes.get(key).toString();
					}
					Attribute attrFirstName = buildStringAttribute(key, val, getSAMLBuilder());
					attrStatement.getAttributes().add(attrFirstName);
				}
			}

			// Create the do-not-cache condition
			SAMLObjectBuilder doNotCacheConditionBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(OneTimeUse.DEFAULT_ELEMENT_NAME);
			Condition condition = (Condition) doNotCacheConditionBuilder.buildObject();

			SAMLObjectBuilder conditionsBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
			Conditions conditions = (Conditions) conditionsBuilder.buildObject();
			conditions.getConditions().add(condition);

			// Create Issuer
			SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
			Issuer issuer = (Issuer) issuerBuilder.buildObject();
			issuer.setValue(input.getStrIssuer());

			// Create the assertion
			SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
			Assertion assertion = (Assertion) assertionBuilder.buildObject();
			assertion.setID(input.getAssertionId());
			assertion.setIssuer(issuer);
			assertion.setIssueInstant(now);
			assertion.setVersion(SAMLVersion.VERSION_20);

			assertion.getAuthnStatements().add(authnStatement);
			assertion.getAttributeStatements().add(attrStatement);
			assertion.setConditions(conditions);

			return assertion;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
