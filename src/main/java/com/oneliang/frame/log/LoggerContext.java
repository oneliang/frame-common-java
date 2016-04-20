package com.oneliang.frame.log;

import com.oneliang.frame.AbstractContext;
import com.oneliang.util.log.LoggerManager;

public class LoggerContext extends AbstractContext {

	private static final LoggerManager loggerManager=new LoggerManager();

	/**
	 * initialize
	 */
	public void initialize(String parameters) {
		loggerManager.setProjectRealPath(this.projectRealPath);
		loggerManager.setClassesRealPath(this.classesRealPath);
		loggerManager.setClassLoader(this.classLoader);
		loggerManager.initialize(parameters);
	}

	/**
	 * destroy
	 */
	public void destroy() {
		loggerManager.destroy();
	}
}
