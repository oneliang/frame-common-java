package com.oneliang.frame.servlet.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.oneliang.Constant;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AnnotationContextUtil;
import com.oneliang.frame.servlet.action.Action.RequestMapping;
import com.oneliang.frame.servlet.action.Action.RequestMapping.Interceptor;
import com.oneliang.frame.servlet.action.Action.RequestMapping.Static;
import com.oneliang.util.common.StringUtil;

public class AnnotationActionContext extends ActionContext {

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
		try{
			List<Class<?>> classList=AnnotationContextUtil.parseAnnotationContextParameter(parameters, classLoader, classesRealPath, jarClassLoader, projectRealPath, Action.class);
			if(classList!=null){
				for(Class<?> clazz:classList){
					String classId=clazz.getName();
					Object actionInstance=null;
					if(!objectMap.containsKey(classId)){
						actionInstance=clazz.newInstance();
						objectMap.put(classId,actionInstance);
					}else{
						actionInstance=objectMap.get(classId);
					}
					Method[] methods=clazz.getMethods();
					for(Method method:methods){
						if(method.isAnnotationPresent(RequestMapping.class)){
							Class<?> returnType=method.getReturnType();
							if(returnType!=null&&returnType.equals(String.class)){
								AnnotationActionBean annotationActionBean=new AnnotationActionBean();
								RequestMapping requestMappingAnnotation=method.getAnnotation(RequestMapping.class);
								annotationActionBean.setType(clazz.getName());
								String[] httpRequestMethods=requestMappingAnnotation.httpRequestMethods();
								if(httpRequestMethods!=null&&httpRequestMethods.length>0){
									StringBuilder stringBuilder=new StringBuilder();
									for(int i=0;i<httpRequestMethods.length;i++){
										stringBuilder.append(httpRequestMethods[i]);
										if(i<(httpRequestMethods.length-1)){
											stringBuilder.append(Constant.Symbol.COMMA);
										}
									}
									annotationActionBean.setHttpRequestMethods(stringBuilder.toString());
								}
								int httpRequestMethodsCode=annotationActionBean.getHttpRequestMethodsCode();
								String id=classId+Constant.Symbol.DOT+method.getName()+Constant.Symbol.COMMA+httpRequestMethodsCode;
								annotationActionBean.setId(id);
								String requestPath=requestMappingAnnotation.value();
								annotationActionBean.setPath(requestPath);
								annotationActionBean.setMethod(method);
								annotationActionBean.setActionInstance(actionInstance);
								actionBeanMap.put(id, annotationActionBean);
								List<ActionBean> actionBeanList=null;
								if(pathActionBeanMap.containsKey(requestPath)){
									actionBeanList=pathActionBeanMap.get(requestPath);
								}else{
									actionBeanList=new ArrayList<ActionBean>();
									pathActionBeanMap.put(requestPath, actionBeanList);
								}
								actionBeanList.add(annotationActionBean);
								//interceptor
								Interceptor[] interceptors=requestMappingAnnotation.interceptors();
								if(interceptors!=null&&interceptors.length>0){
									for(Interceptor interceptor:interceptors){
										String interceptorId=interceptor.id();
										Interceptor.Mode interceptorMode=interceptor.mode();
										if(StringUtil.isNotBlank(interceptorId)){
											ActionInterceptorBean actionInterceptorBean=new ActionInterceptorBean();
											actionInterceptorBean.setId(interceptorId);
											switch(interceptorMode){
											case BEFORE:
												actionInterceptorBean.setMode(ActionInterceptorBean.INTERCEPTOR_MODE_BEFORE);
												break;
											case AFTER:
												actionInterceptorBean.setMode(ActionInterceptorBean.INTERCEPTOR_MODE_AFTER);
												break;
											}
											annotationActionBean.addActionBeanInterceptor(actionInterceptorBean);
										}
									}
								}
								//static
								Static[] statics=requestMappingAnnotation.statics();
								if(statics!=null&&statics.length>0){
									for(Static staticAnnotation:statics){
										ActionForwardBean actionForwardBean=new ActionForwardBean();
										actionForwardBean.setStaticParameters(staticAnnotation.parameters());
										actionForwardBean.setStaticFilePath(staticAnnotation.filePath());
										annotationActionBean.addActionForwardBean(actionForwardBean);
									}
								}
							}else{
								throw new InitializeException("@"+RequestMapping.class.getSimpleName()+" method:"+method.getName()+" which the return type must be String.class,current is:"+returnType);
							}
						}
					}
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}
}
