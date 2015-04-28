/**
 * 
 */
package gov.on.businesslogic;

import gov.on.model.DBassistant;
import gov.on.model.FHIR2ReceiptsDBClientMgr;

import com.cloudant.client.api.Database;

/**
 * @author pankil
 *
 */
public class ProcessFhirReceipt {

	public String saveReceipt(String input) {
		
		String output = "{\"status\":\"error\"}";
		Database db = null;
		try {
			db = getFhirReceiptsDB();
			if (validReceipt(input)){
		        output = DBassistant.saveStringAsJSON(db, input);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
		return output;	
	}

	
	/**
	 * TODO add logic
	 * @param input
	 * @return
	 */
	private boolean validReceipt(String input) {
		boolean isValid = true;
		
		//TODO logic
		
		return isValid;
	}
	
	/**
	 * Exprimental document id search
	 * @param id
	 * @return
	 */
	public String searchID(String id) {
		String filter = "id:"+id;
		String desingDoc = "searchDD";
		String indexName = "dbid";
		return DBassistant.searchDB(getFhirReceiptsDB(), desingDoc, indexName, filter);
	}


	private Database getFhirReceiptsDB()
	{
		return FHIR2ReceiptsDBClientMgr.getDB();
	}
}
