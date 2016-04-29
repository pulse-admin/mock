package gov.ca.emsa;

import gov.ca.emsa.*;
import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="/mock")
public class MockController {

    @RequestMapping(value= "/mock", method = RequestMethod.GET,produces="application/json; charset=utf-8")
    public Mock mock() {
    	return new Mock("This is the Mock service.");
    }
}
