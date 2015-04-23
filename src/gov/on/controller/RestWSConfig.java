package gov.on.controller;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Used to configure the restful web services
 * @author pankil
 *
 */
public class RestWSConfig extends Application{

	@Override 
	public Set<Class<?>> getClasses() { 
		Set<Class<?>> classes = new HashSet<Class<?>>(); 
		classes.add(ImmsReceiptsServices.class); 
		classes.add(HealthRecordServices.class);
		System.out.println("Classes:"+classes); 
		return classes; 
	}
	
}
