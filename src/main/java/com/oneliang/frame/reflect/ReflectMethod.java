package com.oneliang.frame.reflect;

import java.lang.reflect.Method;

public class ReflectMethod<C, ReturnType> extends ThrowableChain {

	private Method method = null;

	/**
	 * constructor
	 * 
	 * @param clazz
	 * @param methodName
	 * @param methodParameterType
	 * @param modifier
	 */
	public ReflectMethod(Class<?> clazz, String methodName, Class<?>[] methodParameterType, int modifier) {
		if (clazz != null) {
			try {
				this.method = clazz.getDeclaredMethod(methodName, methodParameterType);
				if (modifier > 0 && (method.getModifiers() & modifier) != modifier) {
					this.addThrowable(new ReflectException(method + " does not match modifiers: " + modifier));
				}
				this.method.setAccessible(true);
			} catch (Exception e) {
				this.addThrowable(new ReflectException(e));
			}
		}
	}

	/**
	 * invoke
	 * 
	 * @param object
	 * @param parameters
	 * @return ReturnType
	 * @throws ReflectException
	 */
	@SuppressWarnings("unchecked")
	public ReturnType invoke(C object, Object[] parameters) throws ReflectException {
		ReturnType value = null;
		try {
			value = (ReturnType) this.method.invoke(object, parameters);
		} catch (Exception e) {
			throw new ReflectException(e);
		}
		return value;
	}

}
