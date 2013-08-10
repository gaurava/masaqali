package com.smartrhomes.greendata.dao;

import java.util.List;

public class CompositeColumns {

	private String key;
	private List<Object> colNames;
	private String colValue;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Object> getColNames() {
		return colNames;
	}
	public void setColNames(List<Object> colNames) {
		this.colNames = colNames;
	}
	public String getColValue() {
		return colValue;
	}
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}
	
	@Override
	public String toString() {
		StringBuilder st = new StringBuilder();
		return st.append("{")
				.append("\"key\":").append(key)
				.append(",\"colNames\":").append(colNames)
				.append(",\"colValue\":").append(colValue)
				.append("}").toString();
	}
}
