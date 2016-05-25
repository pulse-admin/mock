package gov.ca.emsa.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Organization {
	
	private String name;
	private Long organizationId;
	private boolean isActive;
	private String adapter;
	private String ipAddress;
	private String username;
	private String password;
	private String certificationKey;
	private String endpointUrl;
	
	public Organization(){}
	
	public Organization(String name){
		this.name = name;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	@XmlElement
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAdapter() {
		return adapter;
	}
	
	@XmlElement
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	
	@XmlElement
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUsername() {
		return username;
	}
	
	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getCertificationKey() {
		return certificationKey;
	}
	
	@XmlElement
	public void setCertificationKey(String certificationKey) {
		this.certificationKey = certificationKey;
	}

	public Long getOrganizationId() {
		return organizationId;
	}
	
	@XmlElement
	public void setOrganizationId(Long id) {
		this.organizationId = id;
	}
	
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getName(){
		return name;
	}
	
	public String getEndpointUrl() {
		return endpointUrl;
	}
	
	@XmlElement
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	
}