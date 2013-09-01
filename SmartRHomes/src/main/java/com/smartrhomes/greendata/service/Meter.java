package com.smartrhomes.greendata.service;

/**
 * @author "Gaurava Srivastava"
 *
 */
public class Meter {

	enum FIELD{
		product,meterId,active,nucliousId,cutOfVoltage,flowDuration,leastCount,location,mfg,madeIn,manufacturer,softWVersion,hardWVersion,meterInfo
	}
	
	private String product;
	private String meterId;
	private String active = "InActive";
	private String nucliousId;
	private double cutOfVoltage;
	private long flowDuration;
	private long leastCount;
	private String location;
	
	private long mfg;
	private String madeIn;
	private String manufacturer;
	private String softWVersion;
	private String hardWVersion;
	private String meterInfo="";
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getMeterId() {
		return meterId;
	}
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getNucliousId() {
		return nucliousId;
	}
	public void setNucliousId(String nucliousId) {
		this.nucliousId = nucliousId;
	}
	public double getCutOfVoltage() {
		return cutOfVoltage;
	}
	public void setCutOfVoltage(double cutOfVoltage) {
		this.cutOfVoltage = cutOfVoltage;
	}
	public long getFlowDuration() {
		return flowDuration;
	}
	public void setFlowDuration(long flowDuration) {
		this.flowDuration = flowDuration;
	}
	public long getLeastCount() {
		return leastCount;
	}
	public void setLeastCount(long leastCount) {
		this.leastCount = leastCount;
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
				.append("\"MeterID\":").append(meterId)
				.append(",\"Active\":").append(active)
				.append(",\"NucliousId\":").append(nucliousId)
				.append(",\"CutOfVoltage\":").append(cutOfVoltage)
				.append(",\"FlowDuration\":").append(flowDuration)
				.append(",\"LeastCount\":").append(leastCount)
				.append(",\"Location\":").append(location)
				.append(",\"mfg\":").append(mfg)
				.append(",\"madeIn\":").append(madeIn)
				.append(",\"manufacturer\":").append(manufacturer)
				.append(",\"softWVersion\":").append(softWVersion)
				.append(",\"hardWVersion\":").append(hardWVersion)
				.append(",\"meterInfo\":").append(meterInfo)
				.append("}").toString();
	}
}
