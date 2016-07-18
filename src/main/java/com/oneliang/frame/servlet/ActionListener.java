package com.oneliang.frame.servlet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oneliang.Constant;
import com.oneliang.frame.ConfigurationFactory;
import com.oneliang.frame.configuration.ConfigurationContext;
import com.oneliang.frame.servlet.action.Action.RequestMapping.RequestParameter;
import com.oneliang.frame.servlet.action.ActionBean;
import com.oneliang.frame.servlet.action.ActionExecuteException;
import com.oneliang.frame.servlet.action.ActionForwardBean;
import com.oneliang.frame.servlet.action.ActionInterceptorBean;
import com.oneliang.frame.servlet.action.ActionInterface;
import com.oneliang.frame.servlet.action.ActionInterface.HttpRequestMethod;
import com.oneliang.frame.servlet.action.AnnotationActionBean;
import com.oneliang.frame.servlet.action.Interceptor;
import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.ClassUtil.ClassProcessor;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.RequestUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;
/**
 * com.lwx.frame.servlet.Listener.java
 * @author Dandelion
 * This is only one global servletListener in commonFrame
 * @since 2008-07-31
 */
public class ActionListener extends HttpServlet{
	
	/**
	 * <p>Property: serialVersionUID</p>
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8982018678465106212L;
	
	private static final Logger logger=LoggerManager.getLogger(ActionListener.class);

	private static final String INIT_PARAMETER_CLASS_PROCESSOR="classProcessor";

	private ClassProcessor classProcessor=ClassUtil.DEFAULT_CLASS_PROCESSOR;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//servlet bean
		ActionUtil.ServletBean servletBean=ActionUtil.getServletBean();
		if(servletBean==null){
			servletBean=new ActionUtil.ServletBean();
			ActionUtil.setServletBean(servletBean);
		}
		servletBean.setServletContext(this.getServletContext());
		servletBean.setServletRequest(request);
		servletBean.setServletResponse(response);
		//execute default service method,distribute doGet or doPost or other http method
		super.service(request, response);
		//servlet bean request and response set null
		servletBean.setServletRequest(null);
		servletBean.setServletResponse(null);
	}

	protected long getLastModified(HttpServletRequest request) {
		//uri
//		String uri=request.getRequestURI();
//		int front=request.getContextPath().length();
//		uri=uri.substring(front,uri.length());
//		return 1368624759725l;
		return super.getLastModified(request);
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
		logger.info("System is shutting down,listener is deleting,please wait");
	}

	/**
	 * The doDelete method of the servlet. <br>
	 *
	 * This method is called when a HTTP delete request is received.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatch(request, response, HttpRequestMethod.DELETE);
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatch(request, response, HttpRequestMethod.GET);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatch(request, response, HttpRequestMethod.POST);
	}

	
	/**
	 * The doPut method of the servlet. <br>
	 *
	 * This method is called when a HTTP put request is received.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatch(request, response, HttpRequestMethod.PUT);
	}

	/**
	 * dispatch http request
	 * @param request
	 * @param response
	 * @param httpRequestMethod
	 * @throws ServletException
	 * @throws IOException
	 */
	private void dispatch(HttpServletRequest request, HttpServletResponse response, HttpRequestMethod httpRequestMethod) throws ServletException, IOException {
			//uri
			String uri=request.getRequestURI();
			
			logger.info("System is requesting uri--:"+uri);
			
			int front=request.getContextPath().length();
	//		int rear=uri.lastIndexOf(StaticVar.DOT);
	//		if(rear>front){
			uri=uri.substring(front,uri.length());
	//		}
	//		uri=uri.substring(front,rear);
			logger.info("The request name is--:"+uri);
			
			//global interceptor doIntercept
			List<Interceptor> beforeGlobalInterceptorList=ConfigurationFactory.getBeforeGlobalInterceptorList();
			boolean beforeGlobalInterceptorSign=doGlobalInterceptorList(beforeGlobalInterceptorList,request, response);
			
			//through the interceptor
			if(beforeGlobalInterceptorSign){
				logger.info("Through the before global interceptors!");
				try {
					List<ActionBean> actionBeanList=ConfigurationFactory.findActionBeanList(uri);
					if(actionBeanList!=null&&!actionBeanList.isEmpty()){
						ActionBean actionBean=null;
						for(ActionBean eachActionBean:actionBeanList){
							if(eachActionBean.isContainHttpRequestMethod(httpRequestMethod)){
								actionBean=eachActionBean;
								break;
							}
						}
						if(actionBean!=null){
							//action interceptor doIntercept
							List<ActionInterceptorBean> beforeActionBeanInterceptorList=actionBean.getBeforeActionInterceptorBeanList();
							boolean beforeActionInterceptorSign=doActionInterceptorBeanList(beforeActionBeanInterceptorList, request, response);
							if(beforeActionInterceptorSign){
								logger.info("Through the before action interceptors!");
								Object actionInstance=actionBean.getActionInstance();
								if(actionInstance instanceof ActionInterface){
									doAction(actionBean, request, response, httpRequestMethod);
								}else{
									doAnnotationAction(actionBean, request, response, httpRequestMethod);
								}
							}else{
								logger.info("Can not through the before action interceptors");
								response.sendError(Constant.Http.StatusCode.FORBIDDEN);
							}
						}else{
							logger.info("Method not allowed,http request method:"+httpRequestMethod);
							response.sendError(Constant.Http.StatusCode.METHOD_NOT_ALLOWED);
						}
					}else{
						logger.info("The request name--:"+uri+" is not exist,please config the name and entity class");
						response.sendError(Constant.Http.StatusCode.NOT_FOUND);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(Constant.Base.EXCEPTION, e);
					logger.info("Action or page is not exist");
					String exceptionPath=ConfigurationFactory.getGlobalExceptionForwardPath();
					if(exceptionPath!=null){
						request.setAttribute(Constant.Base.EXCEPTION, e);
						RequestDispatcher requestDispatcher=request.getRequestDispatcher(exceptionPath);
						requestDispatcher.forward(request,response);
						logger.info("Forward to exception path:"+exceptionPath);
					}else{
						logger.info("System can not find the exception path.Please config the global exception forward path.");
						response.sendError(Constant.Http.StatusCode.INTERNAL_SERVER_ERROR);
					}
				}
			}else{
				logger.info("Can not through the before global interceptors");
				response.sendError(Constant.Http.StatusCode.FORBIDDEN);
			}
		}

	/**
	 * Returns information about the servlet, such as 
	 * author, version, and copyright. 
	 *
	 * @return String information about this servlet
	 */
	public String getServletInfo() {
		return this.getClass().toString();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		logger.info("System is starting up,listener is initial");
		String classProcessorClassName=getInitParameter(INIT_PARAMETER_CLASS_PROCESSOR);
		if(StringUtil.isNotBlank(classProcessorClassName)){
			try {
				Class<?> clazz=Thread.currentThread().getContextClassLoader().loadClass(classProcessorClassName);
				if(ObjectUtil.isInterfaceImplement(clazz, ClassProcessor.class)){
					this.classProcessor=(ClassProcessor)clazz.newInstance();
				}
			} catch (Exception e) {
				logger.error(Constant.Base.EXCEPTION, e);
			}
		}
	}

	/**
	 * do action
	 * @param actionBean
	 * @param request
	 * @param response
	 * @return boolean
	 * @throws IOException
	 * @throws ServletException
	 * @throws ActionExecuteException
	 */
	@SuppressWarnings("unchecked")
	private boolean doAction(ActionBean actionBean,HttpServletRequest request, HttpServletResponse response, HttpRequestMethod httpRequestMethod) throws ActionExecuteException, ServletException, IOException{
		boolean result=false;
		Object actionInstance=actionBean.getActionInstance();
		if(actionInstance instanceof ActionInterface){
			ActionInterface actionInterface=(ActionInterface)actionInstance;
			String forward=null;
			logger.info("Action implements ("+actionInstance+") is executing");
			//judge is it contain static file page
			Map<String,String[]> parameterMap=(Map<String,String[]>)request.getParameterMap();
			ActionForwardBean actionForwardBean=actionBean.findActionForwardBeanByStaticParameter(parameterMap);
			boolean normalExecute=true;//default normal execute
			boolean needToStaticExecute=false;
			if(actionForwardBean!=null){//static file page
				normalExecute=false;
				String staticFilePathKey=actionForwardBean.getStaticFilePath();
				if(!StaticFilePathUtil.isContainsStaticFilePath(staticFilePathKey)){
					needToStaticExecute=true;
				}
			}//else normal execute
			if(normalExecute||needToStaticExecute){
				if(normalExecute){
					logger.info("Normal executing");
				}else if(needToStaticExecute){
					logger.info("Need to static execute,first time executing original action");
				}
				forward=actionInterface.execute(request, response);
			}else{
				logger.info("Static execute,not the first time execute");
				forward=actionForwardBean.getName();
			}
			List<ActionInterceptorBean> afterActionBeanInterceptorList=actionBean.getAfterActionInterceptorBeanList();
			boolean afterActionInterceptorSign=doActionInterceptorBeanList(afterActionBeanInterceptorList, request, response);
			if(afterActionInterceptorSign){
				logger.info("Through the after action interceptors!");
				List<Interceptor> afterGlobalInterceptorList=ConfigurationFactory.getAfterGlobalInterceptorList();
				boolean afterGlobalInterceptorSign=doGlobalInterceptorList(afterGlobalInterceptorList, request, response);
				if(afterGlobalInterceptorSign){
					logger.info("Through the after global interceptors!");
					if(forward!=null){
						String path=actionBean.findForwardPath(forward);
						if(path!=null){
							logger.info("The forward name in configFile is--:actionPath:"+actionBean.getPath()+"--forward:"+forward+"--path:"+path);
						}else{
							path=ConfigurationFactory.findGlobalForwardPath(forward);
							logger.info("The forward name in global forward configFile is--:forward:"+forward+"--path:"+path);
						}
						this.doForward(normalExecute, needToStaticExecute, actionForwardBean, path, request, response, false);
					}else{
						logger.info("The forward name--:"+forward+" is not exist,may be ajax use if not please config the name and entity page or class");
					}
				}else{
					logger.info("Can not through the after global interceptors");
				}
			}else{
				logger.info("Can not through the after action interceptors");
			}
			result=true;
		}
		return result;
	}

	/**
	 * @param annotationActionBean
	 * @param request
	 * @param response
	 * @return Object[]
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private Object[] annotationActionMethodParameterValues(AnnotationActionBean annotationActionBean,HttpServletRequest request,HttpServletResponse response) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		Method annotationActionBeanMethod=annotationActionBean.getMethod();
		Class<?>[] classes=annotationActionBeanMethod.getParameterTypes();
		Object[] parameterValues=new Object[classes.length];
		Annotation[][] annotations=annotationActionBean.getMethod().getParameterAnnotations();
		for(int i=0;i<annotations.length;i++){
			if(annotations[i].length>0&&annotations[i][0] instanceof RequestParameter){
				RequestParameter requestParameterAnnotation=(RequestParameter)annotations[i][0];
				parameterValues[i]=ClassUtil.changeType(classes[i],request.getParameterValues(requestParameterAnnotation.value()),this.classProcessor);
			}else if(ObjectUtil.isEntity(request, classes[i])){
				parameterValues[i]=request;
			}else if(ObjectUtil.isEntity(response, classes[i])){
				parameterValues[i]=response;
			}else{
				if(ClassUtil.isBaseClass(classes[i])||ClassUtil.isBaseArray(classes[i])||ClassUtil.isSimpleClass(classes[i])||ClassUtil.isSimpleArray(classes[i])){
					parameterValues[i]=ClassUtil.changeType(classes[i],null,this.classProcessor);
				}else if(classes[i].isArray()){
					Class<?> clazz=classes[i].getComponentType();
					List<?> objectList=RequestUtil.requestMapToObjectList(request.getParameterMap(), clazz, this.classProcessor);
					if(objectList!=null&&!objectList.isEmpty()){
						Object[] objectArray=(Object[])Array.newInstance(clazz, objectList.size());
						objectArray=objectList.toArray(objectArray);
						parameterValues[i]=objectArray;
					}
				}else{
					Object object=classes[i].newInstance();
					RequestUtil.requestMapToObject(request.getParameterMap(), object, this.classProcessor);
					parameterValues[i]=object;
				}
			}
		}
		return parameterValues;
	}

	/**
	 * do annotation bean
	 * @param actionBean
	 * @param request
	 * @param response
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	private boolean doAnnotationAction(ActionBean actionBean,HttpServletRequest request,HttpServletResponse response,HttpRequestMethod httpRequestMethod) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ServletException, IOException{
		boolean result=false;
		if(actionBean instanceof AnnotationActionBean){
			Object actionInstance=actionBean.getActionInstance();
			AnnotationActionBean annotationActionBean=(AnnotationActionBean)actionBean;
			Map<String,String[]> parameterMap=(Map<String,String[]>)request.getParameterMap();
			ActionForwardBean actionForwardBean=actionBean.findActionForwardBeanByStaticParameter(parameterMap);
			boolean normalExecute=true;//default normal execute
			boolean needToStaticExecute=false;
			String path=null;
			if(actionForwardBean!=null){//static file page
				normalExecute=false;
				String staticFilePathKey=actionForwardBean.getStaticFilePath();
				if(!StaticFilePathUtil.isContainsStaticFilePath(staticFilePathKey)){
					needToStaticExecute=true;
				}
			}
			if(normalExecute||needToStaticExecute){
				if(normalExecute){
					logger.info("Common bean action ("+actionInstance+") is executing.");
				}else if(needToStaticExecute){
					logger.info("Need to static execute,first time executing original action");
				}
				Object[] parameterValues=this.annotationActionMethodParameterValues(annotationActionBean, request, response);
				Object methodInvokeValue=annotationActionBean.getMethod().invoke(actionInstance, parameterValues);
				if(methodInvokeValue!=null&&(methodInvokeValue instanceof String)){
					path=methodInvokeValue.toString();
				}
			}else{
				logger.info("Static execute,not the first time execute");
			}
			List<ActionInterceptorBean> afterActionBeanInterceptorList=actionBean.getAfterActionInterceptorBeanList();
			boolean afterActionInterceptorSign=doActionInterceptorBeanList(afterActionBeanInterceptorList, request, response);
			if(afterActionInterceptorSign){
				logger.info("Through the after action interceptors!");
				List<Interceptor> afterGlobalInterceptorList=ConfigurationFactory.getAfterGlobalInterceptorList();
				boolean afterGlobalInterceptorSign=doGlobalInterceptorList(afterGlobalInterceptorList, request, response);
				if(afterGlobalInterceptorSign){
					logger.info("Through the after global interceptors!");
					this.doForward(normalExecute, needToStaticExecute, actionForwardBean, path, request, response, true);
				}
			}
			result=true;
		}
		return result;
	}

	/**
	 * do forward
	 * @param normalExecute
	 * @param needToStaticExecute
	 * @param actionForwardBean
	 * @param path
	 * @param request
	 * @param response
	 * @param annotationBeanExecute
	 * @throws IOException
	 * @throws ServletException
	 */
	private void doForward(boolean normalExecute,boolean needToStaticExecute,ActionForwardBean actionForwardBean,String path,HttpServletRequest request,HttpServletResponse response,boolean annotationBeanExecute) throws ServletException, IOException{
		if(!normalExecute&&!needToStaticExecute){
			String staticFilePath=actionForwardBean.getStaticFilePath();
			logger.info("Send redirect to static file path:"+staticFilePath);
			RequestDispatcher requestDispatcher=request.getRequestDispatcher(staticFilePath);
			requestDispatcher.forward(request,response);
		}else{
			if(StringUtil.isNotBlank(path)){
				path=ActionUtil.parsePath(path);
				if(normalExecute){
					if(annotationBeanExecute){
						logger.info("Annotation bean action executed forward path:"+path);
					}else{
						logger.info("Normal executed forward path:"+path);
					}
					RequestDispatcher requestDispatcher=request.getRequestDispatcher(path);
					requestDispatcher.forward(request,response);
				}else if(needToStaticExecute){
					String staticFilePath=actionForwardBean.getStaticFilePath();
					ConfigurationContext configurationContext=ConfigurationFactory.getSingletonConfigurationContext();
					if(StaticFilePathUtil.staticize(path, configurationContext.getProjectRealPath()+staticFilePath, request, response)){
						logger.info("Static executed success,redirect static file:"+staticFilePath);
						RequestDispatcher requestDispatcher=request.getRequestDispatcher(staticFilePath);
						requestDispatcher.forward(request,response);
						StaticFilePathUtil.addStaticFilePath(staticFilePath, staticFilePath);
					}else{
						logger.info("Static executed failure,file:"+staticFilePath);
					}
				}
			}else{
				if(annotationBeanExecute){
					logger.info("May be ajax use if not please config the entity page with String type.");
				}else{
					logger.info("System can not find the path:"+path);
				}
			}
		}
	}

	/**
	 * do global interceptor list,include global(before,after)
	 * @param interceptorList
	 * @param request
	 * @param response
	 * @return boolean
	 */
	private boolean doGlobalInterceptorList(List<Interceptor> interceptorList,HttpServletRequest request,HttpServletResponse response){
		boolean interceptorSign=true;
		if(interceptorList!=null){
			try{
				for(Interceptor globalInterceptor:interceptorList){
					boolean sign=globalInterceptor.doIntercept(request, response);
					logger.info("Global intercept:"+sign+",interceptor:"+globalInterceptor);
					if(!sign){
						interceptorSign=false;
						break;
					}
				}
			}catch(Exception e){
				logger.error(Constant.Base.EXCEPTION,e);
				interceptorSign=false;
			}
		}
		return interceptorSign;
	}
	
	/**
	 * do action bean interceptor list,include action(before,action)
	 * @param actionInterceptorBeanList
	 * @param request
	 * @param response
	 * @return boolean
	 */
	private boolean doActionInterceptorBeanList(List<ActionInterceptorBean> actionInterceptorBeanList,HttpServletRequest request,HttpServletResponse response){
		boolean actionInterceptorBeanSign=true;
		if(actionInterceptorBeanList!=null){
			try{
				for(ActionInterceptorBean actionInterceptorBean:actionInterceptorBeanList){
					Interceptor actionInterceptor=actionInterceptorBean.getInterceptorInstance();
					if(actionInterceptor!=null){
						boolean sign=actionInterceptor.doIntercept(request, response);
						logger.info("Action intercept:"+sign+",interceptor:"+actionInterceptor);
						if(!sign){
							actionInterceptorBeanSign=false;
							break;
						}
					}
				}
			}catch(Exception e){
				logger.error(Constant.Base.EXCEPTION, e);
				actionInterceptorBeanSign=false;
			}
		}
		return actionInterceptorBeanSign;
	}
}
