package com.oneliang.frame.jdbc;

import java.sql.Connection;

public final class TransactionManager {

	static final ThreadLocal<Boolean> customTransactionSign=new ThreadLocal<Boolean>();
	static final ThreadLocal<Connection> customTransactionConnection=new ThreadLocal<Connection>();
}
