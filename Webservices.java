package riley.web.rest;
 
import javax.ejb.Stateless;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
 
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
    @Path("/check-address")
    public Response checkAddress(){
        return Response.ok().build();
    }
 
    //Expects a JSON object document representing a user. We go to some
    //as-yet-unspecified web service to check whether this person has been approved.
    //If they're approved, we write their information to the Parse database. If not,
    //we write them a sad email.
    @GET
    @Path("/approve")
    public Response checkApproval(){
    	return Response.ok().build();
    }
    
    //Writes an email given all pertinent information. Look up the JavaMail documentation
    public void sendEmail(){
    	//your code here
    }
    
    //Writes user to parse database.
    public void postUser(){
    	
    }
 
}
