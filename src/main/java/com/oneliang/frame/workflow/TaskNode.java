package com.oneliang.frame.workflow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskNode implements Serializable,Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3345532055742993351L;
	
	private String id=null;
	private String name=null;
	private String command=null;
	private String text=null;
	private boolean finish=false;
	private Task task=null;
	private Processor processor=null;
	private List<TaskNode> previousTaskNodeList=new CopyOnWriteArrayList<TaskNode>();
	private List<TaskNode> nextTaskNodeList=new CopyOnWriteArrayList<TaskNode>();
	private Map<String,TaskNode> nextTaskNodeMap=new ConcurrentHashMap<String,TaskNode>();

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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * @return the finish
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * @param finish the finish to set
	 */
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	/**
	 * @return the process
	 */
	public Processor getProcessor() {
		return processor;
	}

	/**
	 * @param processor the processor to set
	 */
	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
	
	/**
	 * add previous task node
	 * @param taskNode
	 * @return boolean
	 */
	protected boolean addPreviousTaskNode(TaskNode taskNode){
		if(taskNode!=null){
			taskNode.setTask(this.task);
		}
		return previousTaskNodeList.add(taskNode);
	}

	/**
	 * @return the previousTaskNodeList
	 */
	public List<TaskNode> getPreviousTaskNodeList() {
		return previousTaskNodeList;
	}
	
	/**
	 * add next task node
	 * @param taskNode
	 * @return boolean
	 */
	public boolean addNextTaskNode(TaskNode taskNode){
		if(taskNode!=null){
			taskNode.setTask(this.task);
			taskNode.addPreviousTaskNode(this);
			this.nextTaskNodeMap.put(taskNode.command, taskNode);
		}
		return this.nextTaskNodeList.add(taskNode);
	}

	/**
	 * @return the nextTaskNodeList
	 */
	public List<TaskNode> getNextTaskNodeList() {
		return nextTaskNodeList;
	}
	
	/**
	 * start the taskNode process
	 */
	public void start(){
		//when this node to start ,all previous task node must be finish
		if(this.processor!=null){
			String[] commands=null;
			//process the processor
			commands=this.processor.process();
			//if process is true,end the processor
			if(commands==null||commands.length==0){
				this.task.addCurrentTaskNode(this);
			}else{
				this.finish=true;
				for(String command:commands){
					TaskNode taskNode=nextTaskNodeMap.get(command);
					if(taskNode!=null){
						taskNode.start();
					}
				}
			}
		}
	}
	
	/**
	 * only clone the taskNode,do not clone the dependence of task
	 * so must send the parameter of task
	 */
	public TaskNode clone(Task task){
		TaskNode taskNode=new TaskNode();
		taskNode.setId(this.id);
		taskNode.setName(this.name);
		taskNode.setCommand(this.command);
		taskNode.setText(this.text);
		taskNode.setProcessor(this.processor);
		taskNode.setTask(task);
		for(TaskNode nextTaskNode:this.nextTaskNodeList){
			taskNode.addNextTaskNode(nextTaskNode.clone(task));
		}
		return taskNode;
	}
	
	/**
	 * override toString
	 */
	public String toString() {
		StringBuilder string=new StringBuilder();
		string.append("[TaskNode:"+hashCode()+",");
		string.append("id:"+this.id+",");
		string.append("name:"+this.name+",");
		string.append("command:"+this.command+",");
		string.append("text:"+this.text+",");
		string.append("finish:"+this.finish+",");
		string.append("task:"+(this.task==null?"null":this.task.hashCode())+",");
		string.append("processor:"+this.processor+",");
		string.append("nextTaskNode:[");
		int index=0;
		for(TaskNode taskNode:this.nextTaskNodeList){
			string.append(taskNode);
			if(index<this.nextTaskNodeList.size()-1){
				string.append(",");
			}
			index++;
		}
		string.append("]");
		string.append("]");
		return string.toString();
	}
}
