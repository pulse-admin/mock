package gov.ca.emsa.controller;

import gov.ca.emsa.Utils;
import gov.ca.emsa.domain.Organization;
import io.swagger.annotations.Api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="/mock/directory")
public class DirectoryController {
	@Autowired
	private ResourceLoader resourceLoader;
	@RequestMapping(value= "/mock/directory", method = RequestMethod.GET, produces="application/json; charset=utf-8")
	public List<Organization> directory() {
		Resource resource = resourceLoader.getResource("classpath:organizations.txt");
    	try {
			return Utils.readOrganizations(resource.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Organization>();
		}
    }
}
