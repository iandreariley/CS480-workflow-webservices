package riley.web.rest;
 
import javax.ejb.Stateless;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.PropertyException;

import org.eclipse.persistence.exceptions.JAXBException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

@Path("/")
@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Stateless
public class Webservices {
	
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final int EMAIL_PORT = 587;
    private static final String EMAIL_USERNAME = "cs480webservices@gmail.com";
    private static final String EMAIL_PASSWORD = "easypassword1";
    
    /*
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
    
    /*
     * CONVENIENCE METHODS:
     * The methods defined below aren't intended to be used as a part of this web service.
     * Rather they are only for use in debugging.
     */

    @GET
    @Path("/{class}")
    public Response retrieve(@PathParam("class") String objClass, @PathParam("id") String objId) throws MalformedURLException, IOException{
    	String urlExtension = "/classes/" + objClass;
     	return ParseClient.sendGet(urlExtension);
    }
    
    @GET
    @Path("/{class: [a-zA-Z0-9]+}/{id: [a-zA-Z0-9]+}")
    public Response retrieveOne(@PathParam("class") String objClass, @PathParam("id") String objId) throws MalformedURLException, IOException{
    	String urlExtension = "/classes/" + objClass + "/" + objId;
     	return ParseClient.sendGet(urlExtension);
    }
    
    @GET
    public Response test() {
    	return Response.ok().build();
    }
    
    @PUT
    @Path("/{class}/{id: [a-zA-Z0-9]+}")
    public Response update(String body, @PathParam("class") String objClass, @PathParam("id") String objId) {
    	String urlExtension = "/classes/" + objClass + "/" + objId;
    	return ParseClient.sendPut(urlExtension, body);
    }
    
    @DELETE
    @Path("/{class}/{id: [a-zA-Z0-9]+}")
    public Response delete(@PathParam("class") String objClass, @PathParam("id") String objId) throws MalformedURLException, IOException {
    	String urlExtension = "/classes/" + objClass + "/" + objId;
     	return ParseClient.sendDelete(urlExtension);
    }
    
//  @GET
//  @Path("/check/customer/approval/{id: [a-zA-Z0-9]+}")
//  public Response checkApproval(@PathParam("id") String objId) throws Exception {
//  	String resourceExtension = "/classes/Customer/" + objId;
//  	String approval = ParseClient.checkApproval(resourceExtension);
//  	String json = "{\"approval\":\"" + approval + "\"}";
//  	return Response.ok().entity(json).build();
//  }
    
//  @POST
//  @Path("/{class}")
//  public Response create(String body, @PathParam("class") String objClass) {
//  	String urlExtension = "/classes/" + objClass;
//  	return ParseClient.sendPost(urlExtension, body);
//  }
 
}
