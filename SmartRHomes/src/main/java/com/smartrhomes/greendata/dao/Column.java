package com.smartrhomes.greendata.dao;

/**
 * @author "Gaurava Srivastava"
 *
 * @param <T>
 */
public class Column<T> {
    
    private final String columnName;
    private final T columnValue;
    private DataType dataType;
    //expires never
    private int ttl=-1;

    public Column(String columnName, T columnValue) {
    	this(columnName, columnValue,null);
    }

	public Column(String columnName, T columnValue, DataType dataType) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        if(dataType!=null){
        	this.dataType = dataType;
        }else{
	        if(columnValue instanceof String){
	        	this.dataType = DataType.StringType;
			}else if (columnValue instanceof Long) {
				this.dataType = DataType.LongType;
			}else if(columnValue instanceof byte[]){
				this.dataType = DataType.ByteType;
			}else if(columnValue instanceof Boolean){
				this.dataType = DataType.BooleanType;
			}else if(columnValue instanceof Double){
				this.dataType = DataType.DoubleType;
			} 
        }
    }

    public Column(String columnName, T columnValue, DataType dataType,int ttl) {
        this(columnName,columnValue,dataType);
        this.ttl = ttl;
    }

    public String getColumnName() {
        return columnName;
    }

    public T getColumnValue() {
        return columnValue;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getTtl(){
        return this.ttl;
    }
    
    @Override
    public boolean equals(Object obj) {
    	String col = ((Column<T>)obj).columnName;
    	return this.columnName.equals(col);
    }
    
    @Override
    public int hashCode() {
    	int r = 17*columnName.hashCode();
    	return r+(this.columnValue.hashCode()*37);
    }
    
    @Override
    public String toString() {
        return "Column{" +
                "columnName='" + columnName + '\'' +
                ", columnValue=" + columnValue +
                ", dataType=" + dataType +
                ", ttl=" + ttl +
                '}';
    }
}

