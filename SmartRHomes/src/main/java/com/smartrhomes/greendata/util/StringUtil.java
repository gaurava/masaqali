package com.smartrhomes.greendata.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smartrhomes.greendata.exceptions.UncheckedException;

public class StringUtil {
	public static final String ENCODING = "utf-8";
	public static final String ROW_SEPARATOR = "|";
	private static final String COLUMN_SEPARATOR = ":";
	
	/**
	 * @param bytes
	 * @return
	 */
	public static String bytesToString(byte[] bytes){
		if (null == bytes) {
			return null;
		}
		try {

			return new String(bytes, ENCODING);
		} catch (UnsupportedEncodingException e) {

			throw new UncheckedException("Byte to string conversion error UnsupportedEncodingException",e);
		}
	}

	/**
	 * @param bytes
	 * @return long
	 */
	public static long byteArrayToLong(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		long l = bb.getLong();

		return l;
	}

	
	public static String getRowKey(Object... o) throws UncheckedException{
		StringBuilder sb = new StringBuilder();
		String t;
		for (Object obj : o) {
			if(null!=obj){
				t =String.valueOf(obj);
				if(!(t.indexOf(ROW_SEPARATOR)>0)){
					sb.append(t).append(ROW_SEPARATOR);
				}else{
					throw new UncheckedException("Row Key can't have '|' in the key words!");
				}
			}
		}
		return sb.length()>0?sb.deleteCharAt(sb.lastIndexOf(ROW_SEPARATOR)).toString():null;
	}
	
	
	public static String[] getColumnsToList(String col){
//		List<String> strList = null;
		if(null!=col){
//			strList = new ArrayList<String>();
			String[] str = col.split(COLUMN_SEPARATOR);
//			for (String c : str) {
//				strList.add(c);
//			}
			return str;
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		int x = 123;
		double y = 12;
		String z = "12354";
		String a = getRowKey();
		if(null!=a)
		System.out.println(a);
		else
			System.out.println("nullll");
			
	}
	
	public static String getString(ByteBuffer columnName) {
		return get(StringSerializer.get(), columnName);
	}

	public static int getInt(ByteBuffer columnName) {
		return get(IntegerSerializer.get(), columnName);
	}

	public static long getLong(ByteBuffer columnName) {
		return get(LongSerializer.get(), columnName);
	}

	public static boolean getBoolean(ByteBuffer columnName) {
		return get(BooleanSerializer.get(), columnName);
	}

	public static double getDouble(ByteBuffer columnName) {
		return get(DoubleSerializer.get(), columnName);
	}

	public static <T> T get(Serializer<T> serializer, ByteBuffer columnValue) {
		return serializer.fromByteBuffer(columnValue);
	}

	
	public static <T> String toJson(T nl) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		Type nuType = new TypeToken<T>() {}.getType();
		String s = gson.toJson(nl,nuType);
		return s;
	}
}
