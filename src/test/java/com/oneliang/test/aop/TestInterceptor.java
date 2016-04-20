package com.oneliang.test.aop;

import java.lang.reflect.Method;

import com.oneliang.frame.ioc.aop.AfterReturningProcessor;
import com.oneliang.frame.ioc.aop.AfterThrowingProcessor;
import com.oneliang.frame.ioc.aop.BeforeInvokeProcessor;

public class TestInterceptor implements BeforeInvokeProcessor, AfterReturningProcessor, AfterThrowingProcessor {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -21951626912214368L;

	public void beforeInvoke(Object target, Method method, Object[] args) throws Throwable {
		System.out.println("before");
	}

	public void afterReturning(Object target, Method method, Object[] args, Object returnValue) throws Throwable {
		System.out.println("after");
	}

	public void afterThrowing(Object object, Method method, Object[] args, Exception exception) {
		System.out.println("throwing");
	}
}
