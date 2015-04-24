/**
 * 
 */
package gov.on.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Contains common functions that may be used by different controllers
 * 
 * @author pankil
 *
 */
public class ControllerUtility {
	
	public static String getRequestBodyString(InputStream data){
		StringBuilder builder = new StringBuilder();
		boolean parsingSuccess = true;
		String output = "";
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = null;
			while ((line = in.readLine()) != null){
				builder.append(line);
			}
			in.close();
		}
		catch (Exception e) {
			System.out.println("Error Parsing the request");
			parsingSuccess = false;
		}
		
		
		if (parsingSuccess) 
		{
			output = builder.toString();
		}
		System.out.println("parsing: " + parsingSuccess);
		return output;
	}
	

}
