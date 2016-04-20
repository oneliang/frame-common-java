package com.oneliang.frame.ioc.aop;

import java.lang.reflect.Method;

public abstract interface AfterThrowingProcessor {

	/**
	 * after throwing
	 * @param object
	 * @param method
	 * @param args
	 * @param exception
	 */
	public abstract void afterThrowing(Object object,Method method,Object[] args,Exception exception);
}
