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
@Api(value="/mock")
public class DirectoryController {
	private static final String ORGANIZATION_RESOURCE_FILE_NAME = "organizations.json";
	private static final String LOCATIONS_RESOURCE_FILE_NAME = "locations.json";
	private static final String ENDPOINTS_RESOURCE_FILE_NAME = "endpoints.json";
	private static final Logger logger = LogManager.getLogger(DirectoryController.class);
	@Autowired private ResourceLoader resourceLoader;
	
	@RequestMapping(value= "/Organization", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public List<Organization> getOrganizations() {
		Resource organizationsFile = resourceLoader.getResource("classpath:" + ORGANIZATION_RESOURCE_FILE_NAME);
		List<Organization> results = null;
		try {
			results = Utils.readOrganizations(organizationsFile.getInputStream());
		} catch(IOException ioEx) {
			logger.error("Could not read organizations file " + organizationsFile.getFilename(), ioEx);
		}
		return results;
	}
	
	@RequestMapping(value= "/Location", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public List<Organization> getLocations() {
		Resource locationsFile = resourceLoader.getResource("classpath:" + LOCATIONS_RESOURCE_FILE_NAME);
		List<Organization> results = null;
		try {
			results = Utils.readOrganizations(locationsFile.getInputStream());
		} catch(IOException ioEx) {
			logger.error("Could not read organizations file " + locationsFile.getFilename(), ioEx);
		}
		return results;
	}
	
	@RequestMapping(value= "/Endpoint", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public List<Organization> getEndpoints() {
		Resource endpointsFile = resourceLoader.getResource("classpath:" + ENDPOINTS_RESOURCE_FILE_NAME);
		List<Organization> results = null;
		try {
			results = Utils.readOrganizations(endpointsFile.getInputStream());
		} catch(IOException ioEx) {
			logger.error("Could not read organizations file " + endpointsFile.getFilename(), ioEx);
		}
		return results;
	}
}