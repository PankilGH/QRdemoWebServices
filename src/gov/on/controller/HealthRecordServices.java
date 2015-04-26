/**
 * 
 */
package gov.on.controller;

import gov.on.businesslogic.ProcessHealthRecord;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 
 * @author pankil
 *
 */
@Path("api/healthrecords")
public class HealthRecordServices {

	/**
	 * Returns the health record that matches the id
	 * @param id - the database document id, not a business id
	 * @return the record that matches this id
	 */
	@GET
	@Path("{id}")
	@Produces("application/json")
	public String healthRecordByID(@PathParam("id") String id){
		
		return id;
	}
	
	
	/*
	 * This is for testing purposes only
	 * 
	 */
	@GET
	@Path("test/query/hcn/fhir1/{num}")
	@Produces("application/json")
	public String fhir1testingOnly(@PathParam("num") String num){
		ProcessHealthRecord phr = new ProcessHealthRecord();
		
		return phr.hcnSearchHR(num, "fhir1");
	}
	
	@GET
	@Path("test/query/hcn/fhir2/{num}")
	@Produces("application/json")
	public String fhir2testingOnly(@PathParam("num") String num){
		ProcessHealthRecord phr = new ProcessHealthRecord();
		
		return phr.hcnSearchHR(num, "fhir2");
	}
	
	
	/**
	 * Given some filters in the POST body return matching health record
	 * @return  the health record that matches filters in POST body
	 */
	@POST
	@Path("query/hcn/")
	@Consumes("application/json")
	@Produces("application/json")
	public String healthRecordBySearch(InputStream data){
		String results = "{\"status\":\"error\"}";
		String req = ControllerUtility.getRequestBodyString(data);
		String hcn = "";
		String fhir = "";
		boolean valid = true;
		try {
			JSONObject jsonObject = new JSONObject(req);
			hcn = jsonObject.getString("hcn");
			fhir = jsonObject.getString("fhir");
			System.out.println(hcn + " - " + fhir);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			valid = false;
		}
		
		// if the request is valid, search
		if (valid) {
			ProcessHealthRecord phr = new ProcessHealthRecord();
			results = phr.hcnSearchHR(hcn, fhir);
		}
		return results;
	}
	
	
	/**
	 * Lists the health records in the database
	 * @return
	 */
	/*
	@GET
	@Path("/")
	@Produces("application/json")
	public String healthRecordsList(){
		String result = "list";
		
		return result;
	}
	*/
}
