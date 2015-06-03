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

public class ParseClient {
	private static final String APP_ID = "zo2uyGbQgBecbaQOR8MjhGDf3DjJHFds380Lleah";
	private static final String REST_KEY = "7VBynMHcow9zJWWzT0pfu0cjF2h1cDfWYpl71JgH";
	private static final String PARSE_API_ROOT_URL = "https://api.parse.com/1";
	
	/*
	 * Thi
	 * @param resourceExtension the extension from PARSE_API_ROOT_URL that accesses
	 * the desired element
	 * @return http response with json in the body representing object, or json
	 * with an error message if object wasn't found
	 */
	public static Response sendGet(String resourceExtension)  throws MalformedURLException, ProtocolException, IOException{
		
		HttpsURLConnection parseDB = openConnection(resourceExtension, "GET");
		//Send request and get response code
		int responseCode = parseDB.getResponseCode();
		//TODO Add conditional blocks for possible response codes
		String responseBody = getResponseBody(parseDB);
		return Response.status(responseCode).entity(responseBody).build();
	}
	
	public static Response postCustomer(String form) throws JAXBException, PropertyException, IOException, MalformedURLException, PropertyException {
		String customerString = FormToCustomer(form);
		Response response = sendPost("/classes/Customer", customerString);
		return response;
	}
	
	public static Response sendPost(String resourceExtension, String requestBody) throws MalformedURLException, ProtocolException, IOException {
		HttpsURLConnection parseDB = openConnection(resourceExtension, "POST");		
		OutputStream output = parseDB.getOutputStream();
		output.write(requestBody.getBytes(StandardCharsets.UTF_8.name()));
		int responseCode = parseDB.getResponseCode();
		String responseBody = getResponseBody(parseDB);
		return Response.status(responseCode).entity(responseBody).build();
	}
	
	public static Response sendPut(String resourceExtension, String requestBody) {
		
		try {
			HttpsURLConnection parseDB = openConnection(resourceExtension, "PUT");		
			OutputStream output = parseDB.getOutputStream();
			output.write(requestBody.getBytes(StandardCharsets.UTF_8.name()));
			int responseCode = parseDB.getResponseCode();
			String responseBody = getResponseBody(parseDB);
			return Response.status(responseCode).entity(responseBody).build();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	public static Response sendDelete(String resourceExtension) throws IOException, MalformedURLException, ProtocolException {
		HttpsURLConnection parseDb = openConnection(resourceExtension, "DELETE");
		
		int responseCode = parseDb.getResponseCode();
		
		String responseBody = getResponseBody(parseDb);
		return Response.status(responseCode).entity(responseBody).build();
	}
	
	//Retrieves the body of the HTTP Response and returns it as a string.
	public static String getResponseBody(HttpsURLConnection dbConnect) throws IOException {
		BufferedReader dbResponse = new BufferedReader(
				new InputStreamReader(dbConnect.getInputStream()));
		StringBuilder responseContent = new StringBuilder();
		String line;
		while((line = dbResponse.readLine()) != null)
			responseContent.append(line + "\n");
		dbResponse.close();
		return responseContent.toString();
	}
	
	public static Customer getCustomer(String resourceExtension) throws MalformedURLException, ProtocolException, IOException, JAXBException {
		HttpsURLConnection parseDB = openConnection(resourceExtension, "GET");
		String responseBody = getResponseBody(parseDB);
		return (Customer) jsonToObject(responseBody, "Customer", Customer.class);
	}
	
	public static Object jsonToObject(String json, String classString, Class<?> objClass) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(objClass);
		Unmarshaller jsonUnmarshaller = context.createUnmarshaller();
		jsonUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
		//Wrap json in proper jaxb format.
		String jaxbString = "{\"" + classString + "\":" + json + "}";
		StreamSource jsonStream = new StreamSource(new StringReader(jaxbString));
		return jsonUnmarshaller.unmarshal(jsonStream, objClass).getValue();
	}
	
	public static HttpsURLConnection openConnection(String resourceExtension, String method) throws MalformedURLException, ProtocolException, IOException {
		//Establish connection with parse
		URL resourceURL = new URL(PARSE_API_ROOT_URL + resourceExtension);
		HttpsURLConnection parseDB = (HttpsURLConnection) resourceURL.openConnection();
		
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
	
	private static String FormToCustomer(String form) throws JAXBException, PropertyException {
		Customer customer = (Customer) jsonToObject(form, "Customer", Customer.class);
		JAXBContext jc = JAXBContext.newInstance(Customer.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(customer, sw);
		String customerJson = sw.toString();
		int startBracket = customerJson.indexOf('{', 1);
		int endBracket = customerJson.lastIndexOf('}', customerJson.length() - 2) + 1;
		return customerJson.substring(startBracket, endBracket);
	}
}
