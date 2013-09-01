package com.smartrhomes.greendata.connection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataStoreConfig {

    public enum StoreType {MySql, Cassandra};

    private String configName;
    private StoreType type;
    private String keyspace;
    private ConsistencyLevel consistencyLevel;
    private int maxActiveConnections;
    private List<DataStoreNodeConfig> dataStoreNodeConfigs = new CopyOnWriteArrayList<DataStoreNodeConfig>();
    private static final Logger logger = LoggerFactory.getLogger(DataStoreConfig.class.getName());


    public DataStoreConfig() {
    }

    public DataStoreConfig(String configName, StoreType type,String keyspace, List<DataStoreNodeConfig> dataStoreNodeConfigs,ConsistencyLevel consistencyLevel) {
        this.configName = configName;
        this.type = type;
        this.keyspace = keyspace;
        this.dataStoreNodeConfigs = dataStoreNodeConfigs;
        this.consistencyLevel = consistencyLevel;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public StoreType getType() {
        return type;
    }

    public void setType(StoreType type) {
        this.type = type;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public List<DataStoreNodeConfig> getDataStoreNodeConfigs() {
        return dataStoreNodeConfigs;
    }
    
    public void addDataStoreNodeConfig(DataStoreNodeConfig nodeConfig) {
        dataStoreNodeConfigs.add(nodeConfig);
    }

    public void removeDataStoreNodeConfig(DataStoreNodeConfig nodeConfig) {
        dataStoreNodeConfigs.remove(nodeConfig);
    }

    public ConsistencyLevel getConsistencyLevel() {
        return consistencyLevel;
    }

    public void setConsistencyLevel(ConsistencyLevel consistencyLevel) {
        this.consistencyLevel = consistencyLevel;
    }

    public static ConsistencyLevel getConsistencyLevel(String consistencyLevelString,String keyspace){
        if(consistencyLevelString==null){
            if (logger.isDebugEnabled()) {
                logger.debug("CassandraDataStore.init() : 'consistencylevel' is null for"+keyspace + " Defaulting to localquorum");
            }
            return ConsistencyLevel.localquorum;
        }else{
            if (logger.isDebugEnabled()) {
                logger.debug("CassandraDataStore.init() : 'consistencylevel' in audit.properties is "+consistencyLevelString);
            }
            return ConsistencyLevel.valueOf(consistencyLevelString);
        }
    }

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(int maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    @Override
    public String toString() {
        return "DataStoreConfig{" +
                "name='" + configName + '\'' +
                ", type=" + type +
                ", keyspace='" + keyspace + '\'' +
                ", consistencyLevel=" + consistencyLevel +
                ", maxActiveConnections=" + maxActiveConnections +
                ", dataStoreNodeConfigs=" + dataStoreNodeConfigs +
                '}';
    }
}
