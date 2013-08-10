package com.smartrhomes.greendata.service;

public class NucliouWideMeterData {

	private long meterId;
	private String previousDateTime;
	private String latestDateTime;
	private long previousQty;
	private long latestQty;
	private long diff;
	private long sumOfConsumption;
	
	public long getMeterId() {
		return meterId;
	}
	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}
	public String getPreviousDateTime() {
		return previousDateTime;
	}
	public void setPreviousDateTime(String previousDateTime) {
		this.previousDateTime = previousDateTime;
	}
	public String getLatestDateTime() {
		return latestDateTime;
	}
	public void setLatestDateTime(String latestDateTime) {
		this.latestDateTime = latestDateTime;
	}
	public long getPreviousQty() {
		return previousQty;
	}
	public void setPreviousQty(long previousQty) {
		this.previousQty = previousQty;
	}
	public long getLatestQty() {
		return latestQty;
	}
	public void setLatestQty(long latestQty) {
		this.latestQty = latestQty;
	}
	public long getDiff() {
		return diff;
	}
	public void setDiff(long diff) {
		this.diff = diff;
	}
	public long getSumOfConsumption() {
		return sumOfConsumption;
	}
	public void setSumOfConsumption(long sumOfConsumption) {
		this.sumOfConsumption = sumOfConsumption;
	}
	
	@Override
	public String toString() {
		StringBuilder st = new StringBuilder();
		return st.append("{")
				.append("\"MeterId\":").append(meterId)
				.append(",\"PreviousDateTime\":").append(previousDateTime)
				.append(",\"LatestDateTime\":").append(latestDateTime)
				.append(",\"PreviousQty\":").append(previousQty)
				.append(",\"LatestQty\":").append(latestQty)
				.append(",\"Latest&Previous_diff\":").append(diff)
				.append(",\"sumOfConsumption\":").append(sumOfConsumption)
				.append("}").toString();
	}
	
}
