package com.oneliang.frame.ioc.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AopInvocationHandler<T extends Object> implements InvocationHandler {

	private T interfaceImpl=null;
	
	private static final List<BeforeInvokeProcessor> beforeInvokeProcessorList=new CopyOnWriteArrayList<BeforeInvokeProcessor>();
	private static final List<AfterReturningProcessor> afterReturningProcessorList=new CopyOnWriteArrayList<AfterReturningProcessor>();
	private static final List<AfterThrowingProcessor> afterThrowingProcessorList=new CopyOnWriteArrayList<AfterThrowingProcessor>();
	private static InvokeProcessor invokeProcessor=new DefaultInvokeProcessor();

	/**
	 * constructor
	 * @param interfaceImpl
	 */
	public AopInvocationHandler(T interfaceImpl){
		this.interfaceImpl=interfaceImpl;
	}
	
	/**
	 * proxy method invoke
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		logger.log("proxy invocation:"+interfaceImpl.getClass().getName()+"--"+method.getName());
		Object object=null;
		for(BeforeInvokeProcessor beforeInvokeProcessor:beforeInvokeProcessorList){
			beforeInvokeProcessor.beforeInvoke(this.interfaceImpl, method, args);
		}
		try{
			object=invokeProcessor.invoke(this.interfaceImpl, method, args);
		}catch (Exception e) {
			for(AfterThrowingProcessor afterThrowingProcessor:afterThrowingProcessorList){
				afterThrowingProcessor.afterThrowing(this.interfaceImpl, method, args, e);
			}
			throw e;
		}
		for(AfterReturningProcessor afterReturningProcessor:afterReturningProcessorList){
			afterReturningProcessor.afterReturning(this.interfaceImpl, method, args, object);
		}
		return object;
	}
	
	/**
	 * add BeforeInvokeProcessor
	 * @param beforeInvokeProcessor
	 */
	public static void addBeforeInvokeProcessor(BeforeInvokeProcessor beforeInvokeProcessor){
		beforeInvokeProcessorList.add(beforeInvokeProcessor);
	}
	
	/**
	 * add AfterReturningProcessor
	 * @param afterReturningProcessor
	 */
	public static void addAfterReturningProcessor(AfterReturningProcessor afterReturningProcessor){
		afterReturningProcessorList.add(afterReturningProcessor);
	}
	
	/**
	 * add AfterThrowingProcessor
	 */
	public static void addAfterThrowingProcessor(AfterThrowingProcessor afterThrowingProcessor){
		afterThrowingProcessorList.add(afterThrowingProcessor);
	}

	/**
	 * @param invokeProcessor the invokeProcessor to set
	 */
	public static void setInvokeProcessor(InvokeProcessor invokeProcessor) {
		AopInvocationHandler.invokeProcessor = invokeProcessor;
	}
	private static final class DefaultInvokeProcessor implements InvokeProcessor {
		private static final long serialVersionUID = 1361983725058070389L;

		public Object invoke(Object object, Method method, Object[] args) throws Throwable {
			return method.invoke(object, args);
		}
	}
}