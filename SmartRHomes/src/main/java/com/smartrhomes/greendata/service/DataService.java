package com.smartrhomes.greendata.service;

import java.util.Date;
import java.util.List;

/**
 * @author "Gaurava Srivastava"
 *
 */
public interface DataService {

	boolean insertMeterData(Meter meter);

	boolean insertMeterIDData(Meter meter);
	
	boolean updateMeterConfigData(Meter meter);
	
	Meter getMeterConfigData(String product, String meterId);
	
	List<Meter> getMeterIDData(String product);
	
	List<String> getMeterIDs(String product);
	
	boolean insertNucliousData(Nuclious nuclious);
	
	boolean insertNucliousIDData(Nuclious nuclious);

	boolean updateNucliousData(Nuclious nuclious);
	
	List<Nuclious> getNucliousData();
	
	Nuclious getNucliousConfigData(String nucliousId);
	
	List<String> getNucliousIDs();
	
	boolean insertDailyMeterData(DailyMeterData dailyMeterData);
	
	boolean insertDailyMeterData(List<DailyMeterData> dailyMeterData);
	

	/**
	 * @param cutOfDate (yyyy-mm-DD)
	 * @param nucliousId
	 * @return List<NucliouWideMeterData>
	 */
	List<NucliouWideMeterData> getAllMeterDataForNucliousAndCutOfDate(
			String cutOfDate, long nucliousId);

	/**
	 * @param nucliousId
	 * @param date  (yyyy-mm-DD)
	 * @return
	 */
	List<DailyMeterData> getDailyMeterData(long nucliousId, String date,String startRange, String endRange);

	List<DailyMeterData> getDailyMeterDataForOne(String product, String date,long meterId);

	
}
