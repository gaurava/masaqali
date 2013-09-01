package com.smartrhomes.greendata.service;

public class DailyMeterDataJSON {

	private long duaration;
	private long consumption;
	private long totalQty;
	private Double batteryVoltage;
	
	public DailyMeterDataJSON() {
		
	}
	
	public DailyMeterDataJSON(long duaration, long consumption, long totalQty,Double batteryVoltage) {
		this.duaration = duaration;
		this.consumption = consumption;
		this.totalQty = totalQty;
		this.batteryVoltage = batteryVoltage;
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
}
