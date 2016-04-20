package com.oneliang.frame.servlet.action;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract interface Interceptor{
	/**
	 * through intercept return true,else return false
	 * @param request
	 * @param response
	 * @return boolean
	 * @exception InterceptException
	 */
	public abstract boolean doIntercept(ServletRequest request,ServletResponse response) throws InterceptException;

	public static class InterceptException extends Exception {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 3607915711758608642L;

		/**
		 * @param message
		 */
		public InterceptException(String message){
			super(message);
		}

		/**
		 * @param cause
		 */
		public InterceptException(Throwable cause) {
			super(cause);
		}

		/**
		 * @param message
		 * @param cause
		 */
		public InterceptException(String message,Throwable cause){
			super(message,cause);
		}
	}
}
