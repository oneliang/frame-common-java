package com.oneliang.test.concurrent;

import com.oneliang.frame.log.LoggerContext;
import com.oneliang.util.concurrent.ThreadPool;
import com.oneliang.util.concurrent.ThreadTask;
import com.oneliang.util.log.Logger;

public class TestThreadPool {

	public static void main(String[] args) {
		String classesRealPath=System.getProperty("user.dir")+"/bin";
		String logConfigFilePath="/com/lwx/test/concurrent/threadPoolLogConfig.xml";
		LoggerContext loggerContext=new LoggerContext();
		loggerContext.setClassesRealPath(classesRealPath);
		loggerContext.setProjectRealPath(classesRealPath);
		try {
			loggerContext.initialize(logConfigFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThreadPool threadPool=new ThreadPool();
		threadPool.setMaxThreads(3);
		try {
			threadPool.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<100;i++){
			ThreadTask threadTask=new TempThreadTask(String.valueOf(i));
			threadPool.addThreadTask(threadTask);
		}
	}
}
class TempThreadTask implements ThreadTask{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5158735165442274232L;

	private static final Logger logger=Logger.getLogger(TempThreadTask.class);
	
	private String id=null;
	
	public TempThreadTask(String id){
		this.id=id;
	}
	
	public void runTask() {
		logger.log(this.id);
	}
}