/**
 * 
 */
package gov.on.businesslogic;

import gov.on.model.DBassistant;
import gov.on.model.RecordsDBCloudantClientMgr;

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
			db = getHealthRecordsDB();			
	        output = DBassistant.saveStringAsJSON(db, input);
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		return output;	
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public String hcnSearchHR(String input) {
		/*
		 * Use HCN to search on cloudants index
		 * Return the full document
		 */
		String output = "";
		String searchString = "";
		Database db = null;
		
		try {
			db = getHealthRecordsDB();			
	        //output = DBassistant.searchDB(db, field, index);
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		
		return output;	
	}
	
	
	private Database getHealthRecordsDB()
	{
		return RecordsDBCloudantClientMgr.getDB();
	}
	
}
