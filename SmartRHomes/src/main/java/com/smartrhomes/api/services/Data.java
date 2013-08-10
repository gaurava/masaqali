package com.smartrhomes.api.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.smartrhomes.greendata.exceptions.DAOExceptions;
import com.smartrhomes.greendata.service.InsertDataService;
import com.smartrhomes.greendata.service.InsertDataServiceImpl;
import com.smartrhomes.greendata.service.NucliouWideMeterData;
import com.smartrhomes.greendata.util.StringUtil;

@Path("/data/")
public class Data {
	
	/*private InsertDataService insertDataService;*/

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("meterdatafornuclious")
	public Response getMeterDataForNuclious(){
		String s = null;
		try{
			List<NucliouWideMeterData> in = new InsertDataServiceImpl().getAllMeterDataForNucliousAndCutOfDate("2013-07-10",2);
			//TODO make a Default Response Class to take all input and then send in desired output format
			s= StringUtil.toJson(in);
			
			}catch(DAOExceptions e){
				e.printStackTrace();
				s="Server has some Connection Issues...";
			}
		return Response.status(200).entity(s).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("meterdata")
	public Response getMeterData(){
		String s = null;
		try{
			List<NucliouWideMeterData> in = new InsertDataServiceImpl().getAllMeterDataForNucliousAndCutOfDate("2013-07-10",2);
			//TODO make a Default Response Class to take all input and then send in desired output format
			s= StringUtil.toJson(in);
			
			}catch(DAOExceptions e){
				e.printStackTrace();
				s="Server has some Connection Issues...";
			}
		return Response.status(200).entity(s).build();
	}
	
	/*@Inject
	public void setInsertDataService(InsertDataService insertDataService){
		this.insertDataService = insertDataService;
	}*/
}
