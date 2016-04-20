package com.oneliang.frame.servlet.action;

public class ActionExecuteException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9142444524975582593L;

	/**
	 * @param message
	 */
	public ActionExecuteException(String message){
		super(message);
	}

	/**
	 * @param cause
	 */
	public ActionExecuteException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ActionExecuteException(String message,Throwable cause){
		super(message,cause);
	}
}
