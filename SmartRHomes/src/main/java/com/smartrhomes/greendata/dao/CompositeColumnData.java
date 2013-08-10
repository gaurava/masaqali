package com.smartrhomes.greendata.dao;

import java.nio.ByteBuffer;
import java.util.List;

public class CompositeColumnData {

	private List<ByteBuffer> colNames;
	private String colValue;

	public List<ByteBuffer> getColNames() {
		return colNames;
	}
	public void setColNames(List<ByteBuffer> colNames) {
		this.colNames = colNames;
	}
	public String getColValue() {
		return colValue;
	}
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}
}
