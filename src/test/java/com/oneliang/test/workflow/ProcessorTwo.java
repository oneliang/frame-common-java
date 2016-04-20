package com.oneliang.test.workflow;

import com.oneliang.frame.workflow.Processor;

public class ProcessorTwo implements Processor {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5778138161700937719L;

	public String[] process() {
		System.out.println("--process two--");
		return new String[]{"node3"};
	}

}
