package gov.ca.emsa.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.domain.Document;
import gov.ca.emsa.domain.Organization;
import gov.ca.emsa.domain.Patient;
import io.swagger.annotations.Api;

@RestController
@Api(value="/mock/ehealthexchange")
@RequestMapping("/mock/ehealthexchange")
public class DocumentController {
	private static final Logger logger = LogManager.getLogger(DocumentController.class);
	private static final String DOCUMENTS_FILE_NAME = "documents.csv";
	
	@Autowired private ResourceLoader resourceLoader;
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/documents", method = RequestMethod.GET, 
			produces="application/json; charset=utf-8")
	public List<Document> getDocuments(@RequestParam(value="patientId", required=true) String patientId) throws IOException {
		
		Resource documentsFile = resourceLoader.getResource("classpath:" + DOCUMENTS_FILE_NAME);
		
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
						document.setName(record.get(3).toString().trim());
						
						Patient patient = new Patient();
						patient.setId(docPatientId);
						document.setPatient(patient);
						
						String orgName = record.get(2).toString().trim();
						Organization org = new Organization(orgName);
						document.setOrganization(org);
						
						docsForPatient.add(document);
					}
				}
			}
			return docsForPatient;
		} catch(IOException ioEx) {
			logger.error("Could not get input stream for uploaded file " + DOCUMENTS_FILE_NAME);			
			throw new IOException("Could not get input stream for file " + DOCUMENTS_FILE_NAME);
		} finally {
			 try { parser.close(); } catch(Exception ignore) {}
			try { reader.close(); } catch(Exception ignore) {}
		}
    }
}
