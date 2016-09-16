package gov.ca.emsa.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.domain.Document;
import io.swagger.annotations.Api;

@RestController
@Api(value="/mock")
@RequestMapping("/mock")
public class DocumentController {
	private static final Logger logger = LogManager.getLogger(DocumentController.class);
	private static final String CCDA_RESOURCE_DIR = "ccdas";
	@Autowired private ResourceLoader resourceLoader;
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/ehealthexchange/documents", method = RequestMethod.POST, 
			produces="application/json; charset=utf-8")
	public List<Document> getEHealthDocuments(@RequestParam(value="patientId", required=true) String patientId,
			@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
		
		List<Document> docsForPatient = new ArrayList<Document>();
		List<File> ccdaResults = getRandomFileList();
		for(File ccda : ccdaResults) {
			Document document = new Document();
			document.setId(ccda.getName()+"-ID");
			document.setName(ccda.getName());
			docsForPatient.add(document);
		}
		return docsForPatient;

    }
	
	@RequestMapping(value= "/ehealthexchange/document", method = RequestMethod.POST, 
			produces="application/xml; charset=utf-8")
	public String getEHealthDocument(@RequestParam(value="name") String docName, 
			HttpServletResponse response, @RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
		
		Resource documentFile = resourceLoader.getResource("classpath:" + CCDA_RESOURCE_DIR + File.separator + docName);		
		byte[] buffer = new byte[(int)documentFile.contentLength()];
		IOUtils.readFully(documentFile.getInputStream(), buffer);
		return new String(buffer);
	}
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/ihe/documents", method = RequestMethod.POST, 
			produces="application/json; charset=utf-8")
	public List<Document> getIHEDocuments(@RequestParam(value="patientId", required=true) String patientId,
			@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {
		

		List<Document> docsForPatient = new ArrayList<Document>();
		List<File> ccdaResults = getRandomFileList();
		for(File ccda : ccdaResults) {
			Document document = new Document();
			document.setName(ccda.getName());
			docsForPatient.add(document);
		}
		return docsForPatient;
    }
		
	@RequestMapping(value= "/ihe/document", method = RequestMethod.POST, 
			produces="application/xml; charset=utf-8")
	public String getIHEDocument(@RequestParam(value="name") String docName, 
			HttpServletResponse response, @RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException {

		Resource documentFile = resourceLoader.getResource("classpath:" + CCDA_RESOURCE_DIR + File.separator + docName);		
		byte[] buffer = new byte[(int)documentFile.contentLength()];
		IOUtils.readFully(documentFile.getInputStream(), buffer);
		return new String(buffer);
	}
		
	private List<File> getRandomFileList() throws IOException {
		List<File> result = new ArrayList<File>();
		
		Resource ccdaResource = resourceLoader.getResource("classpath:" + CCDA_RESOURCE_DIR);
		File ccdaDir = ccdaResource.getFile();
		if(ccdaDir.exists() && ccdaDir.isDirectory()) {
			File[] ccdas = ccdaDir.listFiles();
			if(ccdas != null && ccdas.length > 0) {
				int numFilesToReturn = (int)(Math.round(Math.random()*ccdas.length));
				if(numFilesToReturn == 0) { numFilesToReturn = 1; }
				if(numFilesToReturn > ccdas.length) { numFilesToReturn = ccdas.length; }
				
				for(int i = 0; i < numFilesToReturn; i++) {
					result.add(ccdas[i]);
				}
			} else {
				logger.error("Could not find any ccdas in " + ccdaDir.getAbsolutePath());
			}
		}
		return result;
	}
}
