package com.oneliang.frame.jdbc;

public class QueryException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8151778761557454636L;

	/**
	 * @param message
	 */
	public QueryException(String message){
		super(message);
	}

	/**
	 * @param cause
	 */
	public QueryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public QueryException(String message,Throwable cause){
		super(message,cause);
	}
}
