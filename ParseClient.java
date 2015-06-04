package riley.web.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.lang.Exception;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

/**
 * This class is a client meant to interact with a Parse application via the url
 * "https://api.parse.com." The class also provides utility functions to turn
 * json representations of objects found in the parse database into POJOs.
 * for the CS480 workflow project. 
 * @author Ian Riley
 * @author Andrew Pearson
 *
 */
public class ParseClient {
	private static final String APP_ID =
			"zo2uyGbQgBecbaQOR8MjhGDf3DjJHFds380Lleah";
	private static final String REST_KEY =
			"7VBynMHcow9zJWWzT0pfu0cjF2h1cDfWYpl71JgH";
	private static final String PARSE_API_ROOT_URL = "https://api.parse.com/1";
	
	/**
	 * Takes a json representation of some form data that contains fields 
	 * pertaining to a customer object, and posts an object of that type to the
	 * parse app specified above.
	 * @param form is a json string with fields corresponding to parse data
	 * @return the http response from parse
	 */
	public static Response postCustomer(String form) throws JAXBException,
	PropertyException, IOException, MalformedURLException, PropertyException {
		String customerString = FormToCustomer(form);
		Response response = sendPost("/classes/Customer", customerString);
		return response;
	}
	
	/**
	 * Sends a Post request to parse to add an object to the parse app specified
	 * above.
	 * @param resourceExtension is the extension to the PARSE_API_ROOT_URL that
	 * is needed to correctly post to Parse.
	 * @param requestBody is the body of the http request send by the client to
	 * this web service.
	 * @return the http response from Parse.
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public static Response sendPost(String resourceExtension,
	String requestBody) throws MalformedURLException, ProtocolException,
	IOException {
		// Open connection to Parse.com api
		HttpsURLConnection parseDB = openConnection(resourceExtension, "POST");		
		OutputStream output = parseDB.getOutputStream();
		// Write request body to Parse.com resource
		output.write(requestBody.getBytes(StandardCharsets.UTF_8.name()));
		// Return Parse's response.
		int responseCode = parseDB.getResponseCode();
		String responseBody = getResponseBody(parseDB);
		return Response.status(responseCode).entity(responseBody).build();
	}
	
	/**
	 * Takes an https connection and returns body of the reponse to the request
	 * sent.
	 * @param dbConnect a connection to Parse.com with a method already
	 * specified.
	 * @return the body of the response from Parse.com
	 * @throws IOException if getInputStream fails
	 */
	public static String getResponseBody(HttpsURLConnection dbConnect)
	throws IOException {
		// Open Stream to Parse response
		BufferedReader dbResponse = new BufferedReader(
				new InputStreamReader(dbConnect.getInputStream()));
		StringBuilder responseContent = new StringBuilder();
		String line;
		// Read each line and add it to json string, then return it
		while((line = dbResponse.readLine()) != null)
			responseContent.append(line + "\n");
		dbResponse.close();
		return responseContent.toString();
	}
	
	/**
	 * Given a persistence class and a json string, attempts the unmarshal the
	 * string into a POJO.
	 * @param json a string of json representing the object
	 * @param classString the name of the POJO class as a string
	 * @param objClass the class of the POJO class
	 * @return the object represented by the json string
	 * @throws JAXBException if the json is not correctly formatted.
	 */
	public static Object jsonToObject(String json, String classString,
	Class<?> objClass) throws JAXBException {
		// Create a new POJO to populate with json data
		JAXBContext context = JAXBContext.newInstance(objClass);
		Unmarshaller jsonUnmarshaller = context.createUnmarshaller();
		jsonUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE,
				"application/json");
		// Wrap json in proper jaxb format.
		String jaxbString = "{\"" + classString + "\":" + json + "}";
		StreamSource jsonStream =
				new StreamSource(new StringReader(jaxbString));
		// Convert string to POJO, and return it
		return jsonUnmarshaller.unmarshal(jsonStream, objClass).getValue();
	}
	
	/**
	 * Creates a connection to a specific resource in parse.com
	 * @param resourceExtension the extension past the root url of the parse api
	 * for the given resource.
	 * @param method http method. One of {GET, POST, PUT, DELETE} as a string
	 * @return an open connection to parse resource
	 * @throws MalformedURLException if the url is invalid
	 * @throws ProtocolException if the wrong protocol is used
	 * @throws IOException if open connection or setRequestMethod fail
	 */
	private static HttpsURLConnection openConnection(String resourceExtension,
	String method) throws MalformedURLException, ProtocolException,
	IOException {
		//Establish connection with parse
		URL resourceURL = new URL(PARSE_API_ROOT_URL + resourceExtension);
		HttpsURLConnection parseDB =
				(HttpsURLConnection) resourceURL.openConnection();
		//Add method and headers to request
		if(method == "POST" || method == "PUT") {
			parseDB.setDoOutput(true);
			parseDB.setRequestProperty("Content-Type", "application/json");
		}
		parseDB.setRequestMethod(method);
		parseDB.setRequestProperty("X-Parse-Application-Id", APP_ID);
		parseDB.setRequestProperty("X-Parse-REST-API-Key", REST_KEY);
		return parseDB;
	}
	
	/**
	 * Takes in a json string representing a form object and returns a json
	 * string representing a customer object by simply extracting those fields
	 * in the form that pertain to the customer.
	 * @param form json string representing form data
	 * @return json string representing the customer named on the form
	 * @throws JAXBException if json is incorrectly formatted
	 * @throws PropertyException if setProperty fails - may be due to a missing
	 * jaxb.properties file in the same package as this class. Look in 
	 * src/main/resources/riley/web/rest. There should be a file called
	 * jaxb.properties with the following line in it:
	 * javax.xml.bind.context.factory=
	 * org.eclipse.persistence.jaxb.JAXBContextFactory
	 */
	private static String FormToCustomer(String form) throws JAXBException,
	PropertyException {
		// Create customer object from form, which will include only fields
		// that the Customer class and the Form class have in common.
		Customer customer = (Customer) jsonToObject(form, "Customer",
				Customer.class);
		JAXBContext jc = JAXBContext.newInstance(Customer.class);
		// Convert the customer object into a json string.
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE,
				"application/json");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(customer, sw);
		String customerJson = sw.toString();
		// Remove the containing brackets that JAXB uses on all its objects so
		// that the json is formatted correctly for Parse, and return it.
		int startBracket = customerJson.indexOf('{', 1);
		int endBracket = customerJson.lastIndexOf('}',
				customerJson.length() - 2) + 1;
		return customerJson.substring(startBracket, endBracket);
	}
}
