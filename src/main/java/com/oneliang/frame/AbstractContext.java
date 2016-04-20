package com.oneliang.frame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oneliang.util.jar.JarClassLoader;

public abstract class AbstractContext implements Context {
	
	protected static final Map<String,Object> objectMap=new ConcurrentHashMap<String,Object>();
	protected static JarClassLoader jarClassLoader=new JarClassLoader(Thread.currentThread().getContextClassLoader());

	protected String classesRealPath=null;
	protected String projectRealPath=null;

	protected ClassLoader classLoader=null;

	public AbstractContext() {
		this.classLoader=Thread.currentThread().getContextClassLoader();
	}

	/**
	 * find bean
	 * @param id
	 * @return Object
	 */
	public Object findBean(String id){
		Object object=null;
		if(objectMap.containsKey(id)){
			object=objectMap.get(id);
		}
		return object;
	}
	
	/**
	 * <p>
	 * Method:set classes real path
	 * </p>
	 * 
	 * @param classesRealPath
	 */
	public void setClassesRealPath(String classesRealPath){
		this.classesRealPath=classesRealPath;
	}

	/**
	 * @return the classesRealPath
	 */
	public String getClassesRealPath() {
		return classesRealPath;
	}

	/**
	 * <p>Method:set project real path</p>
	 * @param projectRealPath
	 */
	public void setProjectRealPath(String projectRealPath){
		this.projectRealPath=projectRealPath;
	}

	/**
	 * @return the projectRealPath
	 */
	public String getProjectRealPath() {
		return projectRealPath;
	}
}
