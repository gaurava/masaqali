package com.smartrhomes.greendata.connection;

import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class EachQuorumConsistencyLevelPolicy implements ConsistencyLevelPolicy {

    private static EachQuorumConsistencyLevelPolicy instance = new EachQuorumConsistencyLevelPolicy();

    public static ConsistencyLevelPolicy getInstance(){
        return instance;
    }

    @Override
    public HConsistencyLevel get(OperationType op) {
        return HConsistencyLevel.EACH_QUORUM;
    }

    @Override
    public HConsistencyLevel get(OperationType op, String cfName) {
        return HConsistencyLevel.EACH_QUORUM;
    }
}