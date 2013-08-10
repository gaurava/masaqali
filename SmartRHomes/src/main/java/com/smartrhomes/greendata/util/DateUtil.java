package com.smartrhomes.greendata.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public static long dateToLong(String date, String time, String dateFormat){
		
		if((date!=null && time!=null) && checkZero(date)){
			if(dateFormat==null){
				dateFormat = DATE_TIME_FORMAT;
			}
			try {
				time = timeFormatCheck(time,dateFormat);
				DateFormat df = new SimpleDateFormat(dateFormat);
				Date dt = df.parse(date+" "+time);
				return dt.getTime()/1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	

	public static String longToStringDate(long timeStamp, String dateFormat){
		
		if(timeStamp!=0){
			if(dateFormat==null){
				dateFormat = DATE_TIME_FORMAT;
			}
//			Timestamp timestamp = Timestamp.valueOf("2013-07-19 10:10:10");
//			Date da = new Date(timestamp.getTime());
			DateFormat df = new SimpleDateFormat(dateFormat);
			
			return df.format(timeStamp*1000);
		}
		return null;
	}
	
	private static boolean checkZero(String date){
		String[] d = date.split("-");
		if(d[1].equals("00") || d[2].equals("00")){
			return false;
		}
		return true;
	}
	
	private static String timeFormatCheck(String time, String dateFormat) {
		if(time.length()==5 && dateFormat.indexOf(":ss")>0){
			return time+":00";
		}
		return time;
	}

	
	public static String previousDate(String date){
		
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date dt = df.parse(date);
			Date bdt = new Date(dt.getTime()-2);
			return df.format(bdt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		102::watermeter::0::0::1373461824
//		102::watermeter::0::0::1373461842
//		102::watermeter::0::0::1373461876
//		102::watermeter::0::0::1373461911
		System.out.println(dateToLong("2000-10-10", "00:00", null));
		System.out.println(longToStringDate(1373427491, null));
		System.out.println(previousDate("2013-03-1"));
	}
}
