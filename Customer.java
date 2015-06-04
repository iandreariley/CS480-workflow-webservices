package riley.web.rest;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Customer")
public class Customer {
	
	private String id;
	private String name;
	private String address;
	private String state;
	private String zipCode;
	private String emailAddress;
	
	public void setId(String id) {this.id = id;}
	
	public void setName(String name) {this.name = name;}
	
	public void setAddress(String address) {this.address = address;}
	
	public void setState(String state) {this.state = state;}
	
	public void setZipCode(String zipCode) {this.zipCode = zipCode;}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getId() {return this.id;}
	
	public String getName() {return this.name;}
	
	public String getAddress() {return this.address;}
	
	public String getState() {return this.state;}
	
	public String getZipCode() {return this.zipCode;}
	
	public String getEmailAddress() {return this.emailAddress;}
}
