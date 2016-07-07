package gov.ca.emsa.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.Utils;
import gov.ca.emsa.domain.Organization;
import io.swagger.annotations.Api;

@RestController
@Api(value="/mock/directory")
public class DirectoryController {
	private static final String RESOURCE_FILE_NAME = "organizations1.xml";
	private static final Logger logger = LogManager.getLogger(DirectoryController.class);
	@Autowired private ResourceLoader resourceLoader;
	
	@RequestMapping(value= "/mock/directory", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public List<Organization> directory() {
		Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
		
		List<Organization> results = null;
		try {
			results = Utils.readOrganizations(documentsFile.getInputStream());
		} catch(IOException ioEx) {
			logger.error("Could not read documents file " + documentsFile.getFilename(), ioEx);
		}
		return results;
	}
}