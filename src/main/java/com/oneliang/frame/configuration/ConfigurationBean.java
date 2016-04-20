package com.oneliang.frame.configuration;

import com.oneliang.frame.Context;


public class ConfigurationBean{

	public static final String TAG_CONFIGURATION="configuration";
	
	private String id=null;
	private String contextClass=null;
	private String parameters=null;
	private Context contextInstance=null;
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
	 * @return the contextClass
	 */
	public String getContextClass() {
		return contextClass;
	}
	/**
	 * @param contextClass the contextClass to set
	 */
	public void setContextClass(String contextClass) {
		this.contextClass = contextClass;
	}
	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the contextInstance
	 */
	public Context getContextInstance() {
		return contextInstance;
	}
	/**
	 * @param contextInstance the contextInstance to set
	 */
	public void setContextInstance(Context contextInstance) {
		this.contextInstance = contextInstance;
	}
}
