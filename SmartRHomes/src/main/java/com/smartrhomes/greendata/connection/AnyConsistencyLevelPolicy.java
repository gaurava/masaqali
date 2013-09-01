package com.smartrhomes.greendata.connection;

import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class AnyConsistencyLevelPolicy implements ConsistencyLevelPolicy {

    private static AnyConsistencyLevelPolicy instance = new AnyConsistencyLevelPolicy();

    public static ConsistencyLevelPolicy getInstance(){
        return instance;
    }

    @Override
    public HConsistencyLevel get(OperationType op) {
        return HConsistencyLevel.ANY;
    }

    @Override
    public HConsistencyLevel get(OperationType op, String cfName) {
        return HConsistencyLevel.ANY;
    }
}