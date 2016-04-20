package com.oneliang.frame.ioc;

import java.lang.reflect.Method;
import java.util.List;

import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AnnotationContextUtil;
import com.oneliang.util.common.StringUtil;

public class AnnotationIocContext extends IocContext {

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
		try{
			List<Class<?>> classList=AnnotationContextUtil.parseAnnotationContextParameter(parameters, classLoader, classesRealPath, jarClassLoader, projectRealPath, Ioc.class);
			if(classList!=null){
				for(Class<?> clazz:classList){
					Ioc iocAnnotation=clazz.getAnnotation(Ioc.class);
					IocBean iocBean=new IocBean();
					String id=iocAnnotation.id();
					if(StringUtil.isBlank(id)){
						Class<?>[] classes=clazz.getInterfaces();
						if(classes!=null&&classes.length>0){
							id=classes[0].getSimpleName();
						}else{
							id=clazz.getSimpleName();
						}
						id=id.substring(0,1).toLowerCase()+id.substring(1);
					}
					iocBean.setId(id);
					iocBean.setType(clazz.getName());
					iocBean.setInjectType(iocAnnotation.injectType());
					iocBean.setProxy(iocAnnotation.proxy());
					iocBean.setBeanClass(clazz);
					//after inject
					Method[] methods=clazz.getMethods();
					for(Method method:methods){
						if(method.isAnnotationPresent(Ioc.AfterInject.class)){
							IocAfterInjectBean iocAfterInjectBean=new IocAfterInjectBean();
							iocAfterInjectBean.setMethod(method.getName());
							iocBean.addIocAfterInjectBean(iocAfterInjectBean);
						}
					}
					iocBeanMap.put(iocBean.getId(),iocBean);
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}
}
