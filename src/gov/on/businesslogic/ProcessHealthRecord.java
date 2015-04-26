/**
 * 
 */
package gov.on.businesslogic;

import org.json.JSONObject;

import gov.on.model.DBassistant;
import gov.on.model.RecordsFHIR1DBClientMgr;
import gov.on.model.RecordsFHIR2DBClientMgr;

import com.cloudant.client.api.Database;

/**
 * @author pankil
 *
 */
public class ProcessHealthRecord {

	/**
	 * 
	 * @param input
	 * @return
	 */
	public String saveHR(String input) {
		String output = "";
		Database db = null;
		try {
			db = getFHIR1HealthRecordsDB();			
	        output = DBassistant.saveStringAsJSON(db, input);
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		return output;	
	}

	/**
	 * Searched for health record for the given HCN the search is performed in the specified FHIR database 
	 * @param hcn the health card number
	 * @param fhir the fhir version (fhir1 or fhir2)
	 * @return
	 */
	public String hcnSearchHR(String hcn, String fhir) {
		String output = "{\"status\":\"error\"}";
		if (fhir.compareTo("fhir1") == 0){
			output = fhir1hcnSearchHR(hcn);
		}
		else if (fhir.compareTo("fhir2") == 0){
			output = fhir2hcnSearchHR(hcn);
		}
		else {
			System.out.println("nothing to search");
		}
		
		return output;
	}
	
	/**
	 * Health Card Number specific search returns FHIR DSTU 2
	 * @param hcn
	 * @return
	 */
	private String fhir2hcnSearchHR(String hcn) {
		/*
		 * Use HCN to search on cloudants index
		 * Return the full document
		 */
		String output = "";
		JSONObject jsonObj = null;
		String searchString = hcn;
		String indexName = "search";
		String designDoc = "searchDD";
		Database db = null;
		
		try {
			db = getFHIR2HealthRecordsDB();
			// searchDB - the design document name, the index name, the index query inputs
			String indexTosearch = designDoc+"/"+indexName;
			jsonObj = DBassistant.queryForJSONobject(db, indexTosearch, searchString);
			output = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONObject("doc").getJSONObject("map").toString();
			
			//try to reverse the order of JSON elements - this is just for viewing purposes
			JSONObject jo = new JSONObject(output);
			output = jo.toString();
			
			System.out.println("output: " + output);
		
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		
		return output;	
	}

	/**
	 * HCN specific search returns FHIR DSTU 1
	 * @param input
	 * @return
	 */
	public String fhir1hcnSearchHR(String hcn) {
		/*
		 * Use HCN to search on cloudants index
		 * Return the full document
		 */
		String output = "";
		JSONObject jsonObj = null;
		String searchString = hcn;
		String indexName = "search";
		String designDoc = "searchDD";
		Database db = null;
		
		try {
			db = getFHIR1HealthRecordsDB();
			// searchDB - the design document name, the index name, the index query inputs
			String indexTosearch = designDoc+"/"+indexName;
			jsonObj = DBassistant.queryForJSONobject(db, indexTosearch, searchString);
			output = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONObject("doc").getJSONObject("map").toString();
			System.out.println("output: " + output);
		
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		
		return output;	
	}
	
	
	private Database getFHIR1HealthRecordsDB()
	{
		return RecordsFHIR1DBClientMgr.getDB();
	}

	private Database getFHIR2HealthRecordsDB()
	{
		return RecordsFHIR2DBClientMgr.getDB();
	}
	
}
