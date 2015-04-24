package gov.on.businesslogic;

import gov.on.model.DBassistant;
import gov.on.model.ReceiptsDBCloudantClientMgr;

import com.cloudant.client.api.Database;


/**
 * The purpose of this class is to process a receipt and related receipt requests.
 * Example:
 * 	saving a receipt to the database
 * 
 * @author pankil
 *
 */
public class ProcessReceipts {

	/**
	 * Given an input it is saved as a receipt in the database
	 * @param input - the string to process
	 * @return status of the save action and an id if applicable
	 */
	public String saveReceipt(String input) {
		String output = "";
		Database db = null;
		try {
			db = getReceiptsDB();			
	        output = DBassistant.saveStringAsJSON(db, input);
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		return output;	
	}
	
	
	/**
	 * This is an experimental method that is still being worked on/defined.
	 * @return
	 */
	/*
	public String getClients() {
		Database db = null;
		String output = "get clients";
		
		try {
			db = getReceiptsDB();
			//output = db.view("_all_docs").toString();
			//DBassistant.searchDB(db,"demoview", "name", "id:test");
			output = DBassistant.searchDB(db, "demoview", "name", "id:\"202809406834\"");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		return output;
	}
	*/
	
	/*
	public String searchClients(){
		Database db = null;
		String output = "search clients";
		
		try {
			db = getReceiptsDB();
			DBassistant.searchDB(db, "demoview", "name", "name:test");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		return output;
	}
	*/
	
	private Database getReceiptsDB()
	{
		return ReceiptsDBCloudantClientMgr.getDB();
	}
	
}
