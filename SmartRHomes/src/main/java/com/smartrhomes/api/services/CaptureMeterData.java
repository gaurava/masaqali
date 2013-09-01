package com.smartrhomes.api.services;//

import com.smartrhomes.greendata.util.JsonConversionInterface;

public class CaptureMeterData implements JsonConversionInterface {

	private String aid;
	private String mid;
	private String date;
	private String time;
	private String tQ;
	private String bV;
	private Data[] data;
	
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String gettQ() {
		return tQ;
	}
	public void settQ(String tQ) {
		this.tQ = tQ;
	}
	public String getbV() {
		return bV;
	}
	public void setbV(String bV) {
		this.bV = bV;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	
	public static void main(String[] args) {
		
//		String x = "{\"aid\":\"00001\",\"mid\":\"A6000000\",\"date\":\"2013-07-21\",\"time\":\"21:02:11\",\"tQ\":\"EA4000000000\",\"bV\":\"3.9\",\"data\":[{\"sd\":\"2013-07-21\",\"st\":\"05:01\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"07:58\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"08:04\",\"dm\":\"2000\",\"cq\":\"5000\"},{\"sd\":\"2013-07-21\",\"st\":\"20:13\",\"dm\":\"1000\",\"cq\":\"1000\"},{\"sd\":\"2013-07-21\",\"st\":\"20:48\",\"dm\":\"1000\",\"cq\":\"4000\"}]}";
//		Gson g = new Gson();
//		CaptureMeterData c = g.fromJson(x, CaptureMeterData.class);
//		
//		System.out.println(c.getData()[0].getSd());
	
//		CaptureMeterData cc = (CaptureMeterData) StringUtil.fromJson(x, CaptureMeterData.class);
//		
//		System.out.println(cc.getData()[0].getSd());
	}
	
}
