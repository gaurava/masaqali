package com.smartrhomes.api.services;

import java.io.File;
import java.net.URISyntaxException;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


@Path("/")
public class Index {//Not in use

	
	@GET	
	@Produces("text/html")
	public Response  index() throws URISyntaxException {		
	
		File f = new File(System.getProperty("user.dir")+"/html/index.html");
		
		String mt = new MimetypesFileTypeMap().getContentType(f);
		return Response.ok(f, mt).build();		
		
	}	
	
	@GET	
	@Path("/dashboard")
	public Response  helloGet() {	
		
		File f = new File(System.getProperty("user.dir")+"/html/Dashboard.html");
		
		String mt = new MimetypesFileTypeMap().getContentType(f);
		return Response.ok(f, mt).build();
	}

    @POST
	@Path("/hello")
	public Response  helloPost(){					
		
		return Response.status(200).entity("HTTP POST method called").build();
		
	}	

}