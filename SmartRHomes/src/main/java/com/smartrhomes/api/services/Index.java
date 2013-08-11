package com.smartrhomes.api.services;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smartrhomes.greendata.exceptions.DAOExceptions;
import com.smartrhomes.greendata.service.InsertDataService;
import com.smartrhomes.greendata.service.InsertDataServiceImpl;
import com.smartrhomes.greendata.service.NucliouWideMeterData;
import com.smartrhomes.greendata.util.StringUtil;


@Path("/")
public class Index {

	
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