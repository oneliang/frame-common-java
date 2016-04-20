package com.oneliang.frame.jdbc;

public abstract interface Transaction {

	/**
	 * transaction execute
	 * @throws QueryException
	 */
	public abstract void execute() throws QueryException;
}
