package com.oneliang.frame.ioc.aop;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataCacheInvokeProcessor implements InvokeProcessor {

	private static Map<Thread,Method> threadMethodMap=new ConcurrentHashMap<Thread,Method>();
	private static Map<Method,MethodUpdateInvoke> methodUpdateInvokeMap=new ConcurrentHashMap<Method,MethodUpdateInvoke>();

	public Object invoke(Object object, Method method, Object[] args) throws Throwable {
		Object returnValue=null;
		Method objectMethod=object.getClass().getMethod(method.getName(), method.getParameterTypes());
		if(objectMethod.isAnnotationPresent(DataCache.class)&&(args==null||args.length==0)){
			if(methodUpdateInvokeMap.containsKey(objectMethod)){
				returnValue=methodUpdateInvokeMap.get(objectMethod).dataCache;
			}else{
				synchronized(method){
					if(methodUpdateInvokeMap.containsKey(objectMethod)){
						returnValue=methodUpdateInvokeMap.get(objectMethod).dataCache;
					}else{
						DataCache dataCache=objectMethod.getAnnotation(DataCache.class);
						returnValue=method.invoke(object, args);
						MethodUpdateInvoke methodUpdateInvoke=new MethodUpdateInvoke();
						methodUpdateInvoke.method=method;
						methodUpdateInvoke.interfaceImpl=object;
						methodUpdateInvoke.dataCache=returnValue;
						methodUpdateInvoke.updateTime=dataCache.updateTime();
						methodUpdateInvokeMap.put(objectMethod, methodUpdateInvoke);
						Thread thread=new Thread(methodUpdateInvoke);
						threadMethodMap.put(thread, objectMethod);
						thread.start();
					}
				}
			}
		}else if(objectMethod.isAnnotationPresent(DataCacheUpdate.class)){
			DataCacheUpdate dataCacheUpdate=objectMethod.getAnnotation(DataCacheUpdate.class);
			String dataCacheMethodName=dataCacheUpdate.dataCacheMethod();
			Method dataCacheMethod=object.getClass().getMethod(dataCacheMethodName);
			if(methodUpdateInvokeMap.containsKey(dataCacheMethod)){
				Object dataCache=methodUpdateInvokeMap.get(dataCacheMethod).dataCache;
				if(args!=null&&args.length>0){
					args[args.length-1]=dataCache;
				}
			}
			returnValue=method.invoke(object, args);
		}else{
			returnValue=method.invoke(object, args);
		}
		return returnValue;
	}

	public static class MethodUpdateInvoke implements Runnable{
		Method method=null;
		Object interfaceImpl=null;
		Object dataCache=null;
		long updateTime=10000;
		public void run() {
			while(true){
				try {
					if(this.method!=null&&this.interfaceImpl!=null){
						Thread.sleep(this.updateTime);
						this.dataCache=this.method.invoke(this.interfaceImpl);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
