package com.oneliang.frame.servlet;

import java.io.File;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.oneliang.Constants;
import com.oneliang.frame.ConfigurationFactory;
import com.oneliang.frame.configuration.ConfigurationContext;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class ContextListener implements ServletContextListener {
	
	/**
	 * ContextListener constant
	 */
	private static final Logger logger=LoggerManager.getLogger(ContextListener.class);

//	private static final String CONTEXT_PARAMETER_DBCONFIG="dbConfig";
	private static final String CONTEXT_PARAMETER_CONFIGFILE="configFile";
	
	/**
	 * when the server is shut down,close the connection pool
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ConfigurationContext configurationContext=ConfigurationFactory.getSingletonConfigurationContext();
		configurationContext.destroyAll();
	}
	
	/**
	 * when the server is starting initial all thing
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		TimeZone.setDefault(TimeZone.getTimeZone(Constants.Timezone.ASIA_SHANGHAI));
		//System.setProperty(StaticVar.USER_TIMEZONE, StaticVar.TIMEZONE_ASIA_SHANGHAI);
		Locale.setDefault(Locale.CHINA);
//		String dbConfig=servletContextEvent.getServletContext().getInitParameter(CONTEXT_PARAMETER_DBCONFIG);
		String configFile=servletContextEvent.getServletContext().getInitParameter(CONTEXT_PARAMETER_CONFIGFILE);
		//real path
		String realPath=servletContextEvent.getServletContext().getRealPath(StringUtil.BLANK);
		
		//config file
		if(StringUtil.isNotBlank(configFile)){
			ConfigurationContext configurationContext=ConfigurationFactory.getSingletonConfigurationContext();
			try {
				String classRealPath=Thread.currentThread().getContextClassLoader().getResource(StringUtil.BLANK).getPath();
				classRealPath=new File(classRealPath).getAbsolutePath();
				configurationContext.setClassesRealPath(classRealPath);
				configurationContext.setProjectRealPath(realPath);
				configurationContext.initialize(configFile);
				ConfigurationFactory.inject();
				ConfigurationFactory.afterInject();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(Constants.Base.EXCEPTION, e);
			}
		}else{
//			log.log("config file is not found,please initial the config file");
		}
	}
}
