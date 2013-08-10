package com.smartrhomes.greendata.connection;

import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class LocalQuorumConsistencyLevelPolicy implements ConsistencyLevelPolicy {

    private static LocalQuorumConsistencyLevelPolicy instance = new LocalQuorumConsistencyLevelPolicy();

    public static ConsistencyLevelPolicy getInstance(){
        return instance;
    }

    @Override
    public HConsistencyLevel get(OperationType op) {
        return HConsistencyLevel.LOCAL_QUORUM;
    }

    @Override
    public HConsistencyLevel get(OperationType op, String cfName) {
        return HConsistencyLevel.LOCAL_QUORUM;
    }
}