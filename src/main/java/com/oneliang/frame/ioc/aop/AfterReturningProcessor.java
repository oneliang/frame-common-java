package com.oneliang.frame.ioc.aop;

import java.lang.reflect.Method;

public abstract interface AfterReturningProcessor{

	/**
	 * after returning
	 * @param object
	 * @param method
	 * @param args
	 * @param returnValue
	 * @throws Throwable
	 */
	public abstract void afterReturning(Object object, Method method,Object[] args, Object returnValue) throws Throwable;
}
