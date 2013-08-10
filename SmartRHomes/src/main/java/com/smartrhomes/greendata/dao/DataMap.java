package com.smartrhomes.greendata.dao;

public class DataMap<N,V> {

	N colName;
	V colValue;

	public N getColName() {
		return colName;
	}
	public void setColName(N colName) {
		this.colName = colName;
	}
	public V getColValue() {
		return colValue;
	}
	public void setColValue(V colValue) {
		this.colValue = colValue;
	}
	
}
