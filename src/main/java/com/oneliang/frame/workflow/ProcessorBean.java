package com.oneliang.frame.workflow;


public class ProcessorBean{

	public static final String TAG_PROCESSOR="processor";
	
	private String id=null;
	private String type=null;
	private Processor processorInstance=null;
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
	 * @return the processorInstance
	 */
	public Processor getProcessorInstance() {
		return processorInstance;
	}
	/**
	 * @param processorInstance the processorInstance to set
	 */
	public void setProcessorInstance(Processor processorInstance) {
		this.processorInstance = processorInstance;
	}
}
