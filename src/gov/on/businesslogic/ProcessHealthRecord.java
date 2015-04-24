/**
 * 
 */
package gov.on.businesslogic;

import org.json.JSONObject;

import gov.on.model.DBassistant;
import gov.on.model.RecordsFHIR1DBClientMgr;

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

	public String hcnSearchHR(String hcn, String dataType) {
		String output = "{\"status\":\"error\"}";
		if (dataType.compareTo("fhir1") == 0){
			output = fhir1hcnSearchHR(hcn);
		}
		else if (dataType.compareTo("fhir2") == 0){
			output = fhir2hcnSearchHR(hcn);
		}
		else {
			System.out.println("nothing to search");
		}
		
		return output;
	}
	
	private String fhir2hcnSearchHR(String hcn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public String fhir1hcnSearchHR(String input) {
		/*
		 * Use HCN to search on cloudants index
		 * Return the full document
		 */
		String output = "";
		JSONObject jsonObj = null;
		String searchString = input;
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

}
