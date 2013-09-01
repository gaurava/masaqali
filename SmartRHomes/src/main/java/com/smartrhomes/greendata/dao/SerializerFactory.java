package com.smartrhomes.greendata.dao;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.ddl.ComparatorType;

public class SerializerFactory<T> implements Serializer<T> {

	@Override
	public ByteBuffer toByteBuffer(T obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes(T obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T fromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T fromByteBuffer(ByteBuffer byteBuffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ByteBuffer> toBytesSet(List<T> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> fromBytesSet(Set<ByteBuffer> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> Map<ByteBuffer, V> toBytesMap(Map<T, V> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> Map<T, V> fromBytesMap(Map<ByteBuffer, V> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ByteBuffer> toBytesList(List<T> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> fromBytesList(List<ByteBuffer> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComparatorType getComparatorType() {
		// TODO Auto-generated method stub
		return null;
	}

}
