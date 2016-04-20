package com.oneliang.frame.servlet.action;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.oneliang.frame.bean.Page;
import com.oneliang.frame.servlet.ActionUtil;
import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.RequestUtil;
import com.oneliang.util.common.ClassUtil.ClassProcessor;

/**
 * <p>
 * Class: abstract class,make sub class union
 * </p>
 * 
 * com.lwx.frame.servlet.CommonAction
 * abstract class
 * @author Dandelion
 * @since 2008-07-31
 */
public abstract class CommonAction implements ActionInterface {

	protected ClassProcessor classProcessor=ClassUtil.DEFAULT_CLASS_PROCESSOR;

	/**
	 * <p>
	 * Method: set the instance object to the request
	 * </p>
	 * @param <T>
	 * @param key
	 * @param value
	 */
	protected <T extends Object> void setObjectToRequest(String key,T value){
		ServletRequest request=ActionUtil.getServletRequest();
		this.setObjectToRequest(request, key, value);
	}
	
	/**
	 * <p>
	 * Method: set the instance object to the request
	 * </p>
	 * 
	 * @param request
	 */
	protected <T extends Object> void setObjectToRequest(ServletRequest request, String key, T value) {
		request.setAttribute(key, value);
	}

	/**
	 * <p>
	 * Method: set the request values to object
	 * </p>
	 * @param <T>
	 * @param object
	 */
	protected <T extends Object> void requestValuesToObject(T object){
		ServletRequest request=ActionUtil.getServletRequest();
		this.requestValuesToObject(request, object);
	}
	
	/**
	 * <p>
	 * Method: set the request values to object
	 * </p>
	 * 
	 * @param <T>
	 * @param request
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Object> void requestValuesToObject(ServletRequest request, T object){
		Map<String, String[]> map = request.getParameterMap();
		RequestUtil.requestMapToObject(map, object ,classProcessor);
	}

	/**
	 * <p>
	 * Method: set the instance object to the session
	 * </p>
	 * @param <T>
	 * @param key
	 * @param value
	 */
	protected <T extends Object> void setObjectToSession(String key,T value){
		ServletRequest request=ActionUtil.getServletRequest();
		this.setObjectToSession(request, key, value);
	}
	
	/**
	 * <p>
	 * Method: set the instance object to the session
	 * </p>
	 * 
	 * @param <T>
	 * @param request
	 * @param key
	 * @param value
	 */
	protected <T extends Object> void setObjectToSession(ServletRequest request, String key,
			T value) {
		((HttpServletRequest)request).getSession().setAttribute(key, value);
	}

	/**
	 * <p>
	 * Method: get the instance object to the session by key
	 * </p>
	 * @param key
	 * @return Object
	 */
	protected Object getObjectFromSession(String key){
		ServletRequest request=ActionUtil.getServletRequest();
		return this.getObjectFromSession(request, key);
	}
	
	/**
	 * <p>
	 * Method: get the instance object to the session by key
	 * </p>
	 * 
	 * @param request
	 * @param key
	 * @return Object
	 */
	protected Object getObjectFromSession(ServletRequest request, String key) {
		return ((HttpServletRequest)request).getSession().getAttribute(key);
	}

	/**
	 * <p>Method: remove object from session</p>
	 * @param key
	 */
	protected void removeObjectFromSession(String key){
		ServletRequest request=ActionUtil.getServletRequest();
		removeObjectFromSession(request, key);
	}

	/**
	 * <p>Method: remove object from session</p>
	 * @param request
	 * @param key
	 */
	protected void removeObjectFromSession(ServletRequest request,String key){
		((HttpServletRequest)request).getSession().removeAttribute(key);
	}

	/**
	 * <p>
	 * Method: get the parameter from request
	 * </p>
	 * @param parameter
	 * @return String
	 */
	protected String getParameter(String parameter) {
		ServletRequest request=ActionUtil.getServletRequest();
		return this.getParameter(request, parameter);
	}
	
	/**
	 * <p>
	 * Method: get the parameter from request
	 * </p>
	 * 
	 * @param request
	 * @param parameter
	 * @return String
	 */
	protected String getParameter(ServletRequest request, String parameter) {
		return request.getParameter(parameter);
	}
	
	/**
	 * <p>
	 * Method:get the parameter values from request
	 * </p>
	 * @param parameter
	 * @return String[]
	 */
	protected String[] getParameterValues(String parameter) {
		ServletRequest request=ActionUtil.getServletRequest();
		return this.getParameterValues(request, parameter);
	}
	
	/**
	 * <p>
	 * Method:get the parameter values from request
	 * </p>
	 * @param request
	 * @param parameter
	 * @return String[]
	 */
	protected String[] getParameterValues(ServletRequest request, String parameter) {
		return request.getParameterValues(parameter);
	}
	
	/**
	 * forward
	 * @param path
	 * @throws ActionExecuteException
	 */
	protected void forward(String path) throws ActionExecuteException{
		ServletRequest request=ActionUtil.getServletRequest();
		ServletResponse response=ActionUtil.getServletResponse();
		try {
			this.forward(request,response,path);
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		}
	}
	
	/**
	 * request.getRequestDispatcher(path).forward(request,response);
	 * @param request
	 * @param response
	 * @param path
	 * @throws ActionExecuteException
	 */
	protected void forward(ServletRequest request,ServletResponse response,String path) throws ActionExecuteException{
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		}
	}
	
	/**
	 * write
	 * @param string
	 * @throws ActionExecuteException
	 */
	protected void write(String string) throws ActionExecuteException{
		ServletResponse response=ActionUtil.getServletResponse();
		try {
			this.write(response,string);
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		}
	}
	
	/**
	 * write
	 * @param response
	 * @param string
	 * @throws ActionExecuteException
	 */
	protected void write(ServletResponse response,String string) throws ActionExecuteException{
		try {
			response.getWriter().write(string);
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		}
	}
	
	/**
	 * <p>Method: get page</p>
	 * @return Page
	 */
	protected Page getPage(){
		ServletRequest request=ActionUtil.getServletRequest();
		return getPage(request);
	}
	
	/**
	 * <p>Method: get page</p>
	 * @param request
	 * @return Page
	 */
	protected Page getPage(ServletRequest request){
		Page page=new Page();
		this.requestValuesToObject(request, page);
		return page;
	}

	/**
	 * @param classProcessor the classProcessor to set
	 */
	public void setClassProcessor(ClassProcessor classProcessor) {
		this.classProcessor = classProcessor;
	}
}
