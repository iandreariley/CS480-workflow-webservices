package riley.web.rest;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "Email")
public class Email {
	private String subject;
	private String message;
	private Form form;
	
	public void setSubject(String subject) {this.subject = subject;}
	
	public void setMessage(String message) {this.message = message;}
	
	public void setForm(Form form) {this.form = form;}
	
	public String getSubject() {return this.subject;}
	
	public String getMessage() {return this.message;}
	
	public Form getForm() {return this.form;}
}
