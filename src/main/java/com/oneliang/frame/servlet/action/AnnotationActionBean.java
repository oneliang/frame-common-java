package com.oneliang.frame.servlet.action;

import java.lang.reflect.Method;

public class AnnotationActionBean extends ActionBean {

	private Method method=null;
	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
}
