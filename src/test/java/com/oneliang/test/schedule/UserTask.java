package com.oneliang.test.schedule;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserTask implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7820540698568227260L;

	private String name=null;
	private List<Task> taskList=new CopyOnWriteArrayList<Task>();
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
	 * add task
	 * @param task
	 * @return boolean
	 */
	public boolean addTask(Task task){
		return taskList.add(task);
	}
	/**
	 * @return the taskList
	 */
	public List<Task> getTaskList() {
		return taskList;
	}
}
