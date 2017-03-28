package gov.ca.emsa;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.w3c.dom.Node;

import gov.ca.emsa.domain.Patient;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockTestConfig.class)
public class ReadPatientsFileTests extends TestCase {
	
	@Test
	public void testParseZonedDateTime() {
		// option a - parsed from the string
		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
		ZonedDateTime zdt = ZonedDateTime.parse("2016-01-10T04:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
		zdt = zdt.truncatedTo(ChronoUnit.DAYS);
		
		 DateTimeFormatter patientDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		 LocalDate date = LocalDate.parse("01/10/2016", patientDateFormatter);
		 ZonedDateTime result = date.atStartOfDay(ZoneId.of("GMT"));
		 result = result.truncatedTo(ChronoUnit.DAYS);
		 
		assertTrue(zdt.isEqual(result));
		 
	}
	
	@Test
	public void testGetLocalDate() {
		String dob = "1983-02-05T05:00:00.000Z";
		LocalDate result = LocalDate.parse(dob, DateTimeFormatter.ISO_DATE_TIME);
		assertNotNull(result);
		System.out.println(result);
	}
}