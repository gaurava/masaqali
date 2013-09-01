package com.smartrhomes.greendata.connection;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.ConnectionPoolDataSource;

import me.prettyprint.cassandra.connection.HClientPool;
import me.prettyprint.cassandra.model.AllOneConsistencyLevelPolicy;
import me.prettyprint.cassandra.model.QuorumAllConsistencyLevelPolicy;
import me.prettyprint.cassandra.service.CassandraHost;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartrhomes.greendata.dao.ErrorCodes;
import com.smartrhomes.greendata.exceptions.DAOExceptions;


/**
 * @author "Gaurava Srivastava"
 *
 */
public class CassandraConnectionPool implements ConnectionPool<CassandraConnection> {

    private static Map<String, CassandraConnectionPool> connectionPoolMap = new HashMap<String, CassandraConnectionPool>();

    public static final int MAX_ACTIVE_CLIENTS = 10;
    public static final String CASSSANDRA_MAX_CONNECT_TIME_IN_MILLIS = "casssandra.maxConnectTimeInMillis";

    private Cluster cluster = null;
    private Keyspace keyspace = null;
    private DataStoreConfig dataStoreConfig;
    private static final Logger logger = LoggerFactory.getLogger(CassandraConnectionPool.class.getName());


    public static CassandraConnectionPool getInstance(String configName) {
        if (connectionPoolMap.containsKey(configName)) {
            return connectionPoolMap.get(configName);
        }
        throw new DAOExceptions(ErrorCodes.DATASTORECONFIG_DOESNT_EXISTS, configName);
    }

    public static boolean containsInstance(String configName) {
        return connectionPoolMap.containsKey(configName);
    }
    
    public static boolean isConnectionPoolAvailable(String configName) {
    	CassandraConnectionPool cassandraConnectionPool = connectionPoolMap.get(configName);
    	Collection<HClientPool> activePools =null;
    	if(cassandraConnectionPool!=null){
    		activePools = cassandraConnectionPool.getCluster().getConnectionManager().getActivePools();
    	}
    	if(activePools==null || activePools.isEmpty()){
    		return false;
    	}
        return true;
    }
    
    private Cluster getCluster() {
		return cluster;
	}

	public static void putInstance(String configName,CassandraConnectionPool connectionPool) {
        if (logger.isDebugEnabled()) {
            logger.debug("CassandraConnectionPool.putInstance() : Added datastoreconfig:{}",configName);
        }
        connectionPoolMap.put(configName,connectionPool);
    }

    public static CassandraConnectionPool getInstance(DataStoreConfig config, boolean createIfNotFound) {
        String configName = config.getConfigName();
        if (connectionPoolMap.containsKey(configName)) {
            return connectionPoolMap.get(configName);
        } else {
            if (createIfNotFound) {
                if (logger.isDebugEnabled()) {
                    logger.debug("CassandraConnectionPool.getInstance() : Creating connectionpool for datastoreconfig:{}; pool size is {}",new Object[]{configName,connectionPoolMap.size()});
                }
                synchronized (CassandraConnectionPool.class) {
                    if (!connectionPoolMap.containsKey(configName)) {
                        CassandraConnectionPool cassandraConnectionPool = new CassandraConnectionPool(config);
                        connectionPoolMap.put(configName, cassandraConnectionPool);
                    }
                }
                return connectionPoolMap.get(configName);
            }
            return null;
        }
    }

    public DataStoreConfig getDataStoreConfig() {
        return dataStoreConfig;
    }

    private CassandraConnectionPool(DataStoreConfig config) {
        List<DataStoreNodeConfig> dataStoreNodeConfigs = config.getDataStoreNodeConfigs();
        StringBuilder clusterBuilder = new StringBuilder();
        String clusterName = config.getConfigName();
        for (DataStoreNodeConfig dataStoreNodeConfig : dataStoreNodeConfigs) {
            if (clusterBuilder.length() > 0) {
                clusterBuilder.append(",");
            }
            clusterBuilder.append(dataStoreNodeConfig.getHost());
        }
        this.dataStoreConfig = config;
        CassandraHostConfigurator configurator = new CassandraHostConfigurator(clusterBuilder.toString());
        String propertyValue = "1000";//CASSSANDRA_MAX_CONNECT_TIME_IN_MILLIS
        if(propertyValue!=null && !propertyValue.isEmpty()){
            long maxConnectTimeMillis = -1;
            try{
                maxConnectTimeMillis = Long.parseLong(propertyValue);
            }catch(NumberFormatException excep){
               logger.error("Invalid long value for 'casssandra.maxConnectTimeInMillis' in system.properties");  
            }
            if(maxConnectTimeMillis>0){
                if(logger.isDebugEnabled()){
                  logger.debug("Setting MaxConnectTimeMillis for config {} as {}",new Object[]{config,maxConnectTimeMillis});
                }
//                configurator.setMaxConnectTimeMillis(maxConnectTimeMillis);  
            }
        }
       
        int maxActiveConnections = 0;
        if(config.getMaxActiveConnections()<=0){
          logger.warn("Setting MaxActiveConnections as {} instead of {} for keyspace {}", new Object[]{MAX_ACTIVE_CLIENTS,config.getMaxActiveConnections(),config.getKeyspace()});
          maxActiveConnections = MAX_ACTIVE_CLIENTS;
        }else{
            maxActiveConnections = config.getMaxActiveConnections();
        }
        logger.debug("MaxActiveConnections for keyspace {} is {}", new Object[]{config.getKeyspace(),config.getMaxActiveConnections()});
        configurator.setMaxActive(maxActiveConnections);
//        configurator.setMaxIdle(MAX_IDLE_CLIENTS);
        this.cluster = HFactory.getOrCreateCluster(clusterName, configurator);
    }

    @Override
    public CassandraConnection getConnection() throws DAOExceptions {
        if (this.keyspace == null) {
            String keySpaceName = this.dataStoreConfig.getKeyspace();
            KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(keySpaceName);
            if (keyspaceDef == null) {
                throw new DAOExceptions("Keyspace " + keySpaceName + " does not exist");
            }
            ConsistencyLevelPolicy policy = null;
            switch  (dataStoreConfig.getConsistencyLevel()) {
            case localquorum:
            	policy = LocalQuorumConsistencyLevelPolicy.getInstance();
            	break;
            case eachquorum:
            	policy = EachQuorumConsistencyLevelPolicy.getInstance();
            	break;
            case one:
            	policy = new AllOneConsistencyLevelPolicy();
            	break;
            case all:
            	policy = AllConsistencyLevelPolicy.getInstance();
            	break;
            case any:
            	policy = AnyConsistencyLevelPolicy.getInstance();
            	break;
            default :
            	policy =  new QuorumAllConsistencyLevelPolicy();
            }
            if(logger.isDebugEnabled()){
              logger.debug("Consistency policy for keyspace:{} is {}",new Object[]{keyspaceDef ,policy.toString()});
            }
            this.keyspace = HFactory.createKeyspace(keySpaceName, cluster,policy);
        }
        return new CassandraConnection(this.keyspace);
    }

    @Override
    public void addHost(String configName, DataStoreNodeConfig nodeConfig) throws DAOExceptions {
        synchronized (CassandraConnectionPool.class) {
            if (logger.isDebugEnabled()) {
                logger.debug("CassandraConnectionPool.addHost() : add cassandra host to connection pool for keymanagement", nodeConfig.toString());
            }
            CassandraConnectionPool existingConnectionPool = connectionPoolMap.get(configName);
            Set<CassandraHost> suspendedCassandraHosts = existingConnectionPool.cluster.getConnectionManager().getSuspendedCassandraHosts();
            if (suspendedCassandraHosts != null && suspendedCassandraHosts.contains(new CassandraHost(nodeConfig.getHost()))) {
                existingConnectionPool.cluster.getConnectionManager().unsuspendCassandraHost(new CassandraHost(nodeConfig.getHost()));
            } else {
                existingConnectionPool.cluster.getConnectionManager().addCassandraHost(new CassandraHost(nodeConfig.getHost()));
            }
            existingConnectionPool.getDataStoreConfig().addDataStoreNodeConfig(nodeConfig);
        }
    }

    @Override
    public void removeHost(String configName, DataStoreNodeConfig nodeConfig) throws DAOExceptions {
        synchronized (CassandraConnectionPool.class) {
            if (logger.isDebugEnabled()) {
                logger.debug("CassandraConnectionPool.addHost() : remove cassandra host to connection pool for keymanagement", nodeConfig.toString());
            }
            CassandraConnectionPool existingConnectionPool = connectionPoolMap.get(configName);
            existingConnectionPool.cluster.getConnectionManager().suspendCassandraHost(new CassandraHost(nodeConfig.getHost()));
            existingConnectionPool.getDataStoreConfig().removeDataStoreNodeConfig(nodeConfig);
        }
    }

    @Override
    public void shutDown() {
        connectionPoolMap.remove(dataStoreConfig.getConfigName());
        if(connectionPoolMap.values().contains(this)){
            if (logger.isDebugEnabled()) {
                logger.debug("CassandraConnectionPool.shutDown() : Multiple pods share connectionpool of datastoreconfig:{},Ignoring shutdown,entries alone removedﬂ", dataStoreConfig.getConfigName());
            }
        }else{
            if (logger.isDebugEnabled()) {
                logger.debug("CassandraConnectionPool.shutDown() : ConnectionPool shutdown for datastoreconfig:{}", dataStoreConfig.getConfigName());
            }
            if (cluster != null) {
                HFactory.shutdownCluster(cluster);
            }
        }


    }

}

