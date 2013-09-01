package com.smartrhomes.greendata.connection;

import java.util.ArrayList;
import java.util.List;

public class CassandraOpenConnection {
	
	private static final String CONFIGNAME = "Test Cluster";
	private static final String KEYSPACE = "Greendata";
	private static final int MAX_ACTIVE_CONNECTION = 10;
	private static final String HOST = "localhost";
	private static final int PORT = 9160;
	
	private CassandraOpenConnection() {
	}
	
	
	/**
	 *Need to put at the time of boot up the system And as if new host need to be configured. 
	 */
	public static ConnectionPool<CassandraConnection> openFor(String host,int port, String cluster, String keySpace){
		String h = HOST;
		int p = PORT;
		String cname = CONFIGNAME;
		String k = KEYSPACE;
		if(host!=null){
			h = host;
		}
		if(port>0){
			p = port;
		}
		if(cluster!=null){
			cname = cluster;
		}
		if(keySpace!=null){
			k = keySpace;
		}
		
		DataStoreNodeConfig node = new DataStoreNodeConfig();
		node.setHost(h+":"+p);
		List<DataStoreNodeConfig> nodeConfig = new ArrayList<DataStoreNodeConfig>();
		nodeConfig.add(node);
		DataStoreConfig dataStoreConfig = new DataStoreConfig(cname,null,k,nodeConfig,ConsistencyLevel.one);
		dataStoreConfig.setMaxActiveConnections(MAX_ACTIVE_CONNECTION);
		return CassandraConnectionPool.getInstance(dataStoreConfig, true);
	}
}
