package com.oneliang.test.workflow;

import com.oneliang.frame.jdbc.Query;
import com.oneliang.frame.workflow.Processor;

public class ProcessorOne implements Processor {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5006032250291852380L;
	
	private Query query=null; 

	public String[] process() {
		System.out.println("--process one--");
		return new String[]{"node2"};
	}

	public void setQuery(Query query) {
		System.out.println(this+"---"+query);
		this.query = query;
	}
}
