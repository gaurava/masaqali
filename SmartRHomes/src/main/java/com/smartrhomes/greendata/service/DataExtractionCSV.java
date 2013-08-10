package com.smartrhomes.greendata.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataExtractionCSV {

	
	public static void main(String[] args) {
		
		fetchTestData();
	}
	
	public static List<DailyMeterData> fetchTestData(){
		File file = new File("G:\\workspace\\DemoData\\Roughdata\\testdata.txt");
		System.out.println(file.canRead());
		System.out.println(file.length());
		String line;
		try {
//			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			String[] fields = line.split(",");
			for(String f: fields)
			System.out.print(""+f);
			String[] temp;
			List<DailyMeterData> listDM = new ArrayList<DailyMeterData>();
			DailyMeterData dm;
			String marksFirst ="";
			String meterid ="";
			while((line=br.readLine())!=null){
				dm = new DailyMeterData();
				temp = line.split(",");
//				System.out.println("");
				
				String[] dt = temp[9].split(" ");
				dm.setDate(dt[0]);
				dm.setTime(dt[1]);
				dm.setStartDate(temp[3]);
				dm.setStartTime(temp[4]);
				dm.setProduct("watermeter");
				dm.setNucliousId(temp[2]);
				dm.setMeterId(temp[1]);
				if(!dt[0].equalsIgnoreCase(marksFirst) && !meterid.equalsIgnoreCase(temp[1])){
					marksFirst = dt[0];
					meterid=temp[1];
					dm.setMarkFirst("1");
					dm.setTotalQty(Long.parseLong(temp[7]));
					dm.setBatteryVoltage(Double.parseDouble(temp[8]));
				}else{
					dm.setMarkFirst("0");
				}
				dm.setDuaration(Long.parseLong(temp[5]));
				dm.setConsumption(Long.parseLong(temp[6]));
				
				listDM.add(dm);
				/*for(String t : temp){
					System.out.print(""+t);
				}*/
				System.out.println(dm);
			}
			return listDM;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
