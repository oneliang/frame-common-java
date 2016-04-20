package com.oneliang.frame.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {

	private List<String> actionList = new ArrayList<String>();
	private List<Class<?>> classList = new ArrayList<Class<?>>();
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * default constructor
	 */
	public Message() {
	}

	/**
	 * frequent used constructor
	 * @param action
	 */
	public Message(String action) {
		this.addAction(action);
	}

	/**
	 * add class
	 * @param clazz
	 */
	public void addClass(Class<?> clazz) {
		this.classList.add(clazz);
	}

	/**
	 * add action
	 * @param action
	 */
	public void addAction(String action) {
		this.actionList.add(action);
	}

	/**
	 * put object
	 * 
	 * @param key
	 * @param object
	 */
	public void putObject(String key, Object object) {
		this.map.put(key, object);
	}

	/**
	 * get object
	 * 
	 * @param key
	 * @return object
	 */
	public Object getObject(String key) {
		return this.map.get(key);
	}

	/**
	 * @return the actionList
	 */
	public List<String> getActionList() {
		return this.actionList;
	}

	/**
	 * @return the classList
	 */
	public List<Class<?>> getClassList() {
		return this.classList;
	}
}
