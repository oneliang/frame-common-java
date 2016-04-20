package com.oneliang.frame.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * pagination query
 * @author Dandelion
 * @since 2008-11-25
 */
public abstract interface PaginationQuery extends Serializable{
	
	public <T extends Object> List<T> executeQueryLimit(Connection connection,Class<T> clazz);
}
