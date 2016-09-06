package gov.ca.emsa.service.impl;

import gov.ca.emsa.service.EHealthQueryConsumerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import gov.ca.emsa.service.controller.PatientDiscoveryController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.hl7.v3.*;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Service
public class EHealthQueryConsumerServiceImpl implements EHealthQueryConsumerService{
	
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);
	
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml){
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		 try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);
		
		Source requestSource = saajSoap.getSoapBody().getPayloadSource();
		
		// Create a JAXB context
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance(PRPAIN201305UV02.class);
        }
        catch (Exception ex) {
        	logger.error(ex);
        }

        // Create JAXB unmarshaller
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jc.createUnmarshaller();
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        JAXBElement<?> requestObj = null;
        try {
        	requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, PRPAIN201305UV02.class);
		} catch (JAXBException e) {
			logger.error(e);
		}
        
        return (PRPAIN201305UV02) requestObj.getValue();
	}
	
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml){
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		 try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);
		
		Source requestSource = saajSoap.getSoapBody().getPayloadSource();
		
		// Create a JAXB context
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance(AdhocQueryRequest.class);
        }
        catch (Exception ex) {
        	logger.error(ex);
        }

        // Create JAXB unmarshaller
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jc.createUnmarshaller();
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        JAXBElement<?> requestObj = null;
        try {
        	requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, AdhocQueryRequest.class);
		} catch (JAXBException e) {
			logger.error(e);
		}
        
        return (AdhocQueryRequest) requestObj.getValue();
	}
	
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml){
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		 try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);
		
		Source requestSource = saajSoap.getSoapBody().getPayloadSource();
		
		// Create a JAXB context
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance(RetrieveDocumentSetRequestType.class);
        }
        catch (Exception ex) {
        	logger.error(ex);
        }

        // Create JAXB unmarshaller
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = jc.createUnmarshaller();
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        JAXBElement<?> requestObj = null;
        try {
        	requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, RetrieveDocumentSetRequestType.class);
		} catch (JAXBException e) {
			logger.error(e);
		}
        
        return (RetrieveDocumentSetRequestType) requestObj.getValue();
	}

}
