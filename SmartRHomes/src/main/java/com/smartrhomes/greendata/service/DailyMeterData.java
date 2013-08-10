package com.smartrhomes.greendata.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author "Gaurava Srivastava"
 *
 */
public class DailyMeterData {
	
	enum FIELD{
		date,time,startDate,startTime,product,nucliousId,meterId,markFirst,duaration,consumption,totalQty,batteryVoltage
	}

	private String date;
	private String time;
	private String startDate;
	private String startTime;
	private String product;
	private String nucliousId;
	private String meterId;
	private String markFirst;
	private long duaration;
	private long consumption;
	private long totalQty;
	private double batteryVoltage;
	
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getNucliousId() {
		return nucliousId;
	}
	public void setNucliousId(String nucliousId) {
		this.nucliousId = nucliousId;
	}
	public String getMeterId() {
		return meterId;
	}
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	public String getMarkFirst() {
		return markFirst;
	}
	/**
	 * @param markFirst <br>
	 * 1 for start of day <br>
	 * 0 for rest of the day's data
	 */
	public void setMarkFirst(String markFirst) {
		this.markFirst = markFirst;
	}
	public long getDuaration() {
		return duaration;
	}
	public void setDuaration(long duaration) {
		this.duaration = duaration;
	}
	public long getConsumption() {
		return consumption;
	}
	public void setConsumption(long consumption) {
		this.consumption = consumption;
	}
	public long getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(long totalQty) {
		this.totalQty = totalQty;
	}
	public Double getBatteryVoltage() {
		return batteryVoltage;
	}
	public void setBatteryVoltage(Double batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}
	
	@Override
	public String toString() {
		StringBuilder st = new StringBuilder();
		return st.append("{")
				.append("\"NucliousId\":").append(nucliousId)
				.append(",\"Date\":").append(date)
				.append(",\"Time\":").append(time)
				.append(",\"StartTime\":").append(startTime)
				.append(",\"StartDate\":").append(startDate)
				.append(",\"ProductId\":").append(product)
				.append(",\"MeterId\":").append(meterId)
				.append(",\"MarkFirst\":").append(markFirst)
				.append(",\"Duaration\":").append(duaration)
				.append("\"Consumption\":").append(consumption)
				.append(",\"TotalQty\":").append(totalQty)
				.append(",\"BatteryVoltage\":").append(batteryVoltage)
				.append("}").toString();
	}
	
	public static void main(String[] args) {
		
		DailyMeterData d = new DailyMeterData();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = dateFormat.parse("2013-07-19");
			long time = date.getTime();
			System.out.println(time);
			Timestamp t = new Timestamp(time);
			
			Timestamp timestamp = Timestamp.valueOf("2013-07-19 10:10:10.0");
			Date da = new Date(timestamp.getTime());
			System.out.println(timestamp.getTime()+"-->"+da.toString());
			Timestamp timestamp1 = Timestamp.valueOf("2013-07-20 10:10:10.0");
			Date da1 = new Date(timestamp1.getTime());
			System.out.println(timestamp1.getTime()+"-->"+da1.toString());
			Timestamp timestamp2 = Timestamp.valueOf("2013-07-21 10:10:10.0");
			Date da2 = new Date(timestamp2.getTime());
			System.out.println(timestamp2.getTime()+"-->"+da2.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		d.setDate(new Date(System.currentTimeMillis()));
//		
//		System.out.println(d.getDate());
	}
}
