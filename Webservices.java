package riley.web.rest;
 
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.PropertyException;

@Path("/")
@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Stateless
public class Webservices {
	
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final int EMAIL_PORT = 587;
    private static final String EMAIL_USERNAME = "cs480webservices@gmail.com";
    private static final String EMAIL_PASSWORD = "easypassword1";
    
    /**
     * This method expects a valid json string in the HTTP request body that
     * contains a field labeled "state". If the value of this field is
     * California, the HTTP response contains the json string:
     * {
     *   "inState":"true"
     * }
     * Otherwise the value of inState is false
     */
    @POST
    @Path("/check/state")
    public Response checkState(String body) throws Exception {
    	Customer customer = (Customer) ParseClient.jsonToObject(body, "Customer", Customer.class);
    	String state = customer.getState().toLowerCase();
    	String json = "{\"inState\":";
    	if(state.equals("ca") || state.equals("california"))
    		json += "\"true\"}";
    	else
    		json += "\"false\"}";
    	return Response.ok().entity(json).build();
    }
    
    /**
     * This method checks the approval field of the json object passed in
     * the request body. The response generated by this method has a body
     * that is a json string of the form 
     * {
     *   "approval":"<value>"
     * }
     * where <value> is one of {pending, approved, rejected}
     * @param body is body of the http request. It must be valid json with
     * a field labeled "approval"
     * @return an http response.
     */
    @POST
    @Path("/check/approval")
    public Response checkApproval(String body) throws Exception {
    	Form form = (Form) ParseClient.jsonToObject(body, "Form", Form.class);
    	String approve = form.getApproval();
    	String json = "{\"approval\":";
    	if(approve.equals("p"))
    		json += "\"pending\"}";
    	else if(approve.equals("a"))
    		json += "\"approved\"}";
    	else
    		json += "\"rejected\"}";
    	return Response.ok().entity(json).build();
    	
    }
    
    /**
     * This method sends an email to the email address found in the json
     * string passed in as the body of the http request. If the email is
     * sent successfully, it response with an empty 250 response.
     * @ param body is the body of the http request. It must have a field
     * labeled "form" that is a nested json object with a field labeled
     * "emailAddress"
     */
    @POST
    @Path("/send/email")
    public Response sendEmail(String body) {
        Mailer mailer = new Mailer(EMAIL_PORT, EMAIL_HOST, EMAIL_USERNAME,
        		EMAIL_USERNAME, EMAIL_PASSWORD);
        try {
	        Email email = (Email) ParseClient.jsonToObject(body, "Email", Email.class);
	        String emailAddress = email.getForm().getEmailAddress();
	        String subject = email.getSubject();
	        String message = email.getMessage();
	        mailer.sendEmail(emailAddress, subject, message);
	        return Response.status(250).build();
        } catch (Exception jbe) {
        	jbe.printStackTrace();
        	return Response.status(400).entity("Request body is either not valid json" +
        	"or is missing required fields.").build();
        }
    }
    
    /**
     * This method takes form data from a "Customer" form submitted by a 
     * User, extracts the necessary Customer information, and posts a
     * corresponding Customer ojbect to Parse.
     * @ param body is the body of the http request. It must be valid
     * json.
     */
    @POST
    @Path("/Customer")
    public Response postCustomer(String body) {
    	try {
	    	return ParseClient.postCustomer(body);
    	} catch(PropertyException pe) {
			pe.printStackTrace();
			return Response.status(500).entity("Wuh oh, bad news.").build();
		} catch(Exception ioe) {
			ioe.printStackTrace();
	    	return Response.status(400).entity("Bad JSON.").build();
	    }
    }
}
