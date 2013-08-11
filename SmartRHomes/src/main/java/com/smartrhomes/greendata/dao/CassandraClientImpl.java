package com.smartrhomes.greendata.dao;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.prettyprint.cassandra.model.thrift.ThriftColumnFactory;
import me.prettyprint.cassandra.serializers.BigIntegerSerializer;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.ByteBufferSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.DynamicCompositeSerializer;
import me.prettyprint.cassandra.serializers.FloatSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.TimeUUIDSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.AbstractComposite;
import me.prettyprint.hector.api.beans.AbstractComposite.Component;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.DynamicComposite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartrhomes.greendata.connection.CassandraConnection;
import com.smartrhomes.greendata.connection.CassandraConnectionPool;
import com.smartrhomes.greendata.connection.DataStoreConfig;
import com.smartrhomes.greendata.exceptions.DAOExceptions;
import com.smartrhomes.greendata.util.StringUtil;


/**
 * @author "Gaurava Srivastava"
 *
 */
public class CassandraClientImpl implements CassandraClient {

	public static final int MAXIMUM_FETCH_ROWS = 10000;
	public static final int MAXIMUM_COLUMN_COUNT = 200;
	public static final List<String> ENTITY_TIMESTAMP_COLUMNS = Arrays.asList("created_at", "lastmodified_at","timestamp", "issued_at", "expires_at");
	private String dataStoreConfigName;

	private static final Logger logger = LoggerFactory.getLogger(CassandraClientImpl.class.getName());

	/**
	 * Constructor
	 * @param dataStoreConfigName
	 */
	public CassandraClientImpl(String dataStoreConfigName) {
		this.dataStoreConfigName = dataStoreConfigName;
	}

	/**
	 * @return CassandraConnection
	 */
	protected CassandraConnection getConnection(String consString) throws DAOExceptions {
		System.out.println("CassandraClient.getConnection");
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.getConnection Getting connection");
		}
		try {
//			CassandraConnectionPool c = (CassandraConnectionPool)servletContext.getAttribute("CassandraConnectionPool");
			CassandraConnectionPool c = CassandraConnectionPool.getInstance(dataStoreConfigName);
			c.getDataStoreConfig().setConsistencyLevel(DataStoreConfig.getConsistencyLevel(consString, null));
			return c.getConnection();
		} catch (DAOExceptions e) {
			throw new DAOExceptions("CassandraClient.getConnection : Error while Connecting to Database: ",e);
		}
	}

	/**
	 * @param columnFamily
	 * @param rowKey
	 * @param columns
	 */
	@Override
	public void insertMultiDataTypeColumns(String columnFamily, String rowKey, Collection<Column<?>> columns, DataType dataType) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.insertMultiDataTypeColumns; column family {} key:{},columns:{}", new Object[]{columnFamily, rowKey, columns});
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
		
		for (Column<?> column : columns) {
			HColumn<?, ?> hcolumn = null;
			if (column.getDataType() == DataType.LongType) {
				hcolumn = HFactory.createColumn(column.getColumnName(), (Long) column.getColumnValue(), StringSerializer.get(), LongSerializer.get());
			} else if (column.getDataType() == DataType.StringType) {
				hcolumn = HFactory.createColumn(column.getColumnName(), (String) column.getColumnValue(), StringSerializer.get(), StringSerializer.get());
			} else if (column.getDataType() == DataType.BooleanType) {
				hcolumn = HFactory.createColumn(column.getColumnName(), (Boolean) column.getColumnValue(), StringSerializer.get(), BooleanSerializer.get());
			} else if (column.getDataType() == DataType.DoubleType) {
				hcolumn = HFactory.createColumn(column.getColumnName(), (Double) column.getColumnValue(), StringSerializer.get(), DoubleSerializer.get());
			} else if (column.getDataType() == DataType.ByteType) {
				hcolumn = HFactory.createColumn(column.getColumnName(), (byte[]) column.getColumnValue(), StringSerializer.get(), BytesArraySerializer.get());
			}
			if (column.getTtl() > 0) {
				hcolumn.setTtl(column.getTtl());
			}
			if(hcolumn!=null)
			mutator.addInsertion(rowKey, columnFamily, hcolumn);
		}
		try {
			mutator.execute();
		} catch (HectorException e) {
			throw new DAOExceptions("CassandraClient.insertMultiDataTypeColumns: Error While Inserting Data :",e);
		} finally {
			//mutator = null;
		}
	}

	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames
	 * @return Map<String, ByteBuffer>
	 */
	@Override
	public Map<String, ByteBuffer> getMultiDataTypeColumns(String columnFamily, String key, List<String> columnNames) throws DAOExceptions {
		Map<String, ByteBuffer> columnValues = new HashMap<String, ByteBuffer>();

		SliceQuery<String, String, ByteBuffer> query = HFactory.createSliceQuery(getConnection("one").getKeyspace(), StringSerializer.get(), StringSerializer.get(), ByteBufferSerializer.get());
		query.setColumnFamily(columnFamily).setKey(key).setRange("", "", false, 1000);
		QueryResult<ColumnSlice<String, ByteBuffer>> colResult = query.execute();

		for (HColumn<String, ByteBuffer> column : colResult.get().getColumns()) {
			//Column nameColumn = new Column<String>(column.getName(),column.getValue(), column.StringType);
			columnValues.put(column.getName(), column.getValue());
//			  System.out.println("Column Name :  " +column.getName());
//			  if(column.getName().equals("active"))
//			  System.out.println("Column value  :  " +StringUtil.getString(column.getValue()));
		}
		
		//System.out.println("Execution time: "   colResult.getExecutionTimeMicro());
		//System.out.println("CassandraHost used: "   colResult.getHostUsed());
		//System.out.println("Query Execute: "   colResult.getQuery());
		return columnValues;
	}
	

	/**
	 * @param columnFamilyName
	 * @param rowkey
	 * @param columnNamesVsValues : Map<String, String>
	 */
	@Override
	public void multiInsert(String columnFamilyName, String rowkey, Map<String, String> columnNamesVsValues) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.multiInsert; columnFamily:{} key {} ", new Object[]{columnFamilyName, rowkey});
		}

		try {
			CassandraConnection connection = getConnection("one");
			Keyspace keyspaceObj = connection.getKeyspace();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
			ThriftColumnFactory columnFactory = new ThriftColumnFactory();
			for (Map.Entry<String, String> entry : columnNamesVsValues.entrySet()) {
				HColumn<String, ?> hcolumn = columnFactory.createColumn(entry.getKey(), entry.getValue(), StringSerializer.get(), StringSerializer.get());
				mutator.addInsertion(rowkey, columnFamilyName, hcolumn);
			}
			try {
				mutator.execute();
			} catch (HectorException e) {
				if (logger.isErrorEnabled()) {
					logger.error("MulitInsert failed : rowkey:{}, columnfamily:{}", new Object[]{rowkey, columnFamilyName});
				}
				throw new DAOExceptions("MulitInsert failed: ",e);
			} finally {

			}
		} finally {

		}
	}

	/**
	 * @param columnFamily
	 * @param rows : Map<String, Map<String, String>>
	 */
	@Override
	public void multiInsert(String columnFamily, Map<String, Map<String, String>> rows) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.multiInsert rows column family {} ", columnFamily);
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();

		Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
		ThriftColumnFactory columnFactory = new ThriftColumnFactory();

		try {
			Iterator<String> it = rows.keySet().iterator();

			while(it.hasNext())
			{
				String rowKey = it.next();
				Map<String,String> columns = rows.get(rowKey);
				for (String columnName : columns.keySet()) {
					HColumn<String, ?> hcolumn = null;
					hcolumn = columnFactory.createColumn(columnName, columns.get(columnName), StringSerializer.get(), StringSerializer.get());
					mutator.addInsertion(rowKey, columnFamily, hcolumn);
				}
			}

			mutator.execute();
			// ToDo get the row key & column names for batch insert
		}catch(HectorException e){
			throw new DAOExceptions("Error while Inserting Column values:",e);
		}
	}

	/**
	 * @param columnFamily
	 * @param keysList : Collection<String>
	 * @return Map<String, Map<String, String>>
	 */
	@Override
	public Map<String, Map<String, String>> multiGet (String  columnFamily, Collection<String> keysList) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.multiGet column family {} keysList {}", columnFamily, keysList);
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();


		try {
			MultigetSliceQuery<String, String, byte[]> multigetSliceQuery =
					HFactory.createMultigetSliceQuery(keyspaceObj, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
			multigetSliceQuery.setColumnFamily(columnFamily);
			multigetSliceQuery.setKeys(keysList);
			// set null range for empty byte[] on the underlying predicate
			multigetSliceQuery.setRange(null, null, false, MAXIMUM_COLUMN_COUNT);
			QueryResult<Rows<String, String, byte[]>> result = multigetSliceQuery.execute();
			Rows<String, String, byte[]> orderedRows = result.get();
			Map<String, Map<String, String>> resultsMap = new HashMap<String, Map<String, String>>();
			for (Row<String, String, byte[]> currentRow : orderedRows) {
				if (currentRow.getColumnSlice().getColumns() != null && currentRow.getColumnSlice().getColumns().size() > 0) {//check if it has not been deleted
					Map<String, String> currentColumnMap = new HashMap<String, String>();
					for (HColumn<String, byte[]> currentColumn : currentRow.getColumnSlice().getColumns()) {
						if (ENTITY_TIMESTAMP_COLUMNS.contains(currentColumn.getName())) {
							ByteBuffer bb;
							bb = ByteBuffer.wrap(currentColumn.getValue());
							currentColumnMap.put(currentColumn.getName(), String.valueOf(bb.getLong()));
							continue;
						}
						String value = new String(currentColumn.getValue());
						currentColumnMap.put(currentColumn.getName(), value);
					}
					resultsMap.put(currentRow.getKey(), currentColumnMap);
				}
			}
			return resultsMap;
		} catch (HectorException e) {

			throw new DAOExceptions("",e);
		} finally {

		}
	}

	/**
	 * @param columnFamily
	 * @param key
	 * @return List<String>
	 */
	@Override
	public List<String> getColumnNames(String columnFamily, String key) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.getColumnNames column family {} key {}", columnFamily, key);
		}
		return getColumnNames(columnFamily, key, 0);
	}

	/**
	 * @param columnFamily
	 * @param key
	 * @param noOfColumns
	 * @return List<String>
	 */
	@Override
	public List<String> getColumnNames(String columnFamily, String key, int noOfColumns) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.getAllColumns() column family {} key {}", columnFamily, key);
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();

		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspaceObj, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(columnFamily);
		rangeSlicesQuery.setKeys(key, key);
		rangeSlicesQuery.setRange(null, null, false, noOfColumns == 0 ? MAXIMUM_COLUMN_COUNT : noOfColumns);

		try {
			QueryResult<OrderedRows<String, String, String>> results = rangeSlicesQuery.execute();
			List<String> colNames = new ArrayList<String>();
			String colN;
			for (Row<String, String, String> row : results.get()) {
				for (HColumn<String, String> col : row.getColumnSlice().getColumns()) {
					colN = col.getName();
					colNames.add(colN);
				}
			}
			return colNames;
		} catch (HectorException e) {

			throw new DAOExceptions("",e);
		} finally {

		}
	}


	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames : Collection<String>
	 */
	@Override
	public void deleteColumns(String columnFamily, String key, Collection<String> columnNames) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.deleteColumns from column family {} key {}", columnFamily, key);
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());

		try {
			for (String columnName : columnNames) {
				mutator.addDeletion(key, columnFamily, columnName, StringSerializer.get());
			}
			mutator.execute();
		} catch (HectorException e) {

			throw new DAOExceptions("",e);
		} finally {

		}
	}

	/**
	 * @param columnFamily
	 * @param keysList : Collection<String>
	 */
	@Override
	public void multiDelete(String columnFamily, Collection<String> keysList) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.multiDelete column family {} keysList {}", columnFamily, keysList);
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
		mutator.addDeletion(keysList, columnFamily);
		mutator.execute();
	}


	/**
	 * @param columnFamilyName
	 * @param rowkeys : List<String>
	 */
	@Override
	public void deleteMultipleRows(String columnFamilyName, List<String> rowkeys) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.deleteMultipleRows; columnFamily:{} rowskeys: {}", new Object[]{columnFamilyName, rowkeys});
		}

		try {
			CassandraConnection connection = getConnection("one");
			Keyspace keyspaceObj = connection.getKeyspace();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
			for (String rowkey : rowkeys) {
				mutator.addDeletion(rowkey, columnFamilyName);
			}
			try {
				mutator.execute();
			} catch (HectorException e) {
				if (logger.isErrorEnabled()) {
					logger.error("deleteMultipleRows failed : columnFamily:{} rowskeys: {}", new Object[]{columnFamilyName, rowkeys}, e);
				}
				throw new DAOExceptions("",e);
			} finally {

			}
		} finally {

		}
	}

	/**
	 * @param columnFamilyName
	 * @param rowkey
	 * @param columnNames : Collection<String>
	 */
	@Override
	public void multiDeleteColumnsInASingleRow(String columnFamilyName, String rowkey, Collection<String> columnNames) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.multiDeleteColumnsInASingleRow; columnFamily:{} key {}, columns:{}", new Object[]{columnFamilyName, rowkey, columnNames});
		}

		try {
			CassandraConnection connection = getConnection("one");
			Keyspace keyspaceObj = connection.getKeyspace();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
			for (String columnName : columnNames) {
				mutator.addDeletion(rowkey, columnFamilyName, columnName, StringSerializer.get());
			}
			try {
				mutator.execute();
			} catch (HectorException e) {
				if (logger.isErrorEnabled()) {
					logger.error("MulitDelete failed : columnFamily:{} key {}, columns:{}", new Object[]{columnFamilyName, rowkey, columnNames});
				}
				throw new DAOExceptions("",e);
			} finally {

			}
		} finally {

		}
	}

	
	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames
	 * @param columnValues
	 * @throws DAOExceptions
	 */
	/*@Override
	public void insertCompositeColumn(String columnFamily, String key, CompositeColumns compositeColumns) throws DAOExceptions {
		try {
			CassandraConnection connection = getConnection("one");
			Keyspace keyspaceObj = connection.getKeyspace();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
			
			HColumn<Composite, String> hColumn = HFactory.createColumn(getCompositeColumns(columnNames), columnValues, new CompositeSerializer(), StringSerializer.get());
			
			mutator.addInsertion(key, columnFamily, hColumn);
			mutator.execute();
			
		} catch (Exception e) {
			logger.error("Error while Inserting Composite Columns");
			throw new DAOExceptions("CassandraClient.insertCompositeColumns : Error while Inserting Composite Columns", e);
		}
			
		
	}*/
	
	@Override
	public void insertCompositeColumn(String columnFamily, List<CompositeColumns> cols) throws DAOExceptions {
		try {
			CassandraConnection connection = getConnection("one");
			Keyspace keyspaceObj = connection.getKeyspace();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
			HColumn<Composite, String> hColumn;
			for(CompositeColumns comp : cols){
				hColumn = HFactory.createColumn(getCompositeColumns(comp.getColNames()), comp.getColValue(), new CompositeSerializer(), StringSerializer.get());
				mutator.addInsertion(comp.getKey(), columnFamily, hColumn);
			}
			mutator.execute();
			
		} catch (Exception e) {
			logger.error("Error while Inserting Composite Columns");
			throw new DAOExceptions("CassandraClient.insertCompositeColumns : Error while Inserting Composite Columns", e);
		}
		
		
	}
	
	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames: List of Column Names to be part of DynamicComposite column
	 * @param columnValues : Column values in the form of Json--> Map<String,?>
	 */
	@Override
	public void insertDynamicComposite(String columnFamily, String key, List<?> columnNames, String columnValues) throws DAOExceptions {
		logger.debug("CassandraClient.insertDynamicComposite");
		try {
			CassandraConnection connection = getConnection("one");
			Keyspace keyspaceObj = connection.getKeyspace();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get()); 
			/*DynamicComposite itemIdColumnName = new DynamicComposite();
			for (Object s : columnNames) {
				itemIdColumnName.addComponent(String.valueOf(s), StringSerializer.get());
			}*/
			
		    HColumn<DynamicComposite, String> col = HFactory.createColumn(getCompositeColumnsString(columnNames), columnValues, new DynamicCompositeSerializer(), StringSerializer.get());
		    mutator.addInsertion(key, columnFamily, col);
		    mutator.execute();
		} catch (Exception e) {
			logger.error("Error while Inserting Dynamic Composite Columns");
			throw new DAOExceptions("CassandraClient.insertDynamicComposite : Error while Inserting Dynamic Composite Columns", e);
		}
	}
	
	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames : List<String>
	 * @param timeRange : List<Long>
	 * @param rows
	 * @return List<String>
	 */
	@Override
	public List<String> getColumnValueByCompositeColumns(String columnFamily, String key, List<String> columnNames, List<Long> timeRange, int rows) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.getColumnNames column family {} key {}", columnFamily, key);
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();

		SliceQuery<String, DynamicComposite, String> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new DynamicCompositeSerializer(), StringSerializer.get());
		Long startTime = null, endTime = null;
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(key);
		DynamicComposite startRange = new DynamicComposite();
		DynamicComposite endRange = new DynamicComposite();
		for (int i = 0; i < columnNames.size() - 1; i++) {
			startRange.addComponent(i, columnNames.get(i), AbstractComposite.ComponentEquality.EQUAL);
			endRange.addComponent(i, columnNames.get(i), AbstractComposite.ComponentEquality.EQUAL);
		}
		startRange.addComponent(columnNames.size() - 1, columnNames.get(columnNames.size() - 1), AbstractComposite.ComponentEquality.EQUAL);
		endRange.addComponent(columnNames.size() - 1, columnNames.get(columnNames.size() - 1), AbstractComposite.ComponentEquality.GREATER_THAN_EQUAL);
		sliceQuery.setRange(startRange, endRange, false, (rows == 0) ? MAXIMUM_FETCH_ROWS : rows);

		try {
			QueryResult<ColumnSlice<DynamicComposite, String>> result = sliceQuery.execute();
			ColumnSlice<DynamicComposite, String> columnSlice = result.get();
			List<String> columnValueList = new ArrayList<String>();
			if (timeRange != null && timeRange.size() == 2) {
				startTime = timeRange.get(0);
				endTime = timeRange.get(1);
			}
			for (HColumn<DynamicComposite, String> hColumn : columnSlice.getColumns()) {
				Long columnTimeStamp = hColumn.getClock();
				if (timeRange != null && timeRange.size() == 2) {
					if (columnTimeStamp >= startTime && columnTimeStamp <= endTime) {
						columnValueList.add(hColumn.getValue());
					}
				} else {
					columnValueList.add(hColumn.getValue());
				}
			}
			return columnValueList;
		} finally {

		}
	}

	
	/**
	 * @param rowKey
	 * @param columnFamily
	 * @param start
	 * @param end
	 * @param reversed
	 * @return Map<String,String>
	 */
	@Override
	public Map<String,String> getDynamicCompositeColumns(String rowKey, String columnFamily, List<String> start, List<String> end, boolean reversed) throws DAOExceptions {
		System.out.println("getDynamicCompositeColumns");
		Map<String,String> map = new HashMap<String,String>();
		DynamicCompositeQueryIterator columnSliceIterator = new DynamicCompositeQueryIterator(fetchDynamicCompositeColumnsString(rowKey, columnFamily, start, end, reversed));
		StringBuilder colName = new StringBuilder();
		for (HColumn<DynamicComposite, String> hColumn : columnSliceIterator) {
			for(int i=0; i<hColumn.getName().size(); i++){
				colName.append(hColumn.getName().get(i, StringSerializer.get())).append(":");
			}
			map.put(colName.deleteCharAt(colName.lastIndexOf(":")).toString(),hColumn.getValue());
//			System.out.println("hColumn.getName() : "+colName.deleteCharAt(colName.lastIndexOf(":")).toString());
//			System.out.println("hColumn.getValue(): "+hColumn.getValue());
//			System.out.println(hColumn.getName().get(0,StringSerializer.get()));
//			System.out.println(hColumn.getName().get(1,StringSerializer.get()));
//			System.out.println(hColumn.getName().get(2,StringSerializer.get()));
//				System.out.println(hColumn.getClock());
			colName = new StringBuilder();
			}
		return map;
	}
	

	@Override
	public List<CompositeColumns> getCompositeColumnsByIndex(String rowKey, String columnFamily, String start, String end, boolean reversed) throws DAOExceptions{
		List<CompositeColumns> colList = new ArrayList<CompositeColumns>();
//		List colsName;
		ColumnSlice<Composite, String> columnSlice = fetchCompositeColumnsForIndex(rowKey, columnFamily, start, end, reversed);
		for ( HColumn<Composite, String> col: columnSlice.getColumns() ) {
			CompositeColumns compCol = new CompositeColumns();
			List<Object> colsName = new ArrayList<Object>();
			/*for(Component<?> comp : col.getName().getComponents()){
				System.out.println(StringUtil.getLong(comp.getBytes()));
				colsName.add(comp.getBytes());
			}*/
			
			colsName.add(col.getName().get(0, LongSerializer.get()));
			colsName.add(col.getName().get(1, StringSerializer.get()));
			colsName.add(col.getName().get(2, LongSerializer.get()));
			colsName.add(col.getName().get(3, StringSerializer.get()));
			colsName.add(col.getName().get(4, LongSerializer.get()));
			compCol.setColNames(colsName);
			compCol.setColValue(col.getValue());
			
			colList.add(compCol);
			
			
		      /*System.out.println(
		    		  		  col.getName().get(0, LongSerializer.get())+"::"+
		    		  		  col.getName().get(1, StringSerializer.get())+"::"+
						      col.getName().get(2, LongSerializer.get())+"::"+
						      col.getName().get(3, BigIntegerSerializer.get())+"::"+
						      col.getName().get(4, LongSerializer.get())
						      );
		      System.out.println(col.getValue());*/
		}
		/*int i =0;
		for(CompositeColumns co : colList){
			
			List<ByteBuffer> colNames = co.getColNames();
			for(i=0;i<colNames.size()-1;i++){
				if(i==0){
					System.out.println("------"+colNames.get(i).position(0));
				System.out.println(StringUtil.getLong(colNames.get(i)));
				}
			}
		}*/
		/*for(int i=0;i<colList.size();i++){
			System.out.println(colList.get(i));
		}*/
		return colList;
	}
	
	private ColumnSlice<Composite, String> fetchCompositeColumnsForIndex(String rowKey, String columnFamily, String start, String end, boolean reversed) {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		SliceQuery<String, Composite, String> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new CompositeSerializer(), StringSerializer.get());
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(rowKey);
		Composite s = new Composite();
		s.addComponent(0,Long.parseLong(start),LongSerializer.get(),"LongType",AbstractComposite.ComponentEquality.EQUAL);//To get data for a single/range of meter only
		Composite e = new Composite();
		e.addComponent(0,Long.parseLong(end),LongSerializer.get(),"LongType",AbstractComposite.ComponentEquality.EQUAL);//To get data for a single/range of meter only
		
		sliceQuery.setRange(s, e, false, 10000);
		QueryResult<ColumnSlice<Composite, String>> result = sliceQuery.execute();
	    ColumnSlice<Composite, String> cs = result.get();
		return cs;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String,List> getCompositeColumns(String rowKey, String columnFamily, List<String> start, List<String> end, boolean reversed) throws DAOExceptions {
		System.out.println("getCompositeColumns");
		Map<String,List> map = new HashMap<String,List>();
//		CompositeQueryIterator columnSliceIterator = new CompositeQueryIterator(fetchCompositeColumnsString(rowKey, columnFamily, start, end, reversed));
//		StringBuilder colName = new StringBuilder();
		ColumnSlice<Composite, String> columnSlice = fetchCompositeColumns(rowKey, columnFamily, start, end, reversed);
		for ( HColumn<Composite, String> col: columnSlice.getColumns() ) {
			/*//System.out.println(col.getName().getComponents());
		      Map<Class<? extends Serializer>, String> serializerToComparatorMapping = col.getName().getSerializerToComparatorMapping();
		      for (Entry<Class<? extends Serializer>, String> st : serializerToComparatorMapping.entrySet()) {
				System.out.println( st.getKey()+"--"+st.getValue());
		      }*/
			int i= 0;
				for(Component<?> comp : col.getName().getComponents()){
					if(i==0)
					System.out.println(StringUtil.getLong(comp.getBytes()));
					i=1;
				}
		      System.out.println(
		    		  		  col.getName().get(0, LongSerializer.get())+"::"+
		    		  		  col.getName().get(1, StringSerializer.get())+"::"+
						      col.getName().get(2, LongSerializer.get())+"::"+
						      col.getName().get(3, StringSerializer.get())+"::"+
						      col.getName().get(4, LongSerializer.get())
						      );
		      System.out.println(col.getValue());
		}
		
		/*List colNames = new ArrayList();
		for (HColumn<Composite, String> hColumn : columnSliceIterator) {
			for(int i=0; i<hColumn.getName().size(); i++){
				colNames.add(hColumn.getName().get(i));
//				System.out.println("hColumn.getName().get(i) : "+hColumn.getNameSerializer());
//				colName.append(hColumn.getName().get(i)).append(":");
			}
			map.put("ColumnNames", colNames);*/
//			map.put(colName.deleteCharAt(colName.lastIndexOf(":")).toString(),hColumn.getValue());
//			System.out.println("hColumn.getName() : "+colName.deleteCharAt(colName.lastIndexOf(":")).toString());
//			System.out.println("hColumn.getValue(): "+hColumn.getValue());
//			System.out.println(hColumn.getName().get(0,StringSerializer.get()));
//			System.out.println(hColumn.getName().get(1,StringSerializer.get()));
//			System.out.println(hColumn.getName().get(2,StringSerializer.get()));
//				System.out.println(hColumn.getClock());
//			colName = new StringBuilder();
//		}
		return map;
	}
	
	private ColumnSliceIterator<String, DynamicComposite, String> fetchDynamicCompositeColumnsString(String rowKey, String columnFamily, List<String> start, List<String> end, boolean reversed) {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		SliceQuery<String, DynamicComposite, String> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new DynamicCompositeSerializer(), StringSerializer.get());
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(rowKey);
		ColumnSliceIterator<String, DynamicComposite, String> sliceIterator = new ColumnSliceIterator<String, DynamicComposite, String>(sliceQuery, getDynamicCompositeRange(start, true), getDynamicCompositeRange(end, false), reversed);
		return sliceIterator;
	}
	
	private ColumnSliceIterator<String, Composite, String> fetchCompositeColumnsString(String rowKey, String columnFamily, List<String> start, List<String> end, boolean reversed) {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		SliceQuery<String, Composite, String> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new CompositeSerializer(), StringSerializer.get());
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(rowKey);
		ColumnSliceIterator<String, Composite, String> sliceIterator = new ColumnSliceIterator<String, Composite, String>(sliceQuery, getCompositeRange(start, true), getCompositeRange(end, false), reversed);
		return sliceIterator;
	}

	private ColumnSlice<Composite, String> fetchCompositeColumns(String rowKey, String columnFamily, List<String> start, List<String> end, boolean reversed) {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		SliceQuery<String, Composite, String> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new CompositeSerializer(), StringSerializer.get());
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(rowKey);
		Composite s = new Composite();
		s.addComponent(0,Long.parseLong("107"),LongSerializer.get(),"LongType",AbstractComposite.ComponentEquality.EQUAL);//To get data for a meter only
//		s.addComponent(1,0L,LongSerializer.get(),"LongType",AbstractComposite.ComponentEquality.EQUAL);
//		s.addComponent(2,"watermeter",StringSerializer.get(),"UTF8Type",AbstractComposite.ComponentEquality.EQUAL);
//		s.addComponent(3,"",StringSerializer.get(),"UTF8Type",AbstractComposite.ComponentEquality.EQUAL);
//		s.addComponent(4,"",StringSerializer.get(),"UTF8Type",AbstractComposite.ComponentEquality.EQUAL);
//		s.addComponent(5,1,IntegerSerializer.get(),"IntegerType",AbstractComposite.ComponentEquality.EQUAL);
		Composite e = new Composite();
		e.addComponent(0,Long.parseLong("107")+1,LongSerializer.get(),"LongType",AbstractComposite.ComponentEquality.EQUAL);//To get data for a meter only
//		e.addComponent(1,Long.parseLong("1373364660")+1,LongSerializer.get(),"LongType",AbstractComposite.ComponentEquality.LESS_THAN_EQUAL);
//		e.addComponent(2,"watermeter",StringSerializer.get(),"UTF8Type",AbstractComposite.ComponentEquality.EQUAL);
//		e.addComponent(3,Character.MAX_VALUE+"",StringSerializer.get(),"UTF8Type",AbstractComposite.ComponentEquality.EQUAL);
//		e.addComponent(4,Character.MAX_VALUE+"",StringSerializer.get(),"UTF8Type",AbstractComposite.ComponentEquality.EQUAL);
//		e.addComponent(5,1,IntegerSerializer.get(),"IntegerType",AbstractComposite.ComponentEquality.GREATER_THAN_EQUAL);
		
		sliceQuery.setRange(s, e, false, 10000);
		QueryResult<ColumnSlice<Composite, String>> result = sliceQuery.execute();
	    ColumnSlice<Composite, String> cs = result.get();
		return cs;
	}
	
	private ColumnSliceIterator<String, DynamicComposite, byte[]> fetchDynamicCompositeColumns(String rowKey, String columnFamily, List<String> start, List<String> end, boolean reversed) {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		SliceQuery<String, DynamicComposite, byte[]> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new DynamicCompositeSerializer(), BytesArraySerializer.get());
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(rowKey);
		ColumnSliceIterator<String, DynamicComposite, byte[]> sliceIterator = new ColumnSliceIterator<String, DynamicComposite, byte[]>(sliceQuery, getDynamicCompositeRange(start, true), getDynamicCompositeRange(end, false), reversed);
		return sliceIterator;
	}

	public void deletDynamicCompositeColumns(String rowKey, String columnFamily, List<DynamicComposite> columnsToDelete) throws DAOExceptions {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
		for (DynamicComposite composite : columnsToDelete) {
			mutator.addDeletion(rowKey, columnFamily, composite, new DynamicCompositeSerializer());
		}
		mutator.execute();
	}
	
	
	class DynamicCompositeQueryIterator implements Iterable<HColumn<DynamicComposite,String>> {

		private final ColumnSliceIterator<String,DynamicComposite,String> sliceIterator;
		public DynamicCompositeQueryIterator(ColumnSliceIterator<String,DynamicComposite,String> sliceIterator) {
			this.sliceIterator = sliceIterator;
		}
		@Override
		public Iterator<HColumn<DynamicComposite, String>> iterator() {
			return sliceIterator;
		}
		
	}
	
	class CompositeQueryIterator implements Iterable<HColumn<Composite,String>> {
		
		private final ColumnSliceIterator<String,Composite,String> sliceIterator;
		public CompositeQueryIterator(ColumnSliceIterator<String,Composite,String> sliceIterator) {
			this.sliceIterator = sliceIterator;
		}
		@Override
		public Iterator<HColumn<Composite, String>> iterator() {
			return sliceIterator;
		}
		
	}

	/*public void insertDynamicCompositeColumns(String rowKey, String columnFamily, List<HColumn<DynamicComposite, ?>> compositeColumns) {
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
		for (HColumn<?,?> hColumn : compositeColumns) {
			mutator.addInsertion(rowKey, columnFamily, hColumn);
		}
		mutator.execute();
	}*/

	private DynamicComposite getDynamicCompositeRange(List<String> columnNames, boolean range){

		DynamicComposite rangeColumns;
		if(columnNames!=null){
			rangeColumns = new DynamicComposite();
			for (int i = 0; i < columnNames.size() - 1; i++) {
				rangeColumns.addComponent(i, columnNames.get(i), AbstractComposite.ComponentEquality.EQUAL);
			}
			if (!range) {
				rangeColumns.addComponent(columnNames.size() - 1, columnNames.get(columnNames.size() - 1), AbstractComposite.ComponentEquality.GREATER_THAN_EQUAL);
			} else {
				rangeColumns.addComponent(columnNames.size() - 1, columnNames.get(columnNames.size() - 1), AbstractComposite.ComponentEquality.EQUAL);
			}
		}else{
			if (!range) {
				rangeColumns = new DynamicComposite("",AbstractComposite.ComponentEquality.GREATER_THAN_EQUAL);
			}else{
				rangeColumns = new DynamicComposite("",AbstractComposite.ComponentEquality.EQUAL);
			}
		}
		return rangeColumns;
	}
	
	private Composite getCompositeRange(List<String> columnNames, boolean range){
		
		Composite rangeColumns;
		if(columnNames!=null){
			rangeColumns = new Composite();
			for (int i = 0; i < columnNames.size() - 1; i++) {
				rangeColumns.addComponent(i, columnNames.get(i), AbstractComposite.ComponentEquality.EQUAL);
			}
			if (!range) {
				rangeColumns.addComponent(columnNames.size() - 1, columnNames.get(columnNames.size() - 1), AbstractComposite.ComponentEquality.GREATER_THAN_EQUAL);
			} else {
				rangeColumns.addComponent(columnNames.size() - 1, columnNames.get(columnNames.size() - 1), AbstractComposite.ComponentEquality.EQUAL);
			}
		}else{
			if (!range) {
				rangeColumns = new Composite("",AbstractComposite.ComponentEquality.GREATER_THAN_EQUAL);
			}else{
				rangeColumns = new Composite("",AbstractComposite.ComponentEquality.EQUAL);
			}
		}
		return rangeColumns;
	}


	public void deleteColumnByCompositeColumns(String columnFamily, String key, List<String> columnNames) throws DAOExceptions {
		if (logger.isDebugEnabled()) {
			logger.debug("CassandraClient.deleteColumnByCompositeColumns column family {} key {} columns {}", new Object[]{columnFamily, key, columnNames});
		}
		CassandraConnection connection = getConnection("one");
		Keyspace keyspaceObj = connection.getKeyspace();
		SliceQuery<String, DynamicComposite, String> sliceQuery = HFactory.createSliceQuery(keyspaceObj, StringSerializer.get(), new DynamicCompositeSerializer(), StringSerializer.get());
		sliceQuery.setColumnFamily(columnFamily);
		sliceQuery.setKey(key);
		sliceQuery.setRange(getDynamicCompositeRange(columnNames, true), getDynamicCompositeRange(columnNames, false), false, Integer.MAX_VALUE);


		try {
			QueryResult<ColumnSlice<DynamicComposite, String>> result = sliceQuery.execute();
			ColumnSlice<DynamicComposite, String> columnSlice = result.get();
			Mutator<String> mutator = HFactory.createMutator(keyspaceObj, StringSerializer.get());
			for (HColumn<DynamicComposite, String> hColumn : columnSlice.getColumns()) {
				DynamicCompositeSerializer ce = new DynamicCompositeSerializer();
				mutator.addDeletion(key, columnFamily, hColumn.getName(), ce);
			}
			mutator.execute();
		} catch (HectorException e) {

			throw new DAOExceptions("CassandraClient.deleteColumnByCompositeColumns : Error in deleting Composite Columns",e);
		} finally {

		}
	}

	private DynamicComposite getCompositeColumnsString(List<?> compositeColumnNames) {
		DynamicComposite composite = new DynamicComposite();
		for (Object compositeColumnName : compositeColumnNames) {
			composite.addComponent(String.valueOf(compositeColumnName), StringSerializer.get());
		}
		return composite;
	}
	
	private Composite getCompositeColumns(List<Object> compositeColumnNames) {
//TODO Column name should not have ":"
		Composite composite = new Composite();
		for (Object compositeColumnName : compositeColumnNames) {
				composite.add(compositeColumnName);
		}
		return composite;
	}

	private Composite createCompositeColumn(Map<?,DataType> colMap){
		Composite compCol = new Composite();
		for (Entry<?, DataType> col : colMap.entrySet()) {
			if(col.getValue().equals(DataType.StringType)){
				compCol.addComponent(String.valueOf(col.getKey()), StringSerializer.get());
			}else if(col.getValue().equals(DataType.LongType)){
				compCol.addComponent(Long.valueOf(String.valueOf(col.getKey())), LongSerializer.get());
			}else if(col.getValue().equals(DataType.DoubleType)){
				compCol.addComponent(Double.valueOf(String.valueOf(col.getKey())), DoubleSerializer.get());
			}
		}
		
		return null;
	}
	
	public void populateComposietColumnsForSlice(LinkedHashMap<String, String> compositeColumnDetails, DynamicComposite start, DynamicComposite end) {
		Set<Entry<String, String>> entrySet = compositeColumnDetails.entrySet();
		int i = 0;
		for (Entry<String, String> entry : entrySet) {
			start.addComponent(i, entry.getKey(), DynamicComposite.ComponentEquality.EQUAL);
			end.addComponent(i, entry.getValue(), i == entrySet.size() - 1 ? DynamicComposite.ComponentEquality.GREATER_THAN_EQUAL : DynamicComposite.ComponentEquality.EQUAL);
			i++;
		}
	}

}

