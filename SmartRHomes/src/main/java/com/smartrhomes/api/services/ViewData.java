package com.smartrhomes.api.services;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartrhomes.greendata.exceptions.DAOExceptions;
import com.smartrhomes.greendata.service.DailyMeterData;
import com.smartrhomes.greendata.service.DataServiceImpl;
import com.smartrhomes.greendata.service.Meter;
import com.smartrhomes.greendata.service.NucliouWideMeterData;
import com.smartrhomes.greendata.service.Nuclious;
import com.smartrhomes.greendata.util.StringUtil;

@Path("/data/")
public class ViewData {

	/*private InsertDataService insertDataService;*/

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("meterdatafornuclious")
	public Response getMeterDataForNuclious(){
		String s = null;
		try{
			List<NucliouWideMeterData> in = CreateServiceInstance.getDataServiceImpl().getAllMeterDataForNucliousAndCutOfDate("2013-07-10",2);
			//TODO make a Default Response Class to take all input and then send in desired output format
			s= StringUtil.toJson(in);

		}catch(DAOExceptions e){
			e.printStackTrace();
			s="Server has some Connection Issues...";
		}
		return Response.status(200).entity(s).build();
	}


	@GET
	@Path("insertMeterData")
	public Response insertMeterData(@QueryParam("mid") String meterid,@QueryParam("addr") String no){
		Meter meter = new Meter();
		meter.setProduct("watermeter");
		meter.setMeterId(meterid);
		meter.setLocation("#"+no+", 4th cross 8th main, koramangla, bangalore");
		//		meter.setNucliousId("98765");
		meter.setCutOfVoltage(3.7);
		//		meter.setFlowDuration(206);
		meter.setLeastCount(1100);
		meter.setActive("InActive");

		//		meter.setMfg(System.currentTimeMillis()/1000);
		//		meter.setMadeIn("India");
		//		meter.setManufacturer("SamrtrHomes");
		//		meter.setSoftWVersion("S-0.1");
		//		meter.setHardWVersion("M-0.1");

		//		boolean flag1 = CreateServiceInstance.getDataServiceImpl().insertMeterIDData(meter);
				boolean flag2 = CreateServiceInstance.getDataServiceImpl().insertMeterData(meter);
		//		return Response.status(200).entity(flag1+"-"+flag2).build();
		return Response.status(200).entity("nothing").build();
	}
	
	@POST
	@Path("insertDailyMeterData")
	public Response insertDailyMeterData(@FormParam("meterData") String data, @FormParam("pid") String product){
		
//		String data = "{\"aid\":\"00001\",\"mid\":\"A6000000\",\"date\":\"2013-07-21\",\"time\":\"21:02:11\",\"tQ\":\"EA4000000000\",\"bV\":\"3.9\",\"data\":[{\"sd\":\"2013-07-21\",\"st\":\"05:01\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"07:58\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"08:04\",\"dm\":\"2000\",\"cq\":\"5000\"},{\"sd\":\"2013-07-21\",\"st\":\"20:13\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"20:48\",\"dm\":\"1000\",\"cq\":\"4000\"}]}";
//		String product ="w";
		boolean flag = false;
		if(null!=data && !data.isEmpty() && null!=product && !product.isEmpty()){
			List<DailyMeterData> dList = PrepareMeterData.getCapturedMeterData(data, product.toLowerCase());
			if(null!=dList){
				flag = CreateServiceInstance.getDataServiceImpl().insertDailyMeterData(dList);
			}
		}
		return Response.status(flag?200:500).entity(flag?"OK":"Incorrect Data").build();
	}

	@GET
	@Path("updateMeterData")
	public Response upadteMeterData(@QueryParam("mid") String meterid,@QueryParam("nid") String nid){
		Meter meter = new Meter();
		meter.setProduct("watermeter");
		meter.setMeterId(meterid);
		//		meter.setLocation("#"+no+", 4th cross 8th main, koramangla, bangalore");
		meter.setNucliousId(nid);
		meter.setCutOfVoltage(3.7);
		//		meter.setFlowDuration(206);
		//		meter.setLeastCount(1100);
		meter.setActive("Active");

		//		meter.setMfg(System.currentTimeMillis()/1000);
		//		meter.setMadeIn("India");
		//		meter.setManufacturer("SamrtrHomes");
		//		meter.setSoftWVersion("S-0.1");
		//		meter.setHardWVersion("M-0.1");

		boolean flag1 = CreateServiceInstance.getDataServiceImpl().updateMeterConfigData(meter);
		return Response.status(200).entity("nothing"+flag1).build();
	}

	@GET
	@Path("insertNucliousData")
	public Response insertNucliousData(@QueryParam("nid") String nid,@QueryParam("imei") String imei){

		Nuclious n = new Nuclious();
		n.setNucliousId(nid);
		//				n.setActive("Active");
		//				n.setLocation("Ashwini Layout, 3rd Block");
		//				n.setiMEI(imei);
		/*n.setSleepTime("");
		n.setWakeupTime("");
		n.setOrderNo("");*/

		boolean flag1 = CreateServiceInstance.getDataServiceImpl().insertNucliousIDData(n);
		//		boolean flag2 = CreateServiceInstance.getDataServiceImpl().insertNucliousData(n);

		return Response.status(200).entity("nothing-"+flag1).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("meterIds")
	public Response getMeterIdData(@QueryParam("p") String p){
		String s = null;
		try{
			List<String> in = CreateServiceInstance.getDataServiceImpl().getMeterIDs(p);
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
	@Path("meterIdData")
	public Response getMeterData(@QueryParam("mid") String mid, @QueryParam("date") String date, @QueryParam("p") String p){
		String s = null;
		try{
			List<DailyMeterData> in = CreateServiceInstance.getDataServiceImpl().getDailyMeterDataForOne(p,date, Long.valueOf(mid));
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
	@Path("nucliousIds")
	public Response getNucliousIdData(){
		String s = null;
		try{
			List<String> in = CreateServiceInstance.getDataServiceImpl().getNucliousIDs();
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
	@Path("NWMDData")
	public Response getNWMDData(@QueryParam("nid") String nid, @QueryParam("date") String date){
		String s = null;
		try{
			List<NucliouWideMeterData> in = CreateServiceInstance.getDataServiceImpl().getAllMeterDataForNucliousAndCutOfDate(date, Long.valueOf(nid));
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
