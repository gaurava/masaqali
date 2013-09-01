package com.smartrhomes.greendata.dao;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.smartrhomes.greendata.exceptions.DAOExceptions;

/**
 * @author "Gaurava Srivastava"
 *
 */
public interface CassandraClient {

	/**
	 * @param columnFamily
	 * @param rowKey
	 * @param columns
	 * @param dataType
	 */
	void insertMultiDataTypeColumns(String columnFamily, String rowKey,
			Collection<Column<?>> columns, DataType dataType) throws DAOExceptions ;

	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames
	 * @return
	 */
	Map<String, ByteBuffer> getMultiDataTypeColumns(String columnFamily,
			String key, List<String> columnNames) throws DAOExceptions ;
	/**
	 * @param columnFamilyName
	 * @param rowkey
	 * @param columnNamesVsValues
	 */
	void multiInsert(String columnFamilyName, String rowkey,
			Map<String, String> columnNamesVsValues) throws DAOExceptions ;
	/**
	 * @param columnFamily
	 * @param rows
	 */
	void multiInsert(String columnFamily, Map<String, Map<String, String>> rows);
	Map<String, Map<String, String>> multiGet(String columnFamily,
			Collection<String> keysList) throws DAOExceptions ;
	/**
	 * @param columnFamily
	 * @param key
	 * @return
	 */
	List<String> getColumnNames(String columnFamily, String key) throws DAOExceptions ;
	/**
	 * @param columnFamily
	 * @param key
	 * @param noOfColumns
	 * @return
	 */
	List<String> getColumnNames(String columnFamily, String key, int noOfColumns) throws DAOExceptions ;
	void deleteColumns(String columnFamily, String key,
			Collection<String> columnNames) throws DAOExceptions ;
	void multiDelete(String columnFamily, Collection<String> keysList) throws DAOExceptions ;
	void deleteMultipleRows(String columnFamilyName, List<String> rowkeys) throws DAOExceptions ;
	void multiDeleteColumnsInASingleRow(String columnFamilyName, String rowkey,
			Collection<String> columnNames) throws DAOExceptions ;
	
	/**
	 * @param columnFamily
	 * @param key
	 * @param columnNames
	 * @param columnValues
	 */
	void insertDynamicComposite(String columnFamily, String key,
			List<?> columnNames, String columnValues) throws DAOExceptions ;
	List<String> getColumnValueByCompositeColumns(String columnFamily,
			String key, List<String> columnNames, List<Long> timeRange, int rows) throws DAOExceptions ;
	
	/**
	 * @param rowKey
	 * @param columnFamily
	 * @param start
	 * @param end
	 * @param reversed
	 * @return
	 */
	Map<String, String> getDynamicCompositeColumns(String rowKey,
			String columnFamily, List<String> start, List<String> end,
			boolean reversed) throws DAOExceptions ;

	/*void insertCompositeColumn(String columnFamily, String key,
			List<?> columnNames, String columnValues) throws DAOExceptions;*/

	void insertCompositeColumn(String columnFamily, List<CompositeColumns> cols)
			throws DAOExceptions;

	Map<String,List> getCompositeColumns(String rowKey, String columnFamily,
			List<String> start, List<String> end, boolean reversed)
			throws DAOExceptions;

	/**
	 * @param rowKey
	 * @param columnFamily
	 * @param start
	 * @param end
	 * @param reversed
	 * @return
	 * @throws DAOExceptions
	 */
	List<CompositeColumns> getCompositeColumnsByIndex(String rowKey,
			String columnFamily, String start, String end, boolean reversed)
			throws DAOExceptions;

	
	
}
