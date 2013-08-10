package com.smartrhomes.greendata.connection;

import me.prettyprint.hector.api.Keyspace;

public class CassandraConnection {


	private Keyspace keyspace;
	
	public CassandraConnection(Keyspace keyspace) {
		this.setKeyspace(keyspace);
	}

	public Keyspace getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(Keyspace keyspace) {
		this.keyspace = keyspace;
	}
	
}
