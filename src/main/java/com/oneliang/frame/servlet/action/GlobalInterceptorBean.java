package com.oneliang.frame.servlet.action;


public class GlobalInterceptorBean{

	public static final String TAG_GLOBAL_INTERCEPTOR="global-interceptor";
	
	public static final String INTERCEPTOR_MODE_BEFORE="before";
	public static final String INTERCEPTOR_MODE_AFTER="after";

	private String id=null;
	private String type=null;
	private String mode=null;
	private Interceptor interceptorInstance=null;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the interceptorInstance
	 */
	public Interceptor getInterceptorInstance() {
		return interceptorInstance;
	}
	/**
	 * @param interceptorInstance the interceptorInstance to set
	 */
	public void setInterceptorInstance(Interceptor interceptorInstance) {
		this.interceptorInstance = interceptorInstance;
	}
}
