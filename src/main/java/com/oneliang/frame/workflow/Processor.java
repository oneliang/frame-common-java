package com.oneliang.frame.workflow;

import java.io.Serializable;

public abstract interface Processor extends Serializable{
	/**
	 * process the process
	 */
	public abstract String[] process();
}
