package gov.on.model;


import java.util.Set;
import java.util.Map.Entry;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * This code was copied from an IBM sample code that implemented a cloudant client.
 * 
 * The purpose of this code is to connect to a Cloudant database that is on hosted on Bluemix as a service.
 * 
 * @author 
 *
 */
public class ReceiptsDBCloudantClientMgr {
	
	private static CloudantClient cloudant = null;
	private static Database db = null;
	private static String databaseName = "clients"; // the receipts db is named clients in Cloudant
	private static String user = null;
	private static String password = null;
	private static String host = null;


	/**
	 * Added to assist in issue with search using POST
	 * @return the cloudant client - used to execute HTTP post request
	 */
	public static synchronized CloudantClient getCloudantClient(){
		if (cloudant != null){
			return cloudant;
		}else{
			initClient();
			return cloudant;
		}
		
	}
	
	 
	private static void initClient() {
		
		if ( cloudant == null ) {
			synchronized (ReceiptsDBCloudantClientMgr.class) {
				if ( cloudant != null ) {
					return;
				}				
				cloudant = createClient();			
				
			}// end synchronized
		}
	}
    
	private static CloudantClient createClient() {

		// VCAP_SERVICES is a system environment variable
		// Parse it to obtain the  NoSQL DB connection info
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		String serviceName = null;

    	if (VCAP_SERVICES != null) {

			// parse the VCAP JSON structure
			JsonObject obj =  (JsonObject) new JsonParser().parse(VCAP_SERVICES);
			Entry<String, JsonElement> dbEntry = null;
			Set<Entry<String, JsonElement>> entries = obj.entrySet();
			// Look for the VCAP key that holds the cloudant no sql db information
			for (Entry<String, JsonElement> eachEntry : entries) {				
				if (eachEntry.getKey().equals("cloudantNoSQLDB")) {
					dbEntry = eachEntry;
					break;
				}
			}
			if (dbEntry == null) {			
				throw new RuntimeException("Could not find cloudantNoSQLDB key in VCAP_SERVICES env variable");    					
			}

			obj =(JsonObject) ((JsonArray)dbEntry.getValue()).get(0);		
			serviceName = (String)dbEntry.getKey();
			System.out.println("Service Name - "+serviceName);

			obj = (JsonObject) obj.get("credentials");

			user = obj.get("username").getAsString();
			password = obj.get("password").getAsString();
			host = obj.get("host").getAsString();

		}
		else {
			throw new RuntimeException("VCAP_SERVICES not found");
		}

		try {
			return new CloudantClient(user, user, password);
		}
		catch(org.lightcouch.CouchDbException e) {
			throw new RuntimeException("Unable to connect to repository", e);
		}
	}

	
	public static Database getDB() {
		
		if ( cloudant == null ) {
			initClient();
		}
		
		if(db == null)
		{
			try {
				
				db = cloudant.database(databaseName, true);
			}
			catch(Exception e) {

				throw new RuntimeException("DB Not found", e);
			}
		}
		return db;
	}
	
	public static String getUser()
	{
		return user;
	}
	
	public static String getPassword()
	{
		return password;
	}
	
	public static String getHost()
	{
		return host;
	}
	
	public static String getDatabaseName()
	{
		return databaseName;
	}
	
	private ReceiptsDBCloudantClientMgr() {
	    	
	 }
}

