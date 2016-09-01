package gov.ca.emsa.xcpd;

import gov.ca.emsa.xcpd.Device;
import gov.ca.emsa.xcpd.Id;
import gov.ca.emsa.xcpd.PatientDiscoveryResponse;
import gov.ca.emsa.xcpd.aqr.AdhocQueryResponse;
import gov.ca.emsa.xcpd.rds.DocumentResponse;
import gov.ca.emsa.xcpd.rds.RegistryResponse;
import gov.ca.emsa.xcpd.rds.RetrieveDocumentSetResponse;
import gov.ca.emsa.xcpd.soap.DiscoveryResponseSoapBody;
import gov.ca.emsa.xcpd.soap.DiscoveryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryResponseSoapBody;
import gov.ca.emsa.xcpd.soap.QueryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetResponseSoapBody;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.header.DiscoveryResponseSoapHeader;
import gov.ca.emsa.xcpd.soap.header.QueryResponseSoapHeader;
import gov.ca.emsa.xcpd.soap.header.RetrieveDocumentSetResponseSoapHeader;
import gov.ca.emsa.xcpd.aqr.AdhocQueryRequest;
import gov.ca.emsa.xcpd.aqr.Classification;
import gov.ca.emsa.xcpd.aqr.ExtrinsicObject;
import gov.ca.emsa.xcpd.aqr.LocalizedString;
import gov.ca.emsa.xcpd.aqr.RegistryObjectList;
import gov.ca.emsa.xcpd.aqr.Slot;
import gov.ca.emsa.xcpd.aqr.Name;
import gov.ca.emsa.xcpd.aqr.ValueList;
import gov.ca.emsa.xcpd.prpa.AcceptAckCode;
import gov.ca.emsa.xcpd.prpa.Acknowledgement;
import gov.ca.emsa.xcpd.prpa.ControlActProcess;
import gov.ca.emsa.xcpd.prpa.CreationTime;
import gov.ca.emsa.xcpd.prpa.InteractionId;
import gov.ca.emsa.xcpd.prpa.ProcessingCode;
import gov.ca.emsa.xcpd.prpa.ProcessingModeCode;
import gov.ca.emsa.xcpd.prpa.Receiver;
import gov.ca.emsa.xcpd.prpa.Sender;
import gov.ca.emsa.xcpd.prpa.TargetMessage;
import gov.ca.emsa.xcpd.prpa.TypeCode;
import gov.ca.emsa.xcpd.prpa.cap.Code;
import gov.ca.emsa.xcpd.prpa.cap.QueryAck;
import gov.ca.emsa.xcpd.prpa.cap.QueryByParameter;
import gov.ca.emsa.xcpd.prpa.cap.QueryReponseCode;
import gov.ca.emsa.xcpd.prpa.cap.Subject;
import gov.ca.emsa.xcpd.prpa.cap.qbp.ParameterList;
import gov.ca.emsa.xcpd.prpa.cap.qbp.QueryId;
import gov.ca.emsa.xcpd.prpa.cap.qbp.StatusCode;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectNameValue;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjBirthTimeValue;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectAdminGender;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectAdminGenderValue;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectBirthTime;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectId;
import gov.ca.emsa.xcpd.prpa.cap.qbp.pl.LivingSubjectName;
import gov.ca.emsa.xcpd.prpa.cap.subj.Custodian;
import gov.ca.emsa.xcpd.prpa.cap.subj.Patient;
import gov.ca.emsa.xcpd.prpa.cap.subj.PatientPerson;
import gov.ca.emsa.xcpd.prpa.cap.subj.ProviderOrganization;
import gov.ca.emsa.xcpd.prpa.cap.subj.RegistrationEvent;
import gov.ca.emsa.xcpd.prpa.cap.subj.SubjectOfOne;
import gov.ca.emsa.xcpd.prpa.cap.subj.SubjectOne;
import gov.ca.emsa.xcpd.rds.DocumentRequest;
import gov.ca.emsa.xcpd.rds.RetrieveDocumentSetRequest;
import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapBody;
import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapBody;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetRequestSoapBody;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.header.Action;
import gov.ca.emsa.xcpd.soap.header.CorrelationTimeToLive;
import gov.ca.emsa.xcpd.soap.header.DiscoveryRequestSoapHeader;
import gov.ca.emsa.xcpd.soap.header.MessageId;
import gov.ca.emsa.xcpd.soap.header.QueryRequestSoapHeader;
import gov.ca.emsa.xcpd.soap.header.RelatesTo;
import gov.ca.emsa.xcpd.soap.header.RetrieveDocumentSetRequestSoapHeader;
import gov.ca.emsa.xcpd.soap.header.To;
import gov.ca.emsa.xcpd.soap.header.security.Created;
import gov.ca.emsa.xcpd.soap.header.security.Expires;
import gov.ca.emsa.xcpd.soap.header.security.KeyIdentifier;
import gov.ca.emsa.xcpd.soap.header.security.KeyInfo;
import gov.ca.emsa.xcpd.soap.header.security.Security;
import gov.ca.emsa.xcpd.soap.header.security.SecurityTokenReference;
import gov.ca.emsa.xcpd.soap.header.security.Timestamp;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.impl.AssertionImpl;

public class XcpdUtils {
	
	public static DiscoveryRequestSoapEnvelope generateDiscoveryRequest(AssertionImpl samlMessage, String givenInput, String familyInput){
		PatientDiscoveryRequest pdr = new PatientDiscoveryRequest();
		CreationTime ct = new CreationTime();
		ct.value = String.valueOf(System.currentTimeMillis());
		pdr.creationTime = ct;
		InteractionId interId = new InteractionId();
		interId.root = "12345";
		pdr.interactionId = interId;
		ProcessingCode pc = new ProcessingCode();
		pc.code = "P";
		pdr.processingCode = pc;
		ProcessingModeCode pcm = new ProcessingModeCode();
		pcm.code = "T";
		pdr.processingModeCode = pcm;
		AcceptAckCode aac = new AcceptAckCode();
		aac.code = "NE";
		pdr.acceptAckCode = aac;
		
		Id id1 = new Id();
		id1.root = "12345";
		Device device1 = new Device();
		device1.setId(id1);
		Receiver receiver = new Receiver();
		receiver.typeCode = "RCV";
		receiver.device = device1;
		pdr.receiver = receiver;
		
		Id id2 = new Id();
		id2.root = "12345";
		Device device2 = new Device();
		device2.setId(id2);
		Sender sender = new Sender();
		sender.typeCode = "SND";
		sender.device = device2;
		pdr.sender = sender;
		
		// acknowledgement
		Id ackId = new Id();
		ackId.root = "12345";
		ackId.extension = "12345";
		TargetMessage tm = new TargetMessage();
		tm.setId(ackId);
		TypeCode tc = new TypeCode();
		tc.code = "1234";
		
		//queryByParameter
		QueryByParameter qbp = new QueryByParameter();
		
		QueryId qid = new QueryId();
		qid.root = "1234";
		qid.extension = "12345";
		StatusCode sc = new StatusCode();
		sc.code = "12345";
		qbp.setQueryId(qid);
		qbp.setStatusCode(sc);
		
		ParameterList pl = new ParameterList();
		
		Id lsidv = new Id();
		lsidv.root = "12345";
		lsidv.extension = "123";
		LivingSubjectId lsid = new LivingSubjectId();
		lsid.semanticsText = "LivingSubject.id";
		lsid.setLsiv(lsidv);
		ArrayList<LivingSubjectId> array = new ArrayList<LivingSubjectId>();
		array.add(lsid);
		pl.livingSubjectId = array;
		
		String given = givenInput;
		String family = familyInput;
		LivingSubjectNameValue lsnv = new LivingSubjectNameValue();
		lsnv.given = given;
		lsnv.family = family;
		LivingSubjectName lsn = new LivingSubjectName();
		lsn.setValue(lsnv);
		lsn.semanticsText = "LivingSubject.name";
		pl.setLivingSubjectName(lsn);
		
		LivingSubjBirthTimeValue lsbtv = new LivingSubjBirthTimeValue();
		lsbtv.value = "12345";
		LivingSubjectBirthTime lsbt = new LivingSubjectBirthTime();
		lsbt.semanticsText = "LivingSubject.birthTime";
		lsbt.setValue(lsbtv);
		pl.setLivingSubjectBirthTime(lsbt);
		
		LivingSubjectAdminGenderValue lsagv = new LivingSubjectAdminGenderValue();
		lsagv.code = "M";
		LivingSubjectAdminGender lsag = new LivingSubjectAdminGender();
		lsag.value = lsagv;
		lsag.semanticsText = "LivingSubject.administrativeGender";
		pl.setLivingSubjectAdministrativeGender(lsag);
		
		qbp.parameterList = pl;
		
		QueryId qid2 = new QueryId();
		qid2.root = "12345";
		qid2.extension = "123";
		QueryAck qa = new QueryAck();
		qa.setQueryId(qid2);
		
		ControlActProcess cap = new ControlActProcess();
		cap.setQueryByParameter(qbp);
		cap.setQueryAck(qa);
		Code code = new Code();
		code.code = "PRPA_TE201306UV02";
		code.codeSystem = "12345";
		cap.setCode(code);
		
		Subject subject1 = new Subject();
		subject1.typeCode = "SUBJ";
		ArrayList<Subject> subjs = new ArrayList<Subject>();
		
		RegistrationEvent re = new RegistrationEvent();
		re.classCode = "REG";
		re.moodCode = "EVN";
		Id regId = new Id();
		regId.nullFlavor = "NA";
		StatusCode regSc = new StatusCode();
		regSc.code = "active";
		
		SubjectOne s1 = new SubjectOne();
		s1.typeCode = "SBJ";
		
		Patient patient = new Patient();
		patient.classCode = "PAT";
		Id patId = new Id();
		patId.root = "12345";
		patId.extension = "123";
		StatusCode patStatus = new StatusCode();
		patStatus.code = "12345";
		patient.setId(patId);
		patient.setStatusCode(patStatus);
		
		PatientPerson pp = new PatientPerson();
		ProviderOrganization po = new ProviderOrganization();
		SubjectOfOne soo = new SubjectOfOne();
		patient.setPp(pp);
		patient.setProviderOrganization(po);
		patient.setSubjectOf1(soo);
		
		s1.setPatient(patient);
		
		re.setSubject1(s1);
		
		Custodian cust = new Custodian();
		
		re.setCustodian(cust);
		
		subjs.add(subject1);
		cap.subjects = subjs;
		
		QueryAck queryAck = new QueryAck();
		QueryId qid1 = new QueryId();
		qid1.root = "12345";
		qid1.extension = "123";
		
		queryAck.setQueryId(qid1);
		cap.setQueryAck(queryAck);
		cap.setQueryByParameter(qbp);
		
		pdr.controlActProcess = cap;
		
		DiscoveryRequestSoapEnvelope se = new DiscoveryRequestSoapEnvelope();
		DiscoveryRequestSoapHeader sh = new DiscoveryRequestSoapHeader();
		DiscoveryRequestSoapBody sb = new DiscoveryRequestSoapBody();
		sb.PRPA_IN201305UV02 = pdr;
		
		Action action = new Action();
		CorrelationTimeToLive cttl = new CorrelationTimeToLive();
		
		sh.action = action;
		sh.cttl = cttl;
		Security security = new Security();
		Timestamp time = new Timestamp();
		Created created = new Created();
		Expires expires = new Expires();
		KeyInfo keyInfo = new KeyInfo();
		SecurityTokenReference str = new SecurityTokenReference();
		KeyIdentifier keyIdentifier = new KeyIdentifier();
		DateTime now = new DateTime();
		created.value = now.toString();
		expires.value = now.plusHours(1).toString();
		time.id = "_1";
		time.created = created;
		time.expires = expires;
		security.timestamp = time;
		keyIdentifier.valueType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID";
		keyIdentifier.value = "ed62b6fb-4d73-4011-9f7c-43e0575b6317";
		str.id = "uuid_2ca69267-90bd-4785-a28e-ad9cee6d962e";
		str.tokenType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
		str.keyIdentifier = keyIdentifier;
		keyInfo.securityTokenReference = str;
		security.keyInfo = keyInfo;
		security.assertion = samlMessage;
		sh.security = security;
		
		se.sHeader = sh;
		se.sBody = sb;
		
		return se;
	}
	
	public static QueryRequestSoapEnvelope generateQueryRequest(AssertionImpl samlMessage){
		QueryRequestSoapEnvelope se = new QueryRequestSoapEnvelope();
		QueryRequestSoapHeader sh = new QueryRequestSoapHeader();
		QueryRequestSoapBody sb = new QueryRequestSoapBody();
		AdhocQueryRequest aqr = new AdhocQueryRequest();
		RegistryObjectList rol = new RegistryObjectList();
		ExtrinsicObject eo = new ExtrinsicObject();

		Slot slot1 = new Slot();
		ValueList vl1 = new ValueList();
		ArrayList<String> values1 = new ArrayList<String>();
		vl1.value = values1;
		vl1.value.add("http://localhost:8080/XDS/Repository/08a15a6f-5b4a-42de-8f95-89474f83abdf.xml");
		slot1.valueList = vl1;
		slot1.name = "URI";

		Slot slot2 = new Slot();
		ValueList vl2 = new ValueList();
		ArrayList<String> values2 = new ArrayList<String>();
		vl2.value = values2;
		vl2.value.add(String.valueOf(System.currentTimeMillis()));
		slot2.valueList = vl2;
		slot2.name = "creationTime";
		
		Slot slot3 = new Slot();
		ValueList vl3 = new ValueList();
		ArrayList<String> values3 = new ArrayList<String>();
		vl3.value = values3;
		vl3.value.add("12345");
		slot3.valueList = vl3;
		slot3.name = "sourcePatientId";
		
		Classification classification = new Classification();
		Slot slot4 = new Slot();
		ValueList vl4 = new ValueList();
		ArrayList<String> values4 = new ArrayList<String>();
		vl4.value = values4;
		vl4.value.add("12345");
		slot4.valueList = vl4;
		slot4.name = "codingScheme";
		Name name = new Name();
		LocalizedString ls = new LocalizedString();
		ls.charset = "UTF-8";
		ls.value = "Celebrity";
		name.localizedString = ls;
		classification.name = name;
		classification.slot = slot4;
		
		ArrayList<Slot> slots = new ArrayList<Slot>();
		eo.slots = slots;
		eo.slots.add(slot1);
		eo.slots.add(slot2);
		eo.slots.add(slot3);
		ArrayList<Classification> classArray = new ArrayList<Classification>();
		eo.classification = classArray;
		eo.classification.add(classification);
		
		rol.extrinsicObject = eo;
		sb.adhocQueryRequest = aqr;
		
		Action action = new Action();
		sh.action = action;
		sh.action.mustUnderstand = "1";
		sh.action.action = "urn:ihe:iti:2008:RegistryStoredQueryAsyncRequest";
		MessageId messageId = new MessageId();
		sh.messageId = messageId;
		sh.messageId.messageId = "urn:uuid:D6C21225-8E7B-454E-9750-821622C099DB";
		To to = new To();
		sh.to = to;
		sh.to.mustUnderstand = "1";
		sh.to.to = "http://localhost:2647/XdsService/DocumentConsumerReceiver.svc";
		
		
		Security security = new Security();
		Timestamp time = new Timestamp();
		Created created = new Created();
		Expires expires = new Expires();
		KeyInfo keyInfo = new KeyInfo();
		SecurityTokenReference str = new SecurityTokenReference();
		KeyIdentifier keyIdentifier = new KeyIdentifier();
		DateTime now = new DateTime();
		created.value = now.toString();
		expires.value = now.plusHours(1).toString();
		time.id = "_1";
		time.created = created;
		time.expires = expires;
		security.timestamp = time;
		keyIdentifier.valueType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID";
		keyIdentifier.value = "ed62b6fb-4d73-4011-9f7c-43e0575b6317";
		str.id = "uuid_2ca69267-90bd-4785-a28e-ad9cee6d962e";
		str.tokenType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
		str.keyIdentifier = keyIdentifier;
		keyInfo.securityTokenReference = str;
		security.keyInfo = keyInfo;
		sh.security = security;
		security.assertion = samlMessage;
		
		se.body = sb;
		se.header = sh;
		
		return se;
	}
	
	public static RetrieveDocumentSetRequestSoapEnvelope generateDocumentRequest(AssertionImpl samlMessage){
		
		RetrieveDocumentSetRequestSoapEnvelope rdse = new RetrieveDocumentSetRequestSoapEnvelope();
		RetrieveDocumentSetRequestSoapBody rdsb = new RetrieveDocumentSetRequestSoapBody();
		RetrieveDocumentSetRequestSoapHeader rdsh = new RetrieveDocumentSetRequestSoapHeader();
		
		Action action = new Action();
		rdsh.action = action;
		rdsh.action.mustUnderstand = "1";
		rdsh.action.action = "urn:ihe:iti:2008:RegistryStoredQueryAsyncRequest";
		MessageId messageId = new MessageId();
		rdsh.messageId = messageId;
		rdsh.messageId.messageId = "urn:uuid:D6C21225-8E7B-454E-9750-821622C099DB";
		To to = new To();
		rdsh.to = to;
		rdsh.to.mustUnderstand = "1";
		rdsh.to.to = "http://localhost:2647/XdsService/DocumentConsumerReceiver.svc";
		
		Security security = new Security();
		Timestamp time = new Timestamp();
		Created created = new Created();
		Expires expires = new Expires();
		KeyInfo keyInfo = new KeyInfo();
		SecurityTokenReference str = new SecurityTokenReference();
		KeyIdentifier keyIdentifier = new KeyIdentifier();
		DateTime now = new DateTime();
		created.value = now.toString();
		expires.value = now.plusHours(1).toString();
		time.id = "_1";
		time.created = created;
		time.expires = expires;
		security.timestamp = time;
		keyIdentifier.valueType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID";
		keyIdentifier.value = "ed62b6fb-4d73-4011-9f7c-43e0575b6317";
		str.id = "uuid_2ca69267-90bd-4785-a28e-ad9cee6d962e";
		str.tokenType = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
		str.keyIdentifier = keyIdentifier;
		keyInfo.securityTokenReference = str;
		security.keyInfo = keyInfo;
		security.assertion = samlMessage;
		rdsh.security = security;
		security.assertion = samlMessage;
		
		RetrieveDocumentSetRequest rdsr = new RetrieveDocumentSetRequest();
		
		DocumentRequest dr = new DocumentRequest();
		dr.repositoryUniqueId = "12345";
		dr.documentUniqueId = "12345";
		
		ArrayList<DocumentRequest> drArray = new ArrayList<DocumentRequest>();
		rdsr.documentRequest = drArray;
		rdsr.documentRequest.add(dr);
		
		rdse.body = rdsb;
		rdse.header = rdsh;
		
		return rdse;
	}
	
	public static DiscoveryResponseSoapEnvelope generateDiscoveryResponse(String givenInput, String familyInput){
		PatientDiscoveryResponse pdr = new PatientDiscoveryResponse();
		CreationTime ct = new CreationTime();
		ct.value = (String.valueOf(System.currentTimeMillis()));
		pdr.creationTime = ct;
		InteractionId interId = new InteractionId();
		interId.root = "12345";
		pdr.interactionId = interId;
		ProcessingCode pc = new ProcessingCode();
		pc.code = "P";
		pdr.processingCode = pc;
		ProcessingModeCode pcm = new ProcessingModeCode();
		pcm.code = "T";
		pdr.processingModeCode = pcm;
		AcceptAckCode aac = new AcceptAckCode();
		aac.code = "NE";
		pdr.acceptAckCode = aac;
		
		Id id1 = new Id();
		id1.root = "12345";
		Device device1 = new Device();
		device1.setId(id1);
		Receiver receiver = new Receiver();
		receiver.typeCode = "RCV";
		receiver.device = device1;
		pdr.receiver = receiver;
		
		Id id2 = new Id();
		id2.root = "12345";
		Device device2 = new Device();
		device2.setId(id2);
		Sender sender = new Sender();
		sender.typeCode = "SND";
		sender.device = device2;
		pdr.sender = sender;
		
		// acknowledgement
		Id ackId = new Id();
		ackId.root = "12345";
		ackId.extension = "12345";
		TargetMessage tm = new TargetMessage();
		tm.setId(ackId);
		TypeCode tc = new TypeCode();
		tc.code = "1234";
		Acknowledgement ack = new Acknowledgement();
		ack.setTypeCode(tc);
		ack.setTargetMessage(tm);
		pdr.acknowledgement = ack;
		
		//queryByParameter
		QueryByParameter qbp = new QueryByParameter();
		
		QueryId qid = new QueryId();
		qid.root = "1234";
		qid.extension = "12345";
		StatusCode sc = new StatusCode();
		sc.code = "12345";
		qbp.setQueryId(qid);
		qbp.setStatusCode(sc);
		
		ParameterList pl = new ParameterList();
		
		Id lsidv = new Id();
		lsidv.root = "12345";
		lsidv.extension = "123";
		LivingSubjectId lsid = new LivingSubjectId();
		lsid.semanticsText = "LivingSubject.id";
		lsid.setLsiv(lsidv);
		ArrayList<LivingSubjectId> array = new ArrayList<LivingSubjectId>();
		array.add(lsid);
		pl.livingSubjectId = array;
		
		String given = givenInput;
		String family = familyInput;
		LivingSubjectNameValue lsnv = new LivingSubjectNameValue();
		lsnv.given = given;
		lsnv.family = family;
		LivingSubjectName lsn = new LivingSubjectName();
		lsn.setValue(lsnv);
		lsn.semanticsText = "LivingSubject.name";
		pl.setLivingSubjectName(lsn);
		
		LivingSubjBirthTimeValue lsbtv = new LivingSubjBirthTimeValue();
		lsbtv.value = "12345";
		LivingSubjectBirthTime lsbt = new LivingSubjectBirthTime();
		lsbt.semanticsText = "LivingSubject.birthTime";
		lsbt.setValue(lsbtv);
		pl.setLivingSubjectBirthTime(lsbt);
		
		LivingSubjectAdminGenderValue lsagv = new LivingSubjectAdminGenderValue();
		lsagv.code = "M";
		LivingSubjectAdminGender lsag = new LivingSubjectAdminGender();
		lsag.value = lsagv;
		lsag.semanticsText = "LivingSubject.administrativeGender";
		pl.setLivingSubjectAdministrativeGender(lsag);
		
		qbp.parameterList = pl;
		
		QueryId qid2 = new QueryId();
		qid2.root = "12345";
		qid2.extension = "123";
		QueryReponseCode qrc = new QueryReponseCode();
		qrc.code = "OK";
		QueryAck qa = new QueryAck();
		qa.setQueryId(qid2);
		qa.setQueryResponseCode(qrc);
		
		ControlActProcess cap = new ControlActProcess();
		cap.setQueryByParameter(qbp);
		cap.setQueryAck(qa);
		Code code = new Code();
		code.code = "PRPA_TE201306UV02";
		code.codeSystem = "12345";
		cap.setCode(code);
		
		Subject subject1 = new Subject();
		subject1.typeCode = "SUBJ";
		ArrayList<Subject> subjs = new ArrayList<Subject>();
		
		RegistrationEvent re = new RegistrationEvent();
		re.classCode = "REG";
		re.moodCode = "EVN";
		Id regId = new Id();
		regId.nullFlavor = "NA";
		StatusCode regSc = new StatusCode();
		regSc.code = "active";
		
		SubjectOne s1 = new SubjectOne();
		s1.typeCode = "SBJ";
		
		Patient patient = new Patient();
		patient.classCode = "PAT";
		Id patId = new Id();
		patId.root = "12345";
		patId.extension = "123";
		StatusCode patStatus = new StatusCode();
		patStatus.code = "12345";
		patient.setId(patId);
		patient.setStatusCode(patStatus);
		
		PatientPerson pp = new PatientPerson();
		ProviderOrganization po = new ProviderOrganization();
		SubjectOfOne soo = new SubjectOfOne();
		patient.setPp(pp);
		patient.setProviderOrganization(po);
		patient.setSubjectOf1(soo);
		
		s1.setPatient(patient);
		
		re.setSubject1(s1);
		
		Custodian cust = new Custodian();
		
		re.setCustodian(cust);
		
		subjs.add(subject1);
		cap.subjects = subjs;
		
		QueryAck queryAck = new QueryAck();
		QueryId qid1 = new QueryId();
		qid1.root = "12345";
		qid1.extension = "123";
		
		QueryReponseCode qrc1 = new QueryReponseCode();
		qrc1.code = "OK";
		
		queryAck.setQueryId(qid1);
		queryAck.setQueryResponseCode(qrc1);
		cap.setQueryAck(queryAck);
		cap.setQueryByParameter(qbp);
		
		pdr.controlActProcess = cap;
		
		DiscoveryResponseSoapEnvelope se = new DiscoveryResponseSoapEnvelope();
		DiscoveryResponseSoapHeader sh = new DiscoveryResponseSoapHeader();
		DiscoveryResponseSoapBody sb = new DiscoveryResponseSoapBody();
		sb.PRPA_IN201306UV02 = pdr;
		
		Action action = new Action();
		CorrelationTimeToLive cttl = new CorrelationTimeToLive();
		RelatesTo rt = new RelatesTo();
		
		sh.action = action;
		sh.cttl = cttl;
		sh.relatesTo = rt;
		
		se.sHeader = sh;
		se.sBody = sb;
		
		return se;
	}
	
	public static QueryResponseSoapEnvelope generateQueryResponse(){
		QueryResponseSoapEnvelope se = new QueryResponseSoapEnvelope();
		QueryResponseSoapHeader sh = new QueryResponseSoapHeader();
		QueryResponseSoapBody sb = new QueryResponseSoapBody();
		AdhocQueryResponse aqr = new AdhocQueryResponse();
		RegistryObjectList rol = new RegistryObjectList();
		ExtrinsicObject eo = new ExtrinsicObject();

		Slot slot1 = new Slot();
		ValueList vl1 = new ValueList();
		ArrayList<String> values1 = new ArrayList<String>();
		vl1.value = values1;
		vl1.value.add("http://localhost:8080/XDS/Repository/08a15a6f-5b4a-42de-8f95-89474f83abdf.xml");
		slot1.valueList = vl1;
		slot1.name = "URI";

		Slot slot2 = new Slot();
		ValueList vl2 = new ValueList();
		ArrayList<String> values2 = new ArrayList<String>();
		vl2.value = values2;
		vl2.value.add(String.valueOf(System.currentTimeMillis()));
		slot2.valueList = vl2;
		slot2.name = "creationTime";
		
		Slot slot3 = new Slot();
		ValueList vl3 = new ValueList();
		ArrayList<String> values3 = new ArrayList<String>();
		vl3.value = values3;
		vl3.value.add("12345");
		slot3.valueList = vl3;
		slot3.name = "sourcePatientId";
		
		Classification classification = new Classification();
		Slot slot4 = new Slot();
		ValueList vl4 = new ValueList();
		ArrayList<String> values4 = new ArrayList<String>();
		vl4.value = values4;
		vl4.value.add("12345");
		slot4.valueList = vl4;
		slot4.name = "codingScheme";
		Name name = new Name();
		LocalizedString ls = new LocalizedString();
		ls.charset = "UTF-8";
		ls.value = "Celebrity";
		name.localizedString = ls;
		classification.name = name;
		classification.slot = slot4;
		
		ArrayList<Slot> slots = new ArrayList<Slot>();
		eo.slots = slots;
		eo.slots.add(slot1);
		eo.slots.add(slot2);
		eo.slots.add(slot3);
		ArrayList<Classification> classArray = new ArrayList<Classification>();
		eo.classification = classArray;
		eo.classification.add(classification);
		
		rol.extrinsicObject = eo;
		aqr.registryObjectList = rol;
		sb.adhocQueryResponse = aqr;
		
		Action action = new Action();
		sh.action = action;
		sh.action.mustUnderstand = "1";
		sh.action.action = "urn:ihe:iti:2008:RegistryStoredQueryAsyncResponse";
		MessageId messageId = new MessageId();
		sh.messageId = messageId;
		sh.messageId.messageId = "urn:uuid:D6C21225-8E7B-454E-9750-821622C099DB";
		RelatesTo relatesTo = new RelatesTo();
		sh.relatesTo = relatesTo;
		sh.relatesTo.relatesTo = "urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055";
		To to = new To();
		sh.to = to;
		sh.to.mustUnderstand = "1";
		sh.to.to = "http://localhost:2647/XdsService/DocumentConsumerReceiver.svc";
		
		se.body = sb;
		se.header = sh;
		
		return se;
	}
	
	public static RetrieveDocumentSetResponseSoapEnvelope generateDocumentResponse(){
		
		RetrieveDocumentSetResponseSoapEnvelope rdse = new RetrieveDocumentSetResponseSoapEnvelope();
		RetrieveDocumentSetResponseSoapBody rdsb = new RetrieveDocumentSetResponseSoapBody();
		RetrieveDocumentSetResponseSoapHeader rdsh = new RetrieveDocumentSetResponseSoapHeader();
		
		Action action = new Action();
		rdsh.action = action;
		rdsh.action.mustUnderstand = "1";
		rdsh.action.action = "urn:ihe:iti:2008:RegistryStoredQueryAsyncResponse";
		MessageId messageId = new MessageId();
		rdsh.messageId = messageId;
		rdsh.messageId.messageId = "urn:uuid:D6C21225-8E7B-454E-9750-821622C099DB";
		RelatesTo relatesTo = new RelatesTo();
		rdsh.relatesTo = relatesTo;
		rdsh.relatesTo.relatesTo = "urn:uuid:a02ca8cd-86fa-4afc-a27c-616c183b2055";
		To to = new To();
		rdsh.to = to;
		rdsh.to.mustUnderstand = "1";
		rdsh.to.to = "http://localhost:2647/XdsService/DocumentConsumerReceiver.svc";
		
		RetrieveDocumentSetResponse rdsr = new RetrieveDocumentSetResponse();
		RegistryResponse rr = new RegistryResponse();
		rr.status = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
		
		DocumentResponse dr = new DocumentResponse();
		dr.repositoryUniqueId = "12345";
		dr.documentUniqueId = "12345";
		dr.mimeType = "text/xml";
		dr.document = "123456789023w09rew98rp9ew8ry";
		
		rdsr.registryReponse = rr;
		ArrayList<DocumentResponse> drArray = new ArrayList<DocumentResponse>();
		rdsr.documentResponse = drArray;
		rdsr.documentResponse.add(dr);
		
		rdsb.retrieveDocumentSetResponse = rdsr;
		
		rdse.body = rdsb;
		rdse.header = rdsh;
		
		return rdse;
	}
	
}
