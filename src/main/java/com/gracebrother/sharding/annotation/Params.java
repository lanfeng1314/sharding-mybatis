package com.gracebrother.sharding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gracebrother.sharding.shard.Strategy;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Params {
	/**
	 * all tables will be sharding in sql
	 */
	public String[] tables();
	/**
	 * Must Implements Strategy
	 */
	public Class<? extends Strategy> strategy();
	/**
	 * nameSpace.method eg:com.gracebrother.user.dao.insert
	 * default value is the name of the dao append "." append the method name
	 */
	public String mapperId() default "";
}
