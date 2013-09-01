package com.smartrhomes.greendata.connection;

import com.smartrhomes.greendata.exceptions.DAOExceptions;

public interface ConnectionPool<T> {

	CassandraConnection getConnection() throws DAOExceptions;

	void addHost(String configName, DataStoreNodeConfig nodeConfig)
			throws DAOExceptions;

	void removeHost(String configName, DataStoreNodeConfig nodeConfig)
			throws DAOExceptions;

	void shutDown();

	
}
