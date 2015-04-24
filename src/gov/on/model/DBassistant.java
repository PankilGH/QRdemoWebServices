package gov.on.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.lightcouch.internal.URIBuilder;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;



// I found I was going to be copy pasting a lot of code to perform similar tasks
// This "utility" class was created to help prevent duplicating code/logic
/**
 * Used to execute common tasks on the database
 * @author pankil
 *
 */
public class DBassistant {
	
	/**
	 * Given a string input converts it to a JSON object and saves it in the provided db
	 * 
	 * @param db the database
	 * @param input the string input
	 * @return the result of database operation
	 */
	public static String saveStringAsJSON(Database db, String input) {
		String output = "[{\"status\":\"success\"}";
		try {
			
	        JSONObject jsonObject = new JSONObject(input);
	        
			Response resp = db.save(jsonObject);
	        
			System.out.println("DB response: " + resp.toString());
			
			output += ", { \"id\" :\"" + resp.getId() + "\"}]";
			
		}
		catch (Exception e){
			System.out.println(e);
			output = "{\"status\":\"error\"}";
		}
		return output;	
	}

	/*
	public static String searchDB(Database db, String designDocName, String indexName, String filter) {
		
		System.out.println("db: " + db + " - " + "index: " + indexName + " - " + "key:value: " + filter + " - ");
		
		String index = designDocName+"/"+indexName;
		Search search = db.search(index); // the index name and location
		//queryForStream(filter,db,index);
		//InputStream data = search.includeDocs(true).queryForStream(filter); //the search value key:value
		String data = queryForStream(filter,db,index);
		
		
		//
		return data;
	}
	*/
	
	
	// This code needs some clean up
	/**
	 * When I send a number in HTTP GET it gets read as an integer and search doesn't match with
	 * string value of the same number.
	 * 
	 * This function was created to search using HTTP POST since the cloudant clinet
	 * that I am using can't perform HTTP POST search.
	 * 
	 * 
	 * @param query
	 * @param db
	 * @param index
	 * @return
	 */
	public static JSONObject queryForJSONobject(Database db, String indexTosearch, String query) {
		String searchPath = indexTosearch;
		JSONObject jsonObject = null;
		
		
		if(indexTosearch.contains("/")) {
				String[] v = indexTosearch.split("/");
				searchPath = String.format("_design/%s/_search/%s", v[0], v[1]);
		}
		URIBuilder uriBuilder = URIBuilder.buildUri(db.getDBUri()).path(searchPath);
		URI uri = uriBuilder.build();
		
		HttpPost post = new HttpPost(uri);		
		post.addHeader("Accept", "application/json");
		try {
			String queryString = "{\"query\":"+"\"\\\""+query+"\\\"\""+",";
			String incDocs = "\"include_docs\":true}";
			queryString = queryString + incDocs;
			System.out.println(queryString);
			post.setEntity(new StringEntity(queryString));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpResponse res = ReceiptsDBCloudantClientMgr.getCloudantClient().executeRequest(post);
		System.out.println("reading the response");
		InputStream data = null;
		StringBuilder builder = new StringBuilder();
		try {
			data = res.getEntity().getContent();
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(data));
				String line = null;
				while ((line = in.readLine()) != null){
					builder.append(line);
					System.out.println(line);
				}
				in.close();
			}
			catch (Exception e) {
				System.out.println("Error Parsing the request");
			}
			
			System.out.println("builder as string: "+builder.toString());
			
			try {
				jsonObject = new JSONObject(builder.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// review this
			res.getEntity().getContent().close();
			System.out.println("Ran res.getEntity().getContent().close()");
			
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO add finally
		System.out.println("jsonObject" + jsonObject.toString());
		return jsonObject; 
	}
	
	
	
	
	
	
	//** Old code keeping for reference
	/**
	 * This function was created to search using HTTP POST since the cloudant clinet
	 * that I am using can't perform HTTP POST search.
	 * When I send a number in HTTP GET it gets read as an integer and search doesn't match with
	 * string value of the same number.
	 * 
	 * @param query
	 * @param db
	 * @param searchIndexID
	 * @return
	 */
	/*
	public static String queryForStream(String query, Database db, String searchIndexID) {
		String search = searchIndexID;
		if(searchIndexID.contains("/")) {
				String[] v = searchIndexID.split("/");
				search = String.format("_design/%s/_search/%s", v[0], v[1]);
		}
		URIBuilder uriBuilder = URIBuilder.buildUri(db.getDBUri()).path(search);
		//uriBuilder.query("q", query);
		URI uri = uriBuilder.build();
		
		try {
			System.out.println("uri="+uri.toURL()+"\n"+uri.toASCIIString()+"\n"+uri.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		HttpPost post = new HttpPost(uri);		
		post.addHeader("Accept", "application/json");
		try {
			post.setEntity(new StringEntity("{\"query\":\"*:*\",\"id\":\"202809406834\",\"include_docs\":true}"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpResponse res = ReceiptsDBCloudantClientMgr.getCloudantClient().executeRequest(post);
		System.out.println("reading res");
		InputStream data = null;
		StringBuilder builder = new StringBuilder();
		String outputText = "";
		try {
			data = res.getEntity().getContent();
			
			
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(data));
				String line = null;
				while ((line = in.readLine()) != null){
					builder.append(line);
				}
			}
			catch (Exception e) {
				System.out.println("Error Parsing the request");
			}
			System.out.println("builder as string: "+builder.toString());
			
			try {
				JSONObject jsonObject = new JSONObject(builder.toString());
				System.out.println("\n\n Here is your doc \n\n");
				outputText = jsonObject.getJSONArray("rows").getJSONObject(0)
						.getJSONObject("doc").getJSONObject("map").toString();
				System.out.println();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			res.getEntity().getContent().close();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputText; 
	}
	*/

}
