package com.smartrhomes.greendata.dao;

public interface ErrorCodes {

	    String NAMESPACE = "datastore.";
	    String DATASTORECONFIG_DOESNT_EXISTS = NAMESPACE + "DataStoreConfigDoesntExists";
	    String EMPTY_CLUSTER = NAMESPACE + "DataStoreClusterIsEmpty";
	    String ERROR_WHILE_ACCESSING_DATASTORE = NAMESPACE + "ErrorWhileAccessingDataStore";
}
