package com.oneliang.frame.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Task implements Serializable,Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6620490898796242547L;

	private boolean finish=false;
	private List<TaskNode> rootTaskNodeList=new CopyOnWriteArrayList<TaskNode>();
	private List<TaskNode> currentTaskNodeList=new CopyOnWriteArrayList<TaskNode>();

	/**
	 * @return the finish
	 */
	public boolean isFinish() {
		return this.finish;
	}
	/**
	 * @param finish the finish to set
	 */
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	/**
	 * add task node
	 * @param taskNode
	 * @return boolean
	 */
	public boolean addRootTaskNode(TaskNode taskNode){
		boolean result=false;
		if(taskNode!=null){
			taskNode.setTask(this);
		}
		if(!this.rootTaskNodeList.contains(taskNode)){
			result=this.rootTaskNodeList.add(taskNode);
		}
		return result;
	}
	/**
	 * @return the taskNodeList
	 */
	public List<TaskNode> getRootTaskNodeList() {
		return this.rootTaskNodeList;
	}
	/**
	 * @param taskNode
	 * @return boolean
	 */
	protected boolean addCurrentTaskNode(TaskNode taskNode){
		boolean result=false;
		if(!this.currentTaskNodeList.contains(taskNode)){
			result=this.currentTaskNodeList.add(taskNode);
		}
		return result;
	}
	/**
	 * 
	 * @param taskNode
	 * @return boolean
	 */
	protected boolean removeCurrentTaskNode(TaskNode taskNode){
		return this.currentTaskNodeList.remove(taskNode);
	}
	/**
	 * @return the currentTaskNodeList
	 */
	public List<TaskNode> getCurrentTaskNodeList() {
		return this.currentTaskNodeList;
	}
	/**
	 * start the task
	 */
	public void start(){
		if(this.rootTaskNodeList!=null){
			for(TaskNode taskNode:this.rootTaskNodeList){
				taskNode.start();
			}
		}
	}
	
	/**
	 * start current task node
	 */
	public void startCurrentTaskNode(){
		if(this.currentTaskNodeList!=null){
			List<TaskNode> iterateList=new ArrayList<TaskNode>();
			for(TaskNode taskNode:this.currentTaskNodeList){
				iterateList.add(taskNode);
			}
			for(TaskNode taskNode:iterateList){
				taskNode.start();
			}
			List<Integer> finishedList=new ArrayList<Integer>(); 
			int position=0;
			for(TaskNode taskNode:this.currentTaskNodeList){
				if(taskNode.isFinish()){
					finishedList.add(position);
				}
				position++;
			}
			for(Integer index:finishedList){
				this.currentTaskNodeList.remove(index.intValue());
			}
		}
	}
	
	/**
	 * clone the task
	 */
	public Task clone(){
		Task task=new Task();
		for(TaskNode taskNode:this.currentTaskNodeList){
			task.addCurrentTaskNode(taskNode.clone(task));
		}
		for(TaskNode taskNode:this.rootTaskNodeList){
			task.addRootTaskNode(taskNode.clone(task));
		}
		task.setFinish(this.finish);
		return task;
	}
	
	/**
	 * override toString
	 */
	public String toString() {
		StringBuilder string=new StringBuilder();
		string.append("[Task:"+hashCode()+",");
		string.append("finish:"+this.finish+",");
		string.append("rootTaskNode:[");
		int index=0;
		for(TaskNode taskNode:this.rootTaskNodeList){
			string.append(taskNode);
			if(index<rootTaskNodeList.size()-1){
				string.append(",");
			}
			index++;
		}
		string.append("]");
		string.append("currentTaskNode:[");
		index=0;
		for(TaskNode taskNode:this.currentTaskNodeList){
			string.append(taskNode);
			if(index<currentTaskNodeList.size()-1){
				string.append(",");
			}
			index++;
		}
		string.append("]");
		string.append("]");
		return string.toString();
	}
	
	/**
	 * test
	 */
	public static void main(String[] args){
		Task task=new Task();
		TaskNode node1=new TaskNode();
		node1.setId("1");
		node1.setCommand("node1");
		node1.setProcessor(new Processor(){
			int i=0;
			private static final long serialVersionUID = -3638269788787476512L;
			public String[] process() {
				String[] string=null;
				if(i==0){
					i=1;
				}else{
					string=new String[]{"node3"};
				}
				System.out.println("node1--process--"+i);
				return string;
			}
		});
		TaskNode node2=new TaskNode();
		node2.setId("2");
		node2.setCommand("node2");
		node2.setProcessor(new Processor(){
			int i=0;
			private static final long serialVersionUID = -2304884433706804247L;
			public String[] process() {
				String[] string=null;
				if(i==0){
					i=1;
				}else{
					string=new String[]{"node3"};
				}
				System.out.println("node2--process--"+i);
				return string;
			}
		});
		TaskNode node3=new TaskNode();
		node3.setId("3");
		node3.setCommand("node3");
		node3.setProcessor(new Processor(){
			private static final long serialVersionUID = 5328243182273873218L;
			public String[] process() {
				System.out.println("node3--process");
				return null;
			}
		});
		
		TaskNode node4=new TaskNode();
		node4.setId("4");
		node4.setCommand("node4");
		node4.setProcessor(new Processor(){
			private static final long serialVersionUID = -6456522538136656884L;
			public String[] process() {
				System.out.println("node4--process");
				return null;
			}
		});
		task.addRootTaskNode(node1);
		task.addRootTaskNode(node2);
		node1.addNextTaskNode(node3);
		node2.addNextTaskNode(node3);
		task.start();
		node3.setProcessor(new Processor(){
			private static final long serialVersionUID = -230287867740779383L;
			public String[] process() {
				System.out.println("---node3--process");
				return null;
			}
		});
		task.startCurrentTaskNode();
		System.out.println(task.getCurrentTaskNodeList());
	}
}
