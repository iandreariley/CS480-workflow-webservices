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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.MalformedURLException;
 
@Path("/")
@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Stateless
public class Webservices {
 
	//Expects a JSON string as the body of the http request. From that
	//document it looks at the value of a field called 'state' and returns
	//true if that state is either 'ca' or 'california', ignoring case. Returns
	//false otherwise. Should return a 4xx error of some sort if there is no
	//state field.
    @GET
    @Path("{check-address}")
    public Response checkAddress(@PathParam("check-address") String check_address){
    	if(check_address.toLowerCase().contains("ca")||check_address.toLowerCase().contains("california"))
    		return Response.ok().build();
    	else
    		return Response.status(403).build();
    }
 
    //Expects a JSON object document representing a user. We go to some
    //as-yet-unspecified web service to check whether this person has been approved.
    //If they're approved, we write their information to the Parse database. If not,
    //we write them a sad email.
    @GET
    @Path("/{class}/{id: [a-zA-Z0-9]*}")
    public Response retrieve(@PathParam("class") String objClass, @PathParam("id") String objId) throws MalformedURLException, IOException{
    	String urlExtension = "/classes/" + objClass + "/" + objId;
     	return ParseClient.sendGet(urlExtension);
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
    
    //Writes an email given all pertinent information. Look up the JavaMail documentation
    public void sendEmail(){
    	//your code here
    }
    
    //Writes user to parse database.
    public void postUser(){
    	
    }
 
}
