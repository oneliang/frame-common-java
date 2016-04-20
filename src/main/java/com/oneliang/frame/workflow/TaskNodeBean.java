package com.oneliang.frame.workflow;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class TaskNodeBean{

	public static final String TAG_TASKNODE="taskNode";
	
	private String id=null;
	private String name=null;
	private String command=null;
	private String processorId=null;
	private boolean isRoot=false;
	private TaskNode taskNodeInstance=null;
	private List<NextTaskNodeBean> nextTaskNodeBeanList=new CopyOnWriteArrayList<NextTaskNodeBean>();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * @return the processorId
	 */
	public String getProcessorId() {
		return processorId;
	}
	/**
	 * @param processorId the processorId to set
	 */
	public void setProcessorId(String processorId) {
		this.processorId = processorId;
	}
	/**
	 * @return the isRoot
	 */
	public boolean getIsRoot() {
		return isRoot;
	}
	/**
	 * @param isRoot the isRoot to set
	 */
	public void setIsRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	/**
	 * @return the taskNodeInstance
	 */
	public TaskNode getTaskNodeInstance() {
		return taskNodeInstance;
	}
	/**
	 * @param taskNodeInstance the taskNodeInstance to set
	 */
	public void setTaskNodeInstance(TaskNode taskNodeInstance) {
		this.taskNodeInstance = taskNodeInstance;
	}
	/**
	 * @param nextTaskNodeBean
	 * @return boolean
	 */
	public boolean addNextTaskNodeBean(NextTaskNodeBean nextTaskNodeBean){
		boolean result=false;
		if(!this.nextTaskNodeBeanList.contains(nextTaskNodeBean)){
			result=this.nextTaskNodeBeanList.add(nextTaskNodeBean);
		}
		return result;
	}
	/**
	 * @return the nextTaskNodeBeanList
	 */
	public List<NextTaskNodeBean> getNextTaskNodeBeanList() {
		return nextTaskNodeBeanList;
	}
}
