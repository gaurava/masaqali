package com.smartrhomes.greendata.dao;

import java.util.List;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import com.smartrhomes.greendata.exceptions.DAOExceptions;

public class DaoImpl<K, N, V> {

/*	private static final Map<Class, Serializer> serializers = new HashMap<Class, Serializer>();
	static{
		serializers.put(String.class, StringSerializer.get());
		serializers.put(Long.class, LongSerializer.get());
		serializers.put(byte[].class, BytesArraySerializer.get());
	}*/

	/*@Override
	public void insertData(K rowKey, N columnName, V columnValue) throws DAOExceptions 
	{
		Cluster cluster = HFactory.getOrCreateCluster("Test Cluster",new CassandraHostConfigurator("localhost:9160"));
		Keyspace keyspace = HFactory.createKeyspace("gaurava", cluster);
		String cfName = "start";
		
		HColumn<N, V> hColumn = HFactory.createColumn(columnName, columnValue);
		
		Mutator<K> mutator = (Mutator<K>) HFactory.createMutator(keyspace, SerializerTypeInferer.getSerializer(rowKey));
		mutator.insert(rowKey, cfName, hColumn);
		mutator.execute();
	
	}

	@Override
	public void insertData(K rowKey, List<DataMap<N, V>> columnValues) throws DAOExceptions 
	{
		Cluster cluster = HFactory.getOrCreateCluster("Test Cluster",new CassandraHostConfigurator("localhost:9160"));
		Keyspace keyspace = HFactory.createKeyspace("gaurava", cluster);
		String cfName = "start";
		
		HColumn<N,V> hColumn = null;
		Mutator<K> mutator = (Mutator<K>) HFactory.createMutator(keyspace, SerializerTypeInferer.getSerializer(rowKey));
		N colName = null;
		V colValue = null;
		for (DataMap<N, V> dataMap : columnValues) {
			colName = dataMap.getColName();
			colValue = dataMap.getColValue();
			hColumn = (HColumn<N, V>) HFactory.createColumn(colName,colValue, SerializerTypeInferer.getSerializer(colName), SerializerTypeInferer.getSerializer(colValue));
			mutator.addInsertion(rowKey, cfName, hColumn);
		}
		
		mutator.execute();
	}
*/
	
	
}
