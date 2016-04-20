package com.oneliang.frame.ioc;

public class IocAfterInjectBean{

	public static final String TAG_AFTER_INJECT="after-inject";
	
	private String method=null;

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
}
