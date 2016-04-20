package com.oneliang.frame.servlet.action;


public class InterceptorBean {

	public static final String TAG_INTERCEPTOR="interceptor";

	private String id=null;
	private String type=null;
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
