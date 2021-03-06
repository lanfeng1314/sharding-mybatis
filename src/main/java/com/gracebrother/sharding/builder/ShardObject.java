package com.gracebrother.sharding.builder;

public class ShardObject {
	private String mapperId;
	private String[] tables;
	private Class<?> strategy;
	public String[] getTables() {
		return tables;
	}
	public void setTables(String[] tables) {
		this.tables = tables;
	}
	public String getMapperId() {
		return mapperId;
	}
	public void setMapperId(String mapperId) {
		this.mapperId = mapperId;
	}
	public Class<?> getStrategy() {
		return strategy;
	}
	public void setStrategy(Class<?> strategy) {
		this.strategy = strategy;
	}
	
}
