/*package com.smartrhomes.api.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.smartrhomes.greendata.connection.CassandraOpenConnection;
import com.smartrhomes.greendata.service.InsertDataServiceImpl;

public class MyServices extends Application  {

	private static Set<Object> services = new HashSet<Object>();	

	
	public  MyServices() {		
			
			// initialize restful services			
			services.add(new Index());
			services.add(new CassandraOpenConnection());
			services.add(new InsertDataServiceImpl());
			
	}

	@Override
	public  Set<Object> getSingletons() {
		return services;
	}	
	
	public  static Set<Object> getServices() {		
		return services;
	}
	
}*/


 package com.smartrhomes.api.services;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.smartrhomes.greendata.connection.CassandraOpenConnection;
import com.smartrhomes.greendata.service.DataServiceImpl;

public class MyServices extends Application  {

	private static Set<Class<?>> services = new HashSet<Class<?>>();	

	
	public  MyServices() {		
			
			// initialize restful services			
//		services.add(GuiceFilter.class);
//		services.add(GuiceContainer.class);
//		services.add(ServiceCollection.class);
		services.add(CassandraOpenConnection.class);
//		services.add(Index.class);//Depricated
		services.add(ViewData.class);
		services.add(DataServiceImpl.class);
			
	}

	@Override
	public  Set<Class<?>> getClasses() {
		return services;
	}	
	
	public  static Set<Class<?>> getServices() {		
		return services;
	}
	
}
 