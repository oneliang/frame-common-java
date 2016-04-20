package com.oneliang.frame.ioc.aop;

import java.lang.reflect.Method;

public abstract interface InvokeProcessor{

	/**
	 * invoke
	 * @param object
	 * @param method
	 * @param args
	 * @return Object
	 */
	public abstract Object invoke(Object object, Method method, Object[] args) throws Throwable;
}
