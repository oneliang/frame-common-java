package com.oneliang.frame.servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.oneliang.Constant;

public final class StaticFilePathUtil{

	private static final String RESPONSE_STATIC_CONTENTTYPE="text/html;charset="+Constant.Encoding.UTF8;

	/**
	 * key:uri,value:
	 */
	private static final Map<String,String> staticFilePathMap=new ConcurrentHashMap<String,String>();

	private StaticFilePathUtil(){}

	/**
	 * is contains static file path
	 * @param staticFilePath
	 * @return boolean
	 */
	static boolean isContainsStaticFilePath(String staticFilePath){
		return staticFilePathMap.containsKey(staticFilePath);
	}
	
	/**
	 * add static file path
	 * @param key
	 * @param staticFilePath
	 */
	static void addStaticFilePath(String key,String staticFilePath){
		staticFilePathMap.put(key, staticFilePath);
	}

	/**
	 * get static file path
	 * @param key
	 * @return static file path
	 */
	public static String getStaticFilePath(String key){
		return staticFilePathMap.get(key);
	}

	/**
	 * update static file path
	 * @param key
	 * @param staticFilePath
	 */
	public static void updateStaticFilePath(String key,String staticFilePath){
		staticFilePathMap.put(key, staticFilePath);
	}

	/**
	 * staticize 
	 * @param path
	 * @param staticFilePath
	 * @param request
	 * @param response
	 * @return boolean
	 */
	static boolean staticize(String path,String staticFilePath,ServletRequest request, ServletResponse response){
		boolean result=false;
		response.setContentType(RESPONSE_STATIC_CONTENTTYPE);
		result=ActionUtil.includeJspAndSave(path,staticFilePath,request,response);
		return result;
	}
}
