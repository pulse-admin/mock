package gov.ca.emsa.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hl7.v3.Charset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.io.Resources;

import gov.ca.emsa.MockTestConfig;
import gov.ca.emsa.service.EHealthQueryConsumerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import junit.framework.TestCase;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockTestConfig.class)
public class DocumentSetRetrieveControllerTest extends TestCase {
	@Autowired private ResourceLoader resourceLoader;
	@Autowired EHealthQueryConsumerService consumerService;
	
	@Test
	public void testReadDocumentContents() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:" + "ValidDocumentSetResponse.xml");
		String fileContents = Resources.toString(resource.getURL(), Charsets.UTF_8);
		RetrieveDocumentSetResponseType docResponse = consumerService.unMarshallDocumentSetRetrieveResponseObject(fileContents);
		List<DocumentResponse> docResponses = docResponse.getDocumentResponse();
		for(DocumentResponse resp : docResponses) {
			DataHandler dh = resp.getDocument();
			InputStream in = dh.getDataSource().getInputStream();
			Base64InputStream is = new Base64InputStream(in, false); //false to decode
			StringWriter writer = new StringWriter();
			org.apache.commons.io.IOUtils.copy(is, writer, java.nio.charset.Charset.forName("UTF-8"));
			System.out.println(writer.toString());
		}
	}
	 
	@Test
	public void testEncodeDocumentContents() throws IOException {
		Resource contentResource = resourceLoader.getResource("classpath:" + "367 XDR.xml");		
	    DataSource ds = new FileDataSource(contentResource.getFile());
	    DataHandler dh = new DataHandler(ds);
	    
	    InputStream in = dh.getDataSource().getInputStream();
		Base64InputStream is = new Base64InputStream(in, false); //false to decode
		StringWriter writer = new StringWriter();
		org.apache.commons.io.IOUtils.copy(is, writer, java.nio.charset.Charset.forName("UTF-8"));
		System.out.println(writer.toString());
	}
	
	@Test
	public void testAddDocumentContents() throws IOException {
		RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
		RegistryResponseType registryResponse = new RegistryResponseType();
		registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
		response.setRegistryResponse(registryResponse);
		
		DocumentResponse docResponse = new DocumentResponse();
		docResponse.setRepositoryUniqueId("1.21.1.1");
		docResponse.setHomeCommunityId("2.2.2.2.2");
		docResponse.setDocumentUniqueId("3.3.3.3.3");
		docResponse.setMimeType("text/xml");
		
		Resource documentResource = resourceLoader.getResource("classpath:patients.xml");		
		byte[] buffer = new byte[(int)documentResource.contentLength()];
		IOUtils.readFully(documentResource.getInputStream(), buffer);
		String fileContents = new String(buffer);
		
		DataHandler dh = new DataHandler(fileContents, docResponse.getMimeType());
		docResponse.setDocument(dh);
		response.getDocumentResponse().add(docResponse);
		
		assertNotNull(response.getDocumentResponse());
		
		DataHandler dataHandler = docResponse.getDocument();
		InputStream in = null;
		try {
			in = dataHandler.getInputStream();
			//TODO confirm if the size is in bytes and is it guaranteed we will have it?
			long byteCount = new Long(documentResource.contentLength()).longValue();
			byte[] dataBytes = new byte[(int)byteCount];
			IOUtils.readFully(in, dataBytes);
			assertNotNull(dataBytes);
			String dataStr = new String(dataBytes);
			assertNotNull(dataStr);
			assertTrue(dataStr.length() > 0);
			System.out.println(dataStr);
		} catch(IOException e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				if(in != null) { in.close(); }
			} catch(Exception ignore) {}
		}
	}
}
