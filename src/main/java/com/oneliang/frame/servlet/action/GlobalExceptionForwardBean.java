package com.oneliang.frame.servlet.action;

public class GlobalExceptionForwardBean{

	public static final String TAG_GLOBAL_EXCEPTION_FORWARD="global-exception-forward";
	
	private String path=null;
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
