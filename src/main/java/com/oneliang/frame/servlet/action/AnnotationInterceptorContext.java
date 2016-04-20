package com.oneliang.frame.servlet.action;

import java.util.List;

import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AnnotationContextUtil;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.StringUtil;

public class AnnotationInterceptorContext extends InterceptorContext {

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
		try{
			List<Class<?>> classList=AnnotationContextUtil.parseAnnotationContextParameter(parameters, classLoader, classesRealPath, jarClassLoader, projectRealPath, ActionInterceptor.class);
			if(classList!=null){
				for(Class<?> clazz:classList){
					if(ObjectUtil.isInheritanceOrInterfaceImplement(clazz, Interceptor.class)){
						ActionInterceptor interceptorAnnotation=clazz.getAnnotation(ActionInterceptor.class);
						ActionInterceptor.Mode interceptorMode=interceptorAnnotation.mode();
						String id=interceptorAnnotation.id();
						if(StringUtil.isBlank(id)){
							id=clazz.getSimpleName();
							id=id.substring(0,1).toLowerCase()+id.substring(1);
						}
						Interceptor interceptorInstance=(Interceptor)clazz.newInstance();
						switch(interceptorMode){
						case GLOBAL_ACTION_BEFORE:
							GlobalInterceptorBean globalBeforeInterceptor=new GlobalInterceptorBean();
							globalBeforeInterceptor.setId(id);
							globalBeforeInterceptor.setMode(GlobalInterceptorBean.INTERCEPTOR_MODE_BEFORE);
							globalBeforeInterceptor.setInterceptorInstance(interceptorInstance);
							globalBeforeInterceptor.setType(clazz.getName());
							globalInterceptorBeanMap.put(globalBeforeInterceptor.getId(),globalBeforeInterceptor);
							beforeGlobalInterceptorList.add(interceptorInstance);
							break;
						case GLOBAL_ACTION_AFTER:
							GlobalInterceptorBean globalAfterInterceptor=new GlobalInterceptorBean();
							globalAfterInterceptor.setId(id);
							globalAfterInterceptor.setMode(GlobalInterceptorBean.INTERCEPTOR_MODE_AFTER);
							globalAfterInterceptor.setInterceptorInstance(interceptorInstance);
							globalAfterInterceptor.setType(clazz.getName());
							globalInterceptorBeanMap.put(globalAfterInterceptor.getId(),globalAfterInterceptor);
							afterGlobalInterceptorList.add(interceptorInstance);
							break;
						case SINGLE_ACTION:
							InterceptorBean interceptor=new InterceptorBean();
							interceptor.setId(id);
							interceptor.setInterceptorInstance(interceptorInstance);
							interceptor.setType(clazz.getName());
							interceptorBeanMap.put(interceptor.getId(),interceptor);
							break;
						}
						objectMap.put(id,interceptorInstance);
					}
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}
}
