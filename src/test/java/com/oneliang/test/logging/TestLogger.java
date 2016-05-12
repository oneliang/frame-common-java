package com.oneliang.test.logging;

import com.oneliang.util.logging.BaseLogger;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.Logger.Level;

public class TestLogger {

	public static void main(String[] args){
		Logger logger=new BaseLogger(Logger.Level.ERROR);
		logger.verbose("verbose");
		logger.debug("debug");
		logger.info("info");
		logger.warning("warning");
		logger.error("error",null);
	}
}
