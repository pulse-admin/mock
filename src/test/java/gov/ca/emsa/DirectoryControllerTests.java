package gov.ca.emsa;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.cten.domain.Endpoint;
import gov.ca.emsa.pulse.cten.domain.EndpointResource;
import gov.ca.emsa.pulse.cten.domain.EndpointWrapper;
import gov.ca.emsa.pulse.cten.domain.Location;
import gov.ca.emsa.pulse.cten.domain.LocationResource;
import gov.ca.emsa.pulse.cten.domain.LocationWrapper;
import gov.ca.emsa.pulse.cten.domain.Organization;
import gov.ca.emsa.pulse.cten.domain.OrganizationResource;
import gov.ca.emsa.pulse.cten.domain.OrganizationWrapper;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockTestConfig.class)
public class DirectoryControllerTests extends TestCase {
	@Autowired private ResourceLoader resourceLoader;
	ObjectMapper jsonMapper;
	
	@Before
	public void setup() {
		 jsonMapper = new ObjectMapper();
	}
	
	@Test
	public void parseOrganizations() {
		OrganizationWrapper parsed = null;
		Resource orgsResource = resourceLoader.getResource("classpath:organizations.json");
		try {
			parsed = jsonMapper.readValue(orgsResource.getInputStream(), OrganizationWrapper.class);
		} catch(IOException ex) {
			ex.printStackTrace();
			fail("Caught IOException parsing organizations: " + ex.getMessage());
		}
		
		assertNotNull(parsed);
		assertEquals(3, parsed.getTotal().intValue());
		Organization firstOrg = parsed.getEntry().get(0);
		assertEquals("https://localhost:9080/mock/Organization/1", firstOrg.getFullUrl());
		OrganizationResource resource = firstOrg.getResource();
		assertEquals("John Muir Health Foundation", resource.getName());
		assertEquals("1", resource.getId());
		assertEquals("true", resource.getActive());
	}
	
	@Test
	public void parseLocations() {
		LocationWrapper parsed = null;
		Resource locationsResource = resourceLoader.getResource("classpath:locations.json");
		try {
			parsed = jsonMapper.readValue(locationsResource.getInputStream(), LocationWrapper.class);
		} catch(IOException ex) {
			ex.printStackTrace();
			fail("Caught IOException parsing locations: " + ex.getMessage());
		}
		
		assertNotNull(parsed);
		assertEquals(4, parsed.getTotal().intValue());
		Location firstLocation = parsed.getEntry().get(0);
		assertEquals("https://localhost:9080/mock/Location/1", firstLocation.getFullUrl());
		LocationResource resource = firstLocation.getResource();
		assertNotNull(resource.getEndpoint());
		assertEquals(5, resource.getEndpoint().size());
		assertEquals("John Muir Medical Center", resource.getName());
		assertNotNull(resource.getManagingOrganization());
		assertEquals("John Muir Health Foundation", resource.getManagingOrganization().getDisplay());
		assertEquals("1", resource.getId());
		assertEquals("active", resource.getStatus());
	}
	
	@Test
	public void parseEndpoints() {
		EndpointWrapper parsed = null;
		Resource endpointsResource = resourceLoader.getResource("classpath:endpoints.json");
		try {
			parsed = jsonMapper.readValue(endpointsResource.getInputStream(), EndpointWrapper.class);
		} catch(IOException ex) {
			ex.printStackTrace();
			fail("Caught IOException parsing endpoints: " + ex.getMessage());
		}
		
		assertNotNull(parsed);
		assertEquals(13, parsed.getTotal().intValue());
		Endpoint firstEndpoint = parsed.getEntry().get(0);
		assertEquals("http://localhost:9080/mock/Endpoint/1", firstEndpoint.getFullUrl());
		EndpointResource resource = firstEndpoint.getResource();
		assertEquals("1", resource.getId());
		assertEquals("application/xml", resource.getPayloadFormat());
		assertEquals("active", resource.getStatus());
		assertEquals("http://localhost:9080/patientDiscovery", resource.getAddress());
	}
	
}