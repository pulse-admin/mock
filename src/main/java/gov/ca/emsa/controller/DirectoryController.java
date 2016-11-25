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

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.cten.domain.Endpoint;
import gov.ca.emsa.pulse.cten.domain.EndpointWrapper;
import gov.ca.emsa.pulse.cten.domain.Location;
import gov.ca.emsa.pulse.cten.domain.LocationWrapper;
import gov.ca.emsa.pulse.cten.domain.Organization;
import gov.ca.emsa.pulse.cten.domain.OrganizationWrapper;
import io.swagger.annotations.Api;

@RestController
@Api(value="/mock")
public class DirectoryController {
	private static final Logger logger = LogManager.getLogger(DirectoryController.class);
	private static final String ORGANIZATION_RESOURCE_FILE_NAME = "organizations.json";
	private static final String LOCATIONS_RESOURCE_FILE_NAME = "locations.json";
	private static final String ENDPOINTS_RESOURCE_FILE_NAME = "endpoints.json";

	@Autowired private ResourceLoader resourceLoader;
	private ObjectMapper jsonMapper;
	
	public DirectoryController() {
		jsonMapper = new ObjectMapper();
	}
	
	@RequestMapping(value= "/Organization", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public OrganizationWrapper getOrganizations() {
		Resource organizationsFile = resourceLoader.getResource("classpath:" + ORGANIZATION_RESOURCE_FILE_NAME);
		OrganizationWrapper parsed = null;
		try {
			parsed = jsonMapper.readValue(organizationsFile.getInputStream(), OrganizationWrapper.class);
		} catch(IOException ex) {
			logger.error("Could not parse organizations file", ex);
		}
		return parsed;
	}
	
	@RequestMapping(value= "/Location", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public LocationWrapper getLocations() {
		Resource locationsFile = resourceLoader.getResource("classpath:" + LOCATIONS_RESOURCE_FILE_NAME);
		LocationWrapper parsed = null;
		try {
			parsed = jsonMapper.readValue(locationsFile.getInputStream(), LocationWrapper.class);
		} catch(IOException ex) {
			logger.error("Could not parse locations file", ex);
		}
		return parsed;
	}
	
	@RequestMapping(value= "/Endpoint", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public EndpointWrapper getEndpoints() {
		Resource endpointsFile = resourceLoader.getResource("classpath:" + ENDPOINTS_RESOURCE_FILE_NAME);
		EndpointWrapper parsed = null;
		try {
			parsed = jsonMapper.readValue(endpointsFile.getInputStream(), EndpointWrapper.class);
		} catch(IOException ex) {
			logger.error("Could not parse endpoints file", ex);
		}
		return parsed;
	}
}