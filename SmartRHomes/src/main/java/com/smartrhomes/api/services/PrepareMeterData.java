package com.smartrhomes.api.services;

import java.util.ArrayList;
import java.util.List;

import com.smartrhomes.greendata.service.DailyMeterData;
import com.smartrhomes.greendata.util.DateUtil;
import com.smartrhomes.greendata.util.StringUtil;

public class PrepareMeterData {

//	private static String TIME = "00:00:01";
	
	public static List<DailyMeterData> getCapturedMeterData(String data,String productInitials){
		List<DailyMeterData> dmdList = new ArrayList<DailyMeterData>();
		CaptureMeterData cData = (CaptureMeterData) StringUtil.fromJson(data, CaptureMeterData.class);
		if(cData!=null){
			DailyMeterData dmd;
			dmd = new DailyMeterData();
			//RowKey
			dmd.setDate(DateUtil.previousDate(cData.getDate()));
			dmd.setNucliousId(StringUtil.HexToLong(cData.getAid(), false));
			//ColumnName
			dmd.setMeterId(StringUtil.HexToLong(cData.getMid(), true));
			dmd.setProduct(productInitials);
			dmd.setStartDate(cData.getData().length>0?cData.getData()[0].getSd():DateUtil.previousDate(cData.getDate()));
			dmd.setStartTime(Vars.nonzerotime.val());
			dmd.setMarkFirst(Vars.one.val());
			//ColumnValue
			dmd.setBatteryVoltage(Double.parseDouble(cData.getbV()));
			dmd.setTotalQty(StringUtil.HexToLong(cData.gettQ(), true));
			dmd.setTime(cData.getTime());
			dmdList.add(dmd);
			if(cData.getData().length>0){
				for (Data d : cData.getData()) {
					dmd = new DailyMeterData();
					//RowKey
					dmd.setDate(DateUtil.previousDate(cData.getDate()));
					dmd.setNucliousId(StringUtil.HexToLong(cData.getAid(), false));
					//ColumnName
					dmd.setMeterId(StringUtil.HexToLong(cData.getMid(), true));
					dmd.setProduct(productInitials);
					dmd.setStartDate(d.getSd());
					dmd.setStartTime(d.getSt());
					dmd.setMarkFirst(Vars.zero.val());
					//ColumnValue
					dmd.setConsumption(Long.parseLong(d.getCq()));
					dmd.setDuaration(Long.parseLong(d.getDm()));
					dmd.setTime(cData.getTime());
					dmdList.add(dmd);
				}
			}
			return dmdList;
		}
		return null;
	}
	
	public static void main(String[] args) {
		String x = "{\"aid\":\"00001\",\"mid\":\"A6000000\",\"date\":\"2013-07-21\",\"time\":\"21:02:11\",\"tQ\":\"EA4000000000\",\"bV\":\"3.9\",\"data\":[{\"sd\":\"2013-07-21\",\"st\":\"05:01\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"07:58\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"08:04\",\"dm\":\"2000\",\"cq\":\"5000\"},{\"sd\":\"2013-07-21\",\"st\":\"20:13\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"20:48\",\"dm\":\"1000\",\"cq\":\"4000\"}]}";

		//CaptureMeterData cc = (CaptureMeterData) StringUtil.fromJson(x, CaptureMeterData.class);
		List<DailyMeterData> capturedMeterData = getCapturedMeterData(x, "w");
		
		System.out.println(capturedMeterData);
	}
}
