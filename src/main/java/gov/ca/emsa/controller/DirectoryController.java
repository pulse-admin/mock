package gov.ca.emsa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	private String xmlLocation = "C:/Users/blindsey/workspace/pulse/mock/src/main/resources/organizations1.xml";
	@Autowired
	private ResourceLoader resourceLoader;
	@RequestMapping(value= "/mock/directory", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public List<Organization> directory() {
    	return Utils.readOrganizations(xmlLocation);
    }
}