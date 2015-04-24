/**
 * 
 */
package gov.on.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


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
	
	/**
	 * Given some search filters in the POST body return matching health record
	 * @return the matching health record
	 */
	@POST
	@Path("query/hcn")
	@Consumes("application/json")
	@Produces("application/json")
	public String healthRecordBySearch(){
		String results = "";
		
		boolean valid = true;
		
		// if the request is valid, search
		if (valid) {
			results = "found";
		}
		
		return results;
	}
	
	
	/**
	 * Lists the health records in the database
	 * @return
	 */
	@GET
	@Path("/")
	@Produces("application/json")
	public String healthRecordsList(){
		String result = "list";
		
		return result;
	}
}
