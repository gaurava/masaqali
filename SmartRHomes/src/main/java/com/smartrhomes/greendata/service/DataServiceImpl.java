package com.smartrhomes.greendata.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.smartrhomes.greendata.dao.CassandraClient;
import com.smartrhomes.greendata.dao.CassandraClientImpl;
import com.smartrhomes.greendata.dao.Column;
import com.smartrhomes.greendata.dao.CompositeColumns;
import com.smartrhomes.greendata.exceptions.DAOExceptions;
import com.smartrhomes.greendata.exceptions.UncheckedException;
import com.smartrhomes.greendata.util.DateUtil;
import com.smartrhomes.greendata.util.StringUtil;


/**
 * @author "Gaurava Srivastava"
 *
 */
//@Provider
public class DataServiceImpl implements DataService {

	private static final String configName = "Test Cluster";

	private static CassandraClient cassandraClient;

	public DataServiceImpl() {
		cassandraClient = new CassandraClientImpl(configName);//Need to pull desired configured connection while getting Connection client
	}


	@Override
	public boolean insertMeterData(Meter meter) {

		List<Column<?>> col = new ArrayList<Column<?>>();
		col.add(new Column<String>(Meter.FIELD.active.name(), meter.getActive()));
		col.add(new Column<String>(Meter.FIELD.nucliousId.name(), meter.getNucliousId()));
		col.add(new Column<String>(Meter.FIELD.location.name(), meter.getLocation()));
		col.add(new Column<Long>(Meter.FIELD.flowDuration.name(), meter.getFlowDuration()));
		col.add(new Column<Long>(Meter.FIELD.leastCount.name(), meter.getLeastCount()));
		col.add(new Column<Double>(Meter.FIELD.cutOfVoltage.name(), meter.getCutOfVoltage()));
		if(insertMeterIDData(meter)){
			try{
				cassandraClient.insertMultiDataTypeColumns(ColumnFamily.MeterData.name(), StringUtil.getRowKey(meter.getProduct(),meter.getMeterId()), col,null);
			} catch (DAOExceptions e) {
				System.out.println("Error while inserting data :"+e.getMessage());
				e.printStackTrace();
				return false;
			} catch (UncheckedException e) {
				System.out.println("Error in Row key Formation"+e.getMessage());
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

	@Override
	public boolean insertMeterIDData(Meter meter){
		Map<String,String> map = new HashMap<String, String>();
		map.put(meter.getMeterId(),"");
		try{
			cassandraClient.multiInsert(ColumnFamily.MeterIDData.name(), meter.getProduct(), map);
		} catch (DAOExceptions e) {
			System.out.println("Error while inserting data :"+e.getMessage());
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	@Override
	public boolean updateMeterConfigData(Meter meter) {
		List<Column<?>> col = new ArrayList<Column<?>>();
		if(null!=meter.getActive())
			col.add(new Column<String>(Meter.FIELD.active.name(), meter.getActive()));
		if(null!=meter.getNucliousId())
			col.add(new Column<String>(Meter.FIELD.nucliousId.name(), meter.getNucliousId()));
		if(null!=meter.getLocation())
			col.add(new Column<String>(Meter.FIELD.location.name(), meter.getLocation()));
		if(meter.getFlowDuration()!=0)
			col.add(new Column<Long>(Meter.FIELD.flowDuration.name(), meter.getFlowDuration()));
		if(meter.getLeastCount()!=0)
			col.add(new Column<Long>(Meter.FIELD.leastCount.name(), meter.getLeastCount()));
		if(meter.getCutOfVoltage()!=0.0)
			col.add(new Column<Double>(Meter.FIELD.cutOfVoltage.name(), meter.getCutOfVoltage()));
		try {
			cassandraClient.insertMultiDataTypeColumns(ColumnFamily.MeterData.name(), StringUtil.getRowKey(meter.getProduct(),meter.getMeterId()), col,null);
		} catch (DAOExceptions e) {
			System.out.println("Error while inserting data :"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (UncheckedException e) {
			System.out.println("Error in Row key Formation"+e.getMessage());
			return false;
		}
		return true;
	}


	@Override
	public Meter getMeterConfigData(String product,String meterId) {
		System.out.println("getMeterConfigData");
		Meter m = new Meter();
		try {
			Map<String, ByteBuffer> meterData = cassandraClient.getMultiDataTypeColumns(ColumnFamily.MeterData.name(),
					StringUtil.getRowKey(product, meterId), null);
			for (Entry<String, ByteBuffer> meter : meterData.entrySet()) {
				if (Meter.FIELD.nucliousId.name().equals(meter.getKey())) {
					m.setNucliousId(StringUtil.getString(meter.getValue()));
				} else if (Meter.FIELD.location.name().equals(meter.getKey())) {
					m.setLocation(StringUtil.getString(meter.getValue()));
				} else if (Meter.FIELD.cutOfVoltage.equals(meter.getKey())) {
					m.setCutOfVoltage(StringUtil.getDouble(meter.getValue()));
				} else if (Meter.FIELD.flowDuration.equals(meter.getKey())) {
					m.setFlowDuration(StringUtil.getLong(meter.getValue()));
				} else if (Meter.FIELD.leastCount.equals(meter.getKey())) {
					m.setLeastCount(StringUtil.getLong(meter.getValue()));
				} else if (Meter.FIELD.active.equals(meter.getKey())) {
					m.setActive(StringUtil.getString(meter.getValue()));
				}
			}
			m.setMeterId(meterId);
//			System.out.println("meterConfigData: "+m);
			return m;
		} catch (DAOExceptions e) {
			System.out.println("Error while retrieving data :"+e.getMessage());
			return null;
		}catch (UncheckedException ex){
			System.out.println("Error in Row key Formation"+ex.getMessage());
			return null;
		}
	}

	@Override
	public List<Meter> getMeterIDData(String product) {//Not in use
		//TODO make it avialable to get information about - mfg, make, expiry, softver, hardver etc.
		return null;
	}

	@Override
	public List<String> getMeterIDs(String product) {
		try{
			List<String> columnNames = cassandraClient.getColumnNames(ColumnFamily.MeterIDData.name(), product);
			return columnNames;
		}catch (DAOExceptions e) {
			System.out.println("Error while getting Meter ID data: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean insertNucliousData(Nuclious nuclious) {

		List<Column<?>> col = new ArrayList<Column<?>>();
		col.add(new Column<String>(Nuclious.FIELD.active.name(), nuclious.getActive()));
		col.add(new Column<String>(Nuclious.FIELD.location.name(), nuclious.getLocation()));
		col.add(new Column<String>(Nuclious.FIELD.iMEI.name(), nuclious.getiMEI()));
		col.add(new Column<String>(Nuclious.FIELD.sleepTime.name(), nuclious.getSleepTime()));
		col.add(new Column<String>(Nuclious.FIELD.wakeupTime.name(), nuclious.getWakeupTime()));
		col.add(new Column<String>(Nuclious.FIELD.orderNo.name(), nuclious.getOrderNo()));

		if(insertNucliousIDData(nuclious)){
			try{
				cassandraClient.insertMultiDataTypeColumns(ColumnFamily.NucliousData.name(), nuclious.getNucliousId(), col,null);
			} catch (DAOExceptions e) {
				System.out.println("Error while inserting Nuclious data :"+e.getMessage());
				e.printStackTrace();
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

	@Override
	public boolean updateNucliousData(Nuclious nuclious) {
		List<Column<?>> col = new ArrayList<Column<?>>();
		if(null!=nuclious.getActive())
			col.add(new Column<String>(Meter.FIELD.active.name(), nuclious.getActive()));
		if(null!=nuclious.getNucliousId())
			col.add(new Column<String>(Meter.FIELD.nucliousId.name(), nuclious.getNucliousId()));
		if(null!=nuclious.getLocation())
			col.add(new Column<String>(Meter.FIELD.location.name(), nuclious.getLocation()));
		if(null!=nuclious.getiMEI())
			col.add(new Column<String>(Meter.FIELD.flowDuration.name(), nuclious.getiMEI()));
		if(null!=nuclious.getSleepTime())
			col.add(new Column<String>(Meter.FIELD.leastCount.name(), nuclious.getSleepTime()));
		if(null!=nuclious.getWakeupTime())
			col.add(new Column<String>(Meter.FIELD.cutOfVoltage.name(), nuclious.getWakeupTime()));
		try {
			cassandraClient.insertMultiDataTypeColumns(ColumnFamily.NucliousData.name(), nuclious.getNucliousId(), col,null);
		} catch (DAOExceptions e) {
			System.out.println("Error while updating Nuclious data :"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (UncheckedException e) {
			System.out.println("Error in Row key Formation"+e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean insertNucliousIDData(Nuclious nuclious) {
		List<Column<?>> col = new ArrayList<Column<?>>();
		col.add(new Column<String>(nuclious.getNucliousId(), nuclious.getNucliousId()));
		try {
			cassandraClient.insertMultiDataTypeColumns(ColumnFamily.NucliousData.name(), Nuclious.ROW_KEY, col,null);
		} catch (DAOExceptions e) {
			System.out.println("Error while updating Nuclious ID data :"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (UncheckedException e) {
			System.out.println("Error in Row key Formation"+e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public List<Nuclious> getNucliousData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Nuclious getNucliousConfigData(String nucliousId) {
		Nuclious nuclious = new Nuclious();
		try {
			Map<String, ByteBuffer> nudata = cassandraClient.getMultiDataTypeColumns(ColumnFamily.NucliousData.name(), nucliousId, null);
			for (Entry<String, ByteBuffer> nuc : nudata.entrySet()) {
				if (Nuclious.FIELD.active.name().equals(nuc.getKey())) {
					nuclious.setActive(StringUtil.getString(nuc.getValue()));
				} else if (Nuclious.FIELD.location.name().equals(nuc.getKey())) {
					nuclious.setLocation(StringUtil.getString(nuc.getValue()));
				} else if (Nuclious.FIELD.iMEI.name().equals(nuc.getKey())) {
					nuclious.setiMEI(StringUtil.getString(nuc.getValue()));
				} else if (Nuclious.FIELD.sleepTime.name().equals(nuc.getKey())) {
					nuclious.setSleepTime(StringUtil.getString(nuc.getValue()));
				} else if (Nuclious.FIELD.wakeupTime.name().equals(nuc.getKey())) {
					nuclious.setWakeupTime(StringUtil.getString(nuc.getValue()));
				}else if (Nuclious.FIELD.orderNo.name().equals(nuc.getKey())) {
					nuclious.setOrderNo(StringUtil.getString(nuc.getValue()));
				}
			}
			nuclious.setNucliousId(nucliousId);
			return nuclious;
		} catch (DAOExceptions e) {
			System.out.println("Error while retrieving Nuclious data :"+e.getMessage());
			return null;
		}catch (UncheckedException ex){
			System.out.println("Error in Row key Formation"+ex.getMessage());
			return null;
		}
	}

	@Override
	public List<String> getNucliousIDs() {
		try{
			List<String> columnNames = cassandraClient.getColumnNames(ColumnFamily.NucliousData.name(), Nuclious.ROW_KEY);
			return columnNames;
		}catch (DAOExceptions e) {
			System.out.println("Error while getting Nuclious ID data: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	//TODO To Make a VO for incoming data and absorb that data as @DailyMeterData 
	
	@Override
	public boolean insertDailyMeterData(DailyMeterData dailyMeterData) {
		List<DailyMeterData> dm = new ArrayList<DailyMeterData>();
		dm.add(dailyMeterData);
		return insertDailyMeterData(dm);
	}

	@Override
	public boolean insertDailyMeterData(List<DailyMeterData> listDailyMeterData) {

		List<CompositeColumns> compList = new ArrayList<CompositeColumns>();
		List<Object> colNames;
		DailyMeterDataJSON colValue;
		CompositeColumns comp;
		for(DailyMeterData dailyMeterData : listDailyMeterData){
			colNames = new ArrayList<Object>();	//To form composite column name
			colNames.add(dailyMeterData.getMeterId());//Need to modify VO
			colNames.add(dailyMeterData.getProduct());
			colNames.add(DateUtil.dateToLong(dailyMeterData.getStartDate(),dailyMeterData.getStartTime(),null));
			colNames.add(dailyMeterData.getMarkFirst());
			colNames.add(DateUtil.dateToLong(dailyMeterData.getDate(),dailyMeterData.getTime(),null));
			//To prepare column value as JSON
			colValue = new DailyMeterDataJSON(dailyMeterData.getDuaration(),dailyMeterData.getConsumption(),dailyMeterData.getTotalQty(),dailyMeterData.getBatteryVoltage());

			comp = new CompositeColumns();
			comp.setColNames(colNames);
			comp.setKey(StringUtil.getRowKey(dailyMeterData.getNucliousId(),dailyMeterData.getDate()));
			comp.setColValue(new Gson().toJson(colValue, DailyMeterDataJSON.class));

			compList.add(comp);
		}
		try {
			cassandraClient.insertCompositeColumn(ColumnFamily.DailyMeterData.name(), compList);
		} catch (DAOExceptions e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<DailyMeterData> getDailyMeterData(long nucliousId,String date,String startRange, String endRange){

		int i=0;
		List<Object> o = null;
		List<DailyMeterData> dailyMeterData = new ArrayList<DailyMeterData>();
		String[] sdt = null;
		String s = null;
		DailyMeterDataJSON dataJSON = null;
		List<CompositeColumns> compositeColumnsByIndex=null;
		try{
			compositeColumnsByIndex = cassandraClient.getCompositeColumnsByIndex(StringUtil.getRowKey(nucliousId,date), 
					ColumnFamily.DailyMeterData.name(), startRange, endRange, false);
		}catch(DAOExceptions e){
			return null;
		}
		if(null!=compositeColumnsByIndex){
			for (CompositeColumns c : compositeColumnsByIndex) {
				DailyMeterData dm = new DailyMeterData();
				o = c.getColNames();
				for(i=0;i<o.size();i++){//List of Columns
					if(i==0){
						dm.setMeterId(Long.parseLong(String.valueOf(o.get(i))));
					}else if(i==1){
						dm.setProduct(String.valueOf(o.get(i)));
					}else if(i==2){
						s = DateUtil.longToStringDate(Long.parseLong(String.valueOf(o.get(i))),null);
						if(null!=s){
							sdt = s.split(" ");
							dm.setStartDate(sdt[0]);
							dm.setStartTime(sdt[1]);
						}
					}else if(i==3){
						dm.setMarkFirst(String.valueOf(o.get(i)));
					}else if(i==4){
						s = DateUtil.longToStringDate(Long.parseLong(String.valueOf(o.get(i))),null);
						if(null!=s){
							sdt = s.split(" ");
							dm.setDate(sdt[0]);
							dm.setTime(sdt[1]);
						}
					}
				}
				
				dm.setNucliousId(Long.parseLong(String.valueOf(nucliousId)));
				dataJSON = new Gson().fromJson(c.getColValue(), DailyMeterDataJSON.class);
				dm.setDuaration(dataJSON.getDuaration());
				dm.setConsumption(dataJSON.getConsumption());
				dm.setTotalQty(dataJSON.getTotalQty());
				dm.setBatteryVoltage(dataJSON.getBatteryVoltage());

				dailyMeterData.add(dm);
			}
		}
		return dailyMeterData;
	}

	@Override
	public List<NucliouWideMeterData> getAllMeterDataForNucliousAndCutOfDate(String cutOfDate, long nucliousId){
		List<DailyMeterData> meterDataCutOfDate = getDailyMeterData(nucliousId, cutOfDate,"0", String.valueOf(Long.MAX_VALUE));
		List<DailyMeterData> previousDayMeterData = getDailyMeterData(nucliousId, DateUtil.previousDate(cutOfDate),"0", String.valueOf(Long.MAX_VALUE));
		List<NucliouWideMeterData> listMeterData = null;
		if(null!=meterDataCutOfDate && null!=previousDayMeterData){
			listMeterData = new ArrayList<NucliouWideMeterData>();
			Set<Long> tempMId = new HashSet<Long>();
			
			//TODO This can be customized Set Object

			for (DailyMeterData d1 : meterDataCutOfDate) {
				if(d1.getMarkFirst().equalsIgnoreCase("1")){
					//					System.out.println("GotFirst");
					if(!tempMId.contains(d1.getMeterId())){
						NucliouWideMeterData nwmd1 = new NucliouWideMeterData();
						fillNucliousWideMeterData(nwmd1, d1.getMeterId(), d1.getTotalQty(), d1.getDate()+" "+d1.getTime(), 
								0, null, d1.getTotalQty(), (nwmd1.getSumOfConsumption()+d1.getConsumption()));
						listMeterData.add(nwmd1);
						tempMId.add(d1.getMeterId());
					}else{//Already Added to list
						for(NucliouWideMeterData nwmd2 : listMeterData){
							if(nwmd2.getMeterId()==d1.getMeterId()){
								fillNucliousWideMeterData(nwmd2, d1.getMeterId(), d1.getTotalQty(), d1.getDate()+" "+d1.getTime(), 
										0, null, -1, (nwmd2.getSumOfConsumption()+d1.getConsumption()));
							}
						}
					}
				}else{
					if(tempMId.contains(d1.getMeterId())){//Already Added to list
						for(NucliouWideMeterData nwmd1 : listMeterData){
							if(nwmd1.getMeterId()==d1.getMeterId()){
								nwmd1.setSumOfConsumption(nwmd1.getSumOfConsumption()+d1.getConsumption());
							}
						}
					}else{
						NucliouWideMeterData nwmd2 = new NucliouWideMeterData();
						nwmd2.setMeterId(d1.getMeterId());
						nwmd2.setSumOfConsumption(nwmd2.getSumOfConsumption()+d1.getConsumption());
						listMeterData.add(nwmd2);
						tempMId.add(d1.getMeterId());
					}
				}
			}

			for (DailyMeterData d2 : previousDayMeterData) {
				if(d2.getMarkFirst().equalsIgnoreCase("1")){
					//					System.out.println("GotFirst2");
					if(!tempMId.contains(d2.getMeterId())){
						NucliouWideMeterData nwmd1 = new NucliouWideMeterData();
						fillNucliousWideMeterData(nwmd1,d2.getMeterId(),d2.getTotalQty(),d2.getDate()+" "+d2.getTime(),nwmd1.getPreviousQty(),
								nwmd1.getPreviousDateTime(),(nwmd1.getLatestQty()-nwmd1.getPreviousQty()),nwmd1.getSumOfConsumption()+d2.getConsumption());
						listMeterData.add(nwmd1);
						tempMId.add(d2.getMeterId());
					}else{//Already Added to list
						for(NucliouWideMeterData nwmd2 : listMeterData){
							if(nwmd2.getMeterId()==d2.getMeterId()){
								fillNucliousWideMeterData(nwmd2, d2.getMeterId(), nwmd2.getPreviousQty(), nwmd2.getPreviousDateTime(), 
										d2.getTotalQty(), d2.getDate()+" "+d2.getTime(), -1, (nwmd2.getSumOfConsumption()+d2.getConsumption()));
							}
						}
					}
				}else{
					if(tempMId.contains(d2.getMeterId())){//Already Added to list
						for(NucliouWideMeterData nwmd1 : listMeterData){
							if(nwmd1.getMeterId()==d2.getMeterId()){
								nwmd1.setSumOfConsumption(nwmd1.getSumOfConsumption()+d2.getConsumption());
								nwmd1.setDiff(nwmd1.getLatestQty()-nwmd1.getPreviousQty());
							}
						}
					}else{
						NucliouWideMeterData nwmd2 = new NucliouWideMeterData();
						nwmd2.setMeterId(d2.getMeterId());
						nwmd2.setSumOfConsumption(nwmd2.getSumOfConsumption()+d2.getConsumption());
						listMeterData.add(nwmd2);
						tempMId.add(d2.getMeterId());
					}
				}
			}

			/*			for (NucliouWideMeterData d : l1) {
				System.out.println(d);
			}*/
		}

		return listMeterData;
	}

	private void fillNucliousWideMeterData(NucliouWideMeterData nwmd,long meterId, long previousQty,
			String previousDateTime, long latestQty, String latestDateTime, long totalDiff,
			long sumOfConsumption) {

		nwmd.setMeterId(meterId);
		nwmd.setPreviousDateTime(previousDateTime);
		nwmd.setPreviousQty(previousQty);
		nwmd.setLatestDateTime(latestDateTime);
		nwmd.setLatestQty(latestQty);
		nwmd.setSumOfConsumption(sumOfConsumption);
		if(totalDiff!=-1){
			nwmd.setDiff(totalDiff);
		}
	}

	@Override
	public List<DailyMeterData> getDailyMeterDataForOne(String product,String date, long meterId) {
		System.out.println("getDailyMeterDataForOne");
		Meter m = getMeterConfigData(product, String.valueOf(meterId));
		String nid = m.getNucliousId();
		String mid = m.getMeterId();
		String ml = String.valueOf(mid+1);
		List<DailyMeterData> dmd = new ArrayList<DailyMeterData>();
		for (DailyMeterData md : getDailyMeterData(Long.valueOf(nid), date, mid,ml)) {
//			System.out.println("***** "+md);
			if(md.getMeterId()==Long.parseLong(mid)){
				dmd.add(md);
			}
		}
		return dmd;
	}





}
