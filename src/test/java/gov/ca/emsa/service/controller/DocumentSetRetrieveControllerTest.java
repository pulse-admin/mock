package gov.ca.emsa.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.io.Resources;
import com.sun.istack.ByteArrayDataSource;

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
		
		Resource documentResource = resourceLoader.getResource("classpath:367 XDR.xml");	
		String docStr = Resources.toString(documentResource.getURL(), Charset.forName("UTF-8"));
		String binaryDocStr = base64EncodeMessage(docStr);
		System.out.println(binaryDocStr);
		DataSource ds = new ByteArrayDataSource(binaryDocStr.getBytes(), "text/xml; charset=UTF-8");
	    DataHandler dh = new DataHandler(ds);
		docResponse.setDocument(dh);
		response.getDocumentResponse().add(docResponse);
		
		assertNotNull(response.getDocumentResponse());
		
		DataHandler dataHandler = docResponse.getDocument();
		InputStream in = null;
		try {
			in = dataHandler.getDataSource().getInputStream();
			StringWriter writer = new StringWriter();
			org.apache.commons.io.IOUtils.copy(in, writer, Charset.forName("UTF-8"));
			String dataStr = base64DecodeMessage(writer.toString());
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
	
	  public static String base64EncodeMessage(String rawMessage){
	        
	        byte[] bytes = rawMessage.getBytes(Charset.forName("UTF-8"));
	        String encoded = Base64.getEncoder().encodeToString(bytes);
	        return encoded;
	        
	    }
	    
	    public static String base64DecodeMessage(String encodedMessage){
	        
	        byte[] decoded = Base64.getDecoder().decode(encodedMessage);
	        String decodedMessage = new String(decoded, Charset.forName("UTF-8"));
	        return decodedMessage;
	    }
}
