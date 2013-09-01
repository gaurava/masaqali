package com.smartrhomes.greendata.service;

/**
 * @author "Gaurava Srivastava"
 *
 */
public class Nuclious {

	enum FIELD{
		nucliousId,active,iMEI,wakeupTime,sleepTime,orderNo,location,mfg,madeIn,manufacturer,softWVersion,hardWVersion,meterInfo
	}

	public static final String ROW_KEY = "NucliousID";
	
	private String nucliousId;
	private String active;
	private String iMEI;
	private String wakeupTime;
	private String sleepTime;
	private String orderNo;
	private String location;
	
	private long mfg;
	private String madeIn;
	private String manufacturer;
	private String softWVersion;
	private String hardWVersion;
	private String meterInfo="";
	
	public String getNucliousId() {
		return nucliousId;
	}
	public void setNucliousId(String nucliousId) {
		this.nucliousId = nucliousId;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getiMEI() {
		return iMEI;
	}
	public void setiMEI(String iMEI) {
		this.iMEI = iMEI;
	}
	public String getWakeupTime() {
		return wakeupTime;
	}
	public void setWakeupTime(String wakeupTime) {
		this.wakeupTime = wakeupTime;
	}
	public String getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public long getMfg() {
		return mfg;
	}
	public void setMfg(long mfg) {
		this.mfg = mfg;
	}
	public String getMadeIn() {
		return madeIn;
	}
	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getSoftWVersion() {
		return softWVersion;
	}
	public void setSoftWVersion(String softWVersion) {
		this.softWVersion = softWVersion;
	}
	public String getHardWVersion() {
		return hardWVersion;
	}
	public void setHardWVersion(String hardWVersion) {
		this.hardWVersion = hardWVersion;
	}
	public String getMeterInfo() {
		return meterInfo;
	}
	public void setMeterInfo(String meterInfo) {
		this.meterInfo = meterInfo;
	}
	@Override
	public String toString() {
		StringBuilder st = new StringBuilder();
		return st.append("{")
				.append("\"NucliousId\":").append(nucliousId)
				.append(",\"Active\":").append(active)
				.append(",\"IMEI\":").append(iMEI)
				.append(",\"WakeupTime\":").append(wakeupTime)
				.append(",\"SleepTime\":").append(sleepTime)
				.append(",\"OrderNo\":").append(orderNo)
				.append(",\"Location\":").append(location)
				.append("}").toString();
	}
	
}
