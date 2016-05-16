package gov.ca.emsa.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Organization {
	
	private String name;
	private Long id;
	
	public Organization(){}
	
	public Organization(String name){
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	
	@XmlElement
	public void setId(Long id) {
		this.id = id;
	}
	
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getName(){
		return name;
	}

}