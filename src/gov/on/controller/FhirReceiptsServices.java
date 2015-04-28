/**
 * 
 */
package gov.on.controller;

import gov.on.businesslogic.ProcessFhirReceipt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * @author pankil
 * Accepts FHIR Immunization Receipt and saves it in to the database (currently there is no validation)
 */
@Path("fhir")
public class FhirReceiptsServices {
	
	/**
	 * Given Immunization Receipt in FHIR Immunization Receipt JSON try to store it in database
	 * @param data the immunization receipt
	 * @return operation result
	 */
	@POST
	@Path("receipt")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addReceipt(InputStream data) {
		
		System.out.println("Add FHIR receipt invoked");
		int status = 201;
		StringBuilder builder = new StringBuilder();
		boolean parsingSuccess = true;
		String output = "{\"status\":\"fail\"}";
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = null;
			while ((line = in.readLine()) != null){
				builder.append(line);
			}
		}
		catch (Exception e) {
			System.out.println("Error Parsing the request");
			parsingSuccess = false;
			status = 500;
		}
		
		
		if (parsingSuccess) 
		{
			// call model to store the file
			ProcessFhirReceipt receiptsProcesser = new ProcessFhirReceipt();
			output = receiptsProcesser.saveReceipt(builder.toString());
			
		}
		
		return Response.status(status).entity(output).build();
	}
	
	
	/**
	 * Returns the record that matches the id
	 * @param id - the database document id, not a business id
	 * @return the record that matches this id
	 */
	@GET
	@Path("receipt/{id}")
	@Produces("application/json")
	public String recordByID(@PathParam("id") String id){
		ProcessFhirReceipt receiptsProcesser = new ProcessFhirReceipt();
		
		return receiptsProcesser.searchID(id);
	}
}
