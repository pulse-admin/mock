package gov.ca.emsa;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Name;

import java.net.URL;
import java.util.ArrayList;

import javax.activation.DataHandler;

public class SOAPGenerator{
	public static SOAPMessage soapMessage;

	//Method to create and send the SOAP message
	public SOAPGenerator() throws SOAPException{
		MessageFactory mf = MessageFactory.newInstance();
		soapMessage = mf.createMessage();
	}
	
	public SOAPMessage getMessage(){
		return soapMessage;
	}
	
	public void addHeader() throws SOAPException{
		SOAPHeader soapHeader = soapMessage.getSOAPPart().getEnvelope().getHeader();
		soapHeader.addNamespaceDeclaration("s", "https://www.w3.org/2003/05/soap-envelope");
		QName headerAction = new QName("Action","s");
		SOAPHeaderElement headerElement1 = soapHeader.addHeaderElement(headerAction);
		headerElement1.setAttribute("mustUnderstand", "1");
		soapHeader.addChildElement(headerElement1).setTextContent("Request patient information");
		QName headerMessageId = new QName("MessageId","s");
		SOAPHeaderElement headerElement2 = soapHeader.addHeaderElement(headerMessageId);
		soapHeader.addChildElement(headerElement2).setTextContent("123456789");
		QName headerTo = new QName("To","s");
		SOAPHeaderElement headerElement3 = soapHeader.addHeaderElement(headerTo);
		soapHeader.addChildElement(headerElement3).setTextContent("http://pusle.gov/search");
		QName headerFrom = new QName("From","s");
		SOAPHeaderElement headerElement4 = soapHeader.addHeaderElement(headerFrom);
		soapHeader.addChildElement(headerElement4).setTextContent("http://someHIE.org/requestPatientInfo");
		QName headerSecurity = new QName("Security","s");
		SOAPHeaderElement headerElement5 = soapHeader.addHeaderElement(headerSecurity);
		soapHeader.addChildElement(headerElement5);
	}
	
	public void addQueryForPatient(String firstName, String lastName) throws SOAPException{
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		
		QName bodyQueryParams = new QName("queryByParameter");
		SOAPBodyElement queryBodyElement = soapBody.addBodyElement(bodyQueryParams);
		soapBody.addChildElement(queryBodyElement);
		
		QName qNameQueryId = new QName("queryId");
		SOAPBodyElement queryId = soapBody.addBodyElement(qNameQueryId);
		queryBodyElement.addChildElement(queryId).setTextContent("1");
		
		QName qNameStatusCode = new QName("statusCode");
		SOAPBodyElement statusCode = soapBody.addBodyElement(qNameStatusCode);
		queryBodyElement.addChildElement(statusCode);
		
		QName qNameResponsePriority = new QName("responsePriority");
		SOAPBodyElement responsePriority = soapBody.addBodyElement(qNameResponsePriority);
		queryBodyElement.addChildElement(responsePriority);
		
		QName qNameParamsList = new QName("parameterList");
		SOAPBodyElement paramsList = soapBody.addBodyElement(qNameParamsList);
		queryBodyElement.addChildElement(paramsList);
		
		QName qNameLivingSubjectBirthTime = new QName("livingSubjectBirthTime");
		SOAPBodyElement livingSubjectBirthTime = soapBody.addBodyElement(qNameLivingSubjectBirthTime);
		paramsList.addChildElement(livingSubjectBirthTime);
		
		QName qNameLivingSubjectName = new QName("livingSubjectName");
		SOAPBodyElement livingSubjectName = soapBody.addBodyElement(qNameLivingSubjectName);
		paramsList.addChildElement(livingSubjectName);
		
		QName qNameValue = new QName("value");
		SOAPBodyElement value = soapBody.addBodyElement(qNameValue);
		livingSubjectName.addChildElement(value);
		
		QName qNameGiven = new QName("given");
		SOAPBodyElement given = soapBody.addBodyElement(qNameGiven);
		value.addChildElement(given).setTextContent(firstName);
		
		QName qNameFamily = new QName("family");
		SOAPBodyElement family = soapBody.addBodyElement(qNameFamily);
		value.addChildElement(family).setTextContent(lastName);
	}
	
	public void addQueryOfPatientList(ArrayList<String> patients) throws SOAPException{
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		
		QName bodyQuery = new QName("query", "AdHocQueryRequest");
		SOAPBodyElement queryBodyElement = soapBody.addBodyElement(bodyQuery);
		queryBodyElement.setAttribute("xmlns:query", "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0");
		queryBodyElement.setAttribute("xmlns:rim", "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0");
		soapBody.addChildElement(queryBodyElement);
		
		QName qNameQueryId = new QName("query", "ResponseOption");
		SOAPBodyElement responseOption = soapBody.addBodyElement(qNameQueryId);
		queryBodyElement.addChildElement(responseOption).setTextContent("1");
		
		// the type of query 
		QName qNameStatusCode = new QName("rim", "AdhocQuery");
		SOAPBodyElement adHocQuery = soapBody.addBodyElement(qNameStatusCode);
		adHocQuery.setAttribute("id", "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
		// must include homeCommunityID in every request
		adHocQuery.setAttribute("home", "urn:uuid:123456789");
		queryBodyElement.addChildElement(adHocQuery);
		
		for(String patient : patients){
			QName qNameSlot= new QName("rim", "slot");
			SOAPBodyElement slot = soapBody.addBodyElement(qNameSlot);
			slot.setAttribute("name", "$XDSDocumentEntryPatientId");
			queryBodyElement.addChildElement(slot);

			QName qNameValueList = new QName("rim", "valueList");
			SOAPBodyElement valueList = soapBody.addBodyElement(qNameValueList);
			slot.addChildElement(valueList);

			QName qNameValueName = new QName("rim", "value");
			SOAPBodyElement value = soapBody.addBodyElement(qNameValueName);
			valueList.addChildElement(value).setTextContent(patient);
			
			QName qNameResponsePriority = new QName("rim", "slot");
			SOAPBodyElement slot2 = soapBody.addBodyElement(qNameResponsePriority);
			slot2.setAttribute("name", "$XDSDocumentEntryStatus");
			queryBodyElement.addChildElement(slot2);

			QName qNameParamsList = new QName("rim", "valueList");
			SOAPBodyElement valueList2 = soapBody.addBodyElement(qNameParamsList);
			slot2.addChildElement(valueList2);
			
			// the status of the document to return
			QName qNameLivingSubjectName = new QName("rim", "value");
			SOAPBodyElement value2 = soapBody.addBodyElement(qNameLivingSubjectName);
			valueList.addChildElement(value2).setTextContent("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')");
		}
	}
	
	public void addQueryOfDocumentList(ArrayList<String> documents) throws SOAPException{
		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		
		QName bodyQuery = new QName("RetrieveDocumentSetRequest");
		SOAPBodyElement retreiveDocumentSetRequest = soapBody.addBodyElement(bodyQuery);
		soapBody.addChildElement(retreiveDocumentSetRequest);
		
		for(String doc : documents){
			QName qNameQueryId = new QName("DocumentRequest");
			SOAPBodyElement documentRequest = soapBody.addBodyElement(qNameQueryId);
			retreiveDocumentSetRequest.addChildElement(documentRequest);
			
			QName qNameResponsePriority = new QName("homeCommunityId");
			SOAPBodyElement slot = soapBody.addBodyElement(qNameResponsePriority);
			documentRequest.addChildElement(slot);

			QName qNameParamsList = new QName("RepositoryUniqueId");
			SOAPBodyElement valueList = soapBody.addBodyElement(qNameParamsList);
			documentRequest.addChildElement(valueList);

			QName qNameLivingSubjectName = new QName("DocumentUniqueId");
			SOAPBodyElement value = soapBody.addBodyElement(qNameLivingSubjectName);
			documentRequest.addChildElement(value).setTextContent(doc);
		}
	}	
}