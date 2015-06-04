package riley.web.rest;

public class Form {

	private String id;
	private String name;
	private String address;
	private String state;
	private String zipCode;
	private String emailAddress;
	private String approval;
	
	public void setId(String id) {this.id = id;}
	
	public void setName(String name) {this.name = name;}
	
	public void setAddress(String address) {this.address = address;}
	
	public void setZipCode(String zipCode) {this.zipCode = zipCode;}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public void setState(String state) {this.state = state;}
	
	public void setApproval(String approval) {this.approval = approval;}
	
	public String getId() {return this.id;}
	
	public String getName() {return this.name;}
	
	public String getAddress() {return this.address;}
	
	public String getZipCode() {return this.zipCode;}
	
	public String getEmailAddress() {return this.emailAddress;}
	
	public String getState() {return this.state;}
	
	public String getApproval() {return this.approval;}
}
