package com.smartrhomes.greendata.connection;

import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class AllConsistencyLevelPolicy implements ConsistencyLevelPolicy {

    private static AllConsistencyLevelPolicy instance = new AllConsistencyLevelPolicy();

    public static ConsistencyLevelPolicy getInstance(){
        return instance;
    }

    @Override
    public HConsistencyLevel get(OperationType op) {
        return HConsistencyLevel.ALL;
    }

    @Override
    public HConsistencyLevel get(OperationType op, String cfName) {
        return HConsistencyLevel.ALL;
    }
}