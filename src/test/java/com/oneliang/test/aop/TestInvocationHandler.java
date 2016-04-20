package com.oneliang.test.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TestInvocationHandler implements InvocationHandler {
	private TestInterface impl=null;
	public TestInvocationHandler(TestInterface impl){
		this.impl=impl;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("method:"+method.getName());
		return method.invoke(impl, args);
	}

}
