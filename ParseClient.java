package riley.web.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.lang.Exception;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.Response;

public class ParseClient {
	private static final String APP_ID = "zo2uyGbQgBecbaQOR8MjhGDf3DjJHFds380Lleah";
	private static final String REST_KEY = "7VBynMHcow9zJWWzT0pfu0cjF2h1cDfWYpl71JgH";
	private static final String PARSE_API_ROOT_URL = "https://api.parse.com/1";

	public static Response sendGet(String resourceExtension)  throws MalformedURLException, ProtocolException, IOException {
		
		HttpsURLConnection parseDB = openConnection(resourceExtension, "GET");
				
		//Send request and get response code
		int responseCode = parseDB.getResponseCode();
		
		//TODO Add conditional blocks for possible response codes
		
		String responseBody = getResponseBody(parseDB);
		return Response.status(responseCode).entity(responseBody).build();
	}
	
	public static Response sendPost(String resourceExtension, String requestBody){
		
		try {
			HttpsURLConnection parseDB = openConnection(resourceExtension, "POST");		
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
	
	public static Response sendPut(String resourceExtension, String requestBody) {
		
		try {
			HttpsURLConnection parseDB = openConnection(resourceExtension, "PUT");		
			OutputStream output = parseDB.getOutputStream();
			output.write(requestBody.getBytes(StandardCharsets.UTF_8.name()));
			int responseCode = parseDB.getResponseCode();
			System.out.println(responseCode);
			System.out.println(resourceExtension);
			String responseBody = getResponseBody(parseDB);
			return Response.status(responseCode).entity(responseBody).build();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	//Retrieves the body of the HTTP Response and returns it as a string.
	private static String getResponseBody(HttpsURLConnection dbConnect) throws IOException {
		BufferedReader dbResponse = new BufferedReader(
				new InputStreamReader(dbConnect.getInputStream()));
		StringBuilder responseContent = new StringBuilder();
		String line;
		while((line = dbResponse.readLine()) != null)
			responseContent.append(line + "\n");
		dbResponse.close();
		return responseContent.toString();
	}
	
	public static HttpsURLConnection openConnection(String resourceExtension, String method) throws MalformedURLException, ProtocolException, IOException {
		//Establish connection with parse
		URL resourceURL = new URL(PARSE_API_ROOT_URL + resourceExtension);
		HttpsURLConnection parseDB = (HttpsURLConnection) resourceURL.openConnection();
		
		//Add method and headers to request
		if(method != "GET") {
			parseDB.setDoOutput(true);
			parseDB.setRequestProperty("Content-Type", "application/json");
		}
		parseDB.setRequestMethod(method);
		parseDB.setRequestProperty("X-Parse-Application-Id", APP_ID);
		parseDB.setRequestProperty("X-Parse-REST-API-Key", REST_KEY);
			
		
		return parseDB;
	}
}
