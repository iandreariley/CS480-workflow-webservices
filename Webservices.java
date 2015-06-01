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
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/")
@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Stateless
public class Webservices {

    @GET
    @Path("/{class}")
    public Response retrieve(@PathParam("class") String objClass, @PathParam("id") String objId) throws MalformedURLException, IOException{
    	String urlExtension = "/classes/" + objClass;
    	System.out.println("HERE I AM!");
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
    
    @POST
    @Path("/{class}")
    public Response create(String body, @PathParam("class") String objClass) {
    	String urlExtension = "/classes/" + objClass;
    	return ParseClient.sendPost(urlExtension, body);
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
    
    @GET
    @Path("/check/customer/state/{id: [a-zA-Z0-9]+}")
    public Response checkState(@PathParam("id") String objId) throws Exception {
    	String resourceExtension = "/classes/Customer/" + objId;
    	String json = "{\"inState\":";
    	if(ParseClient.checkState(resourceExtension))
    		json += "\"true\"}";
    	else
    		json += "\"false\"}";
    	return Response.ok().entity(json).build();
    }
    
    @GET
    @Path("/check/customer/approval/{id: [a-zA-Z0-9]+}")
    public Response checkApproval(@PathParam("id") String objId) throws Exception {
    	String resourceExtension = "/classes/Customer/" + objId;
    	String approval = ParseClient.checkApproval(resourceExtension);
    	String json = "{\"approval\":\"" + approval + "\"}";
    	return Response.ok().entity(json).build();
    }
    
    @POST
    @Path("/send/customer/email/{id: [a-zA-Z0-9]+}")
    public Response sendEmail(String body, @PathParam("id") String objId) throws Exception {
    	

        
        String hostname = "smtp.gmail.com";
        int port = 587;
        String username = "cs480webservices@gmail.com";
        String password = "easypassword1";
        String from = "cs480webservices@gmail.com";
        Mailer mailer = new Mailer(port,hostname,from,username,password);
        String to = "idriley@cpp.edu";
        
    	String resourceExtension = "/classes/Customer/" + objId;
    	Customer customer = ParseClient.getCustomer(resourceExtension);
    	String emailAddress = customer.getEmailAddress();
    	String emailBody = getEmailBodyFromJson(body);
    	mailer.sendEmail(emailAddress, "Booyah!", emailBody);
    	return Response.ok().build();
    }
    
    private String getEmailBodyFromJson(String json) {
    	System.out.println("EMAIL JSON:\n" + json);
    	int begin = json.indexOf(':');
    	begin = json.indexOf('"', begin) + 1;
    	int end = json.indexOf('}') - 1;
    	return json.substring(begin, end);
    }
    
    //Writes an email given all pertinent information. Look up the JavaMail documentation
    public void sendEmail(){
    	//your code here
    }
    
    //Writes user to parse database.
    public void postUser(){
    	
    }
 
}
