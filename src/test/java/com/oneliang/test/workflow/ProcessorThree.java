package com.oneliang.test.workflow;

import com.oneliang.frame.workflow.Processor;

public class ProcessorThree implements Processor {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5251546616558322306L;

	public String[] process() {
		System.out.println("--process three--");
		return null;
	}

}
