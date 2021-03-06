package com.gracebrother.sharding.plugin;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.gracebrother.sharding.builder.ShardHolder;
import com.gracebrother.sharding.builder.ShardObject;
import com.gracebrother.sharding.builder.ShardParser;
import com.gracebrother.sharding.utils.ReflectionUtils;

/**
 * sharding plugin
 * @author bin
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class ShardPlugin implements Interceptor {

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MappedStatement mappedStatement = null;
		if (statementHandler instanceof RoutingStatementHandler) {
			StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
			mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, "mappedStatement");
		} else {
			mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(statementHandler, "mappedStatement");
		}
		Object parm = statementHandler.getParameterHandler().getParameterObject();
		String sql = statementHandler.getBoundSql().getSql();
		String mapperId = mappedStatement.getId();
		if (ShardHolder.getInstance().hasShard(mapperId)) {
			ShardObject shardObject = ShardHolder.getInstance().getShardObject(mapperId);
			String targetSql = ShardParser.getInstance().parse(sql, parm, shardObject);
			if (!sql.equals(targetSql)) {
				ReflectionUtils.setFieldValue(statementHandler.getBoundSql(), "sql", targetSql);
			}
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}

}
