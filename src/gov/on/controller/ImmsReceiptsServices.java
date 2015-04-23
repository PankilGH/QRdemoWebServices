package gov.on.controller;


import gov.on.businesslogic.ProcessReceipts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;



/**
 * Implements services that operate on immunization receipts.
 * 
 * @author pankil
 *
 */
@Path("api")
public class ImmsReceiptsServices {

	//used to keep track of recent requests
	private static String [] receiptList = new String[10];
	private static int counter = 0;
	private static final int trackLimit = 10;
	
	@GET
	@Path("rec")
	@Produces("text/html")
	public String recentReceipts(){
		
		String list = "Last " + trackLimit +" Clients:<br>";
		for (int i = 0; i < trackLimit; i++){
			if (i < counter){
				list += receiptList[i];
			}
		}
		list += "Current Counter = " + counter;
		return  list;
	}
	
	
	
	@GET
	@Path("receipts")
	@Produces("text/html")
	public Response listReceipts() {
		System.out.println("You are trying to get  a list of clients");
		ProcessReceipts bl = new ProcessReceipts();
		return Response.status(200).entity(bl.getClients()).build();
	}
	
	
	@POST
	@Path("client")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addReceipt(InputStream data) {
		
		System.out.println("Add receipt invoked");
		
		StringBuilder builder = new StringBuilder();
		boolean parsingSuccess = true;
		String output = "{\"status\":\"fail\"}";
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = null;
			while ((line = in.readLine()) != null){
				builder.append(line);
			}
		}
		catch (Exception e) {
			System.out.println("Error Parsing the request");
			parsingSuccess = false;
		}
		
		
		if (parsingSuccess) 
		{
			// call model to store the file
			ProcessReceipts bl = new ProcessReceipts();
			output = bl.saveReceipt(builder.toString());
		}
		
		
		// keep track of recent requests
		receiptList[counter%trackLimit] = "<b>" + (counter+1) + " | " 
								+ output + "</b>" 
								+"<br>" + "<p>" + builder.toString() 
								+ "</p>" + "<br>";
		counter++;
		
		return Response.status(201).entity(output).build();
	}
	
}
