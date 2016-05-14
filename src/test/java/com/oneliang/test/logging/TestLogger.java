package com.oneliang.test.logging;

import com.oneliang.util.logging.BaseLogger;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class TestLogger {

	static{
		LoggerManager.registerLogger("com.*", new BaseLogger(Logger.Level.VERBOSE));
	}

	private static final Logger logger=LoggerManager.getLogger(TestLogger.class);

	public void testLogger(){
		logger.verbose("verbose");
		logger.debug("debug");
		logger.info("info");
		logger.warning("warning");
		logger.error("error",null);
	}

	public static void main(String[] args){
		TestLogger testLogger=new TestLogger();
		testLogger.testLogger();
	}
}
