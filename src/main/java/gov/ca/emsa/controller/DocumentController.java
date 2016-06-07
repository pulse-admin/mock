package gov.ca.emsa.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.domain.Document;
import gov.ca.emsa.domain.Organization;
import gov.ca.emsa.domain.Patient;
import io.swagger.annotations.Api;

@RestController
@Api(value="/mock")
@RequestMapping("/mock")
public class DocumentController {
	private static final Logger logger = LogManager.getLogger(DocumentController.class);
	private static final String E_HEALTH_DOCUMENTS_FILE_NAME = "eHealthdocuments.csv";
	private static final String E_HEALTH_CCDA_DOCUMENT = "VCN CCDA.xml";
	private static final String IHE_DOCUMENTS_FILE_NAME = "IHEdocuments.csv";
	private static final String IHE_CCDA_DOCUMENT = "367 XDR.xml";
	
	@Autowired private ResourceLoader resourceLoader;
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/ehealthexchange/documents", method = RequestMethod.POST, 
			produces="application/json; charset=utf-8")
	public List<Document> getEHealthDocuments(@RequestParam(value="patientId", required=true) String patientId,
			@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
		
		Resource documentsFile = resourceLoader.getResource("classpath:" + E_HEALTH_DOCUMENTS_FILE_NAME);
		
    	//load all patients from the file
		BufferedReader reader = null;
		CSVParser parser = null;
		try {
			reader = new BufferedReader(new InputStreamReader(documentsFile.getInputStream()));
			parser = new CSVParser(reader, CSVFormat.EXCEL);
			
			List<CSVRecord> records = parser.getRecords();
			if(records.size() <= 1) {
				throw new IOException("The file appears to have a header line with no other information. Please make sure there are at least two rows in the CSV file.");
			}
			
			List<Document> docsForPatient = new ArrayList<Document>();
			for(CSVRecord record : records) {
				String colValue = record.get(0).toString().trim();
				if(!StringUtils.isEmpty(colValue) && !"ID".equals(colValue)) {
					String docPatientId = record.get(1).toString().trim();
					if(!StringUtils.isEmpty(docPatientId) && docPatientId.equals(patientId)) {
						Document document = new Document();
						document.setId(colValue);
						document.setName(record.get(2).toString().trim());
						
						Patient patient = new Patient();
						patient.setOrgPatientId(docPatientId);
						document.setPatient(patient);
						
						docsForPatient.add(document);
					}
				}
			}
			return docsForPatient;
		} catch(IOException ioEx) {
			logger.error("Could not get input stream for uploaded file " + E_HEALTH_DOCUMENTS_FILE_NAME);			
			throw new IOException("Could not get input stream for file " + E_HEALTH_DOCUMENTS_FILE_NAME);
		} finally {
			 try { parser.close(); } catch(Exception ignore) {}
			try { reader.close(); } catch(Exception ignore) {}
		}
    }
	
	@RequestMapping(value= "/ehealthexchange/document/{documentId}", method = RequestMethod.POST, 
			produces="application/xml; charset=utf-8")
	public String getEHealthDocument(@PathVariable(value="documentId") String documentId, 
			HttpServletResponse response, @RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
		Resource documentFile = resourceLoader.getResource("classpath:" + E_HEALTH_CCDA_DOCUMENT);
		byte[] buffer = new byte[(int)documentFile.contentLength()];
		IOUtils.readFully(documentFile.getInputStream(), buffer);
		return new String(buffer);
	}
	
	@RequestMapping(value= "/ehealthexchange/document/{documentId}/download", method = RequestMethod.POST, 
			produces="application/xml; charset=utf-8")
	public void downloadEHealthDocument(@PathVariable(value="documentId") String documentId, 
			HttpServletResponse response,@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
		
		Resource documentFile = resourceLoader.getResource("classpath:" + E_HEALTH_CCDA_DOCUMENT);
		response.setContentLength((int) documentFile.contentLength());
	 
		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
	                documentFile.getFilename());
		response.setHeader(headerKey, headerValue);
	 
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
	 
		byte[] buffer = new byte[1024];
		int bytesRead = -1;
		
		InputStream inputStream = documentFile.getInputStream();
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
	 
		inputStream.close();
		outStream.close();	   
	}
	
	//note that the first name and last name search params must be a valid java regex
		@RequestMapping(value= "/ihe/documents", method = RequestMethod.POST, 
				produces="application/json; charset=utf-8")
		public List<Document> getIHEDocuments(@RequestParam(value="patientId", required=true) String patientId,
				@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
			
			Resource documentsFile = resourceLoader.getResource("classpath:" + IHE_DOCUMENTS_FILE_NAME);
			
	    	//load all patients from the file
			BufferedReader reader = null;
			CSVParser parser = null;
			try {
				reader = new BufferedReader(new InputStreamReader(documentsFile.getInputStream()));
				parser = new CSVParser(reader, CSVFormat.EXCEL);
				
				List<CSVRecord> records = parser.getRecords();
				if(records.size() <= 1) {
					throw new IOException("The file appears to have a header line with no other information. Please make sure there are at least two rows in the CSV file.");
				}
				
				List<Document> docsForPatient = new ArrayList<Document>();
				for(CSVRecord record : records) {
					String colValue = record.get(0).toString().trim();
					if(!StringUtils.isEmpty(colValue) && !"ID".equals(colValue)) {
						String docPatientId = record.get(1).toString().trim();
						if(!StringUtils.isEmpty(docPatientId) && docPatientId.equals(patientId)) {
							Document document = new Document();
							document.setId(colValue);
							document.setName(record.get(2).toString().trim());
							
							Patient patient = new Patient();
							patient.setOrgPatientId(docPatientId);
							document.setPatient(patient);
							
							docsForPatient.add(document);
						}
					}
				}
				return docsForPatient;
			} catch(IOException ioEx) {
				logger.error("Could not get input stream for uploaded file " + IHE_DOCUMENTS_FILE_NAME);			
				throw new IOException("Could not get input stream for file " + IHE_DOCUMENTS_FILE_NAME);
			} finally {
				 try { parser.close(); } catch(Exception ignore) {}
				try { reader.close(); } catch(Exception ignore) {}
			}
	    }
		
		@RequestMapping(value= "/ihe/document/{documentId}", method = RequestMethod.POST, 
				produces="application/xml; charset=utf-8")
		public String getIHEDocument(@PathVariable(value="documentId") String documentId, 
				HttpServletResponse response, @RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {

			Resource documentFile = resourceLoader.getResource("classpath:" + IHE_CCDA_DOCUMENT);
			byte[] buffer = new byte[(int)documentFile.contentLength()];
			IOUtils.readFully(documentFile.getInputStream(), buffer);
			return new String(buffer);
		}
		
		@RequestMapping(value= "/ihe/document/{documentId}/download", method = RequestMethod.POST, 
				produces="application/xml; charset=utf-8")
		public void downloadIHEDocument(@PathVariable(value="documentId") String documentId, 
				HttpServletResponse response,@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {

			Resource documentFile = resourceLoader.getResource("classpath:" + IHE_CCDA_DOCUMENT);
			response.setContentLength((int) documentFile.contentLength());
		 
			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
		                documentFile.getFilename());
			response.setHeader(headerKey, headerValue);
		 
			// get output stream of the response
			OutputStream outStream = response.getOutputStream();
		 
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			
			InputStream inputStream = documentFile.getInputStream();
			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		 
			inputStream.close();
			outStream.close();	   
		}
}
