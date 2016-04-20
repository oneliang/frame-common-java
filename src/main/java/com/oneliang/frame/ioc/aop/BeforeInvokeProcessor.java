package com.oneliang.frame.ioc.aop;

import java.lang.reflect.Method;

public abstract interface BeforeInvokeProcessor{

	/**
	 * before invoke
	 * @param object
	 * @param method
	 * @param args
	 * @throws Throwable
	 */
	public abstract void beforeInvoke(Object object,Method method, Object[] args) throws Throwable;
}
