package com.oneliang.frame.reflect;

import java.lang.reflect.Modifier;

public class ReflectClass<C> extends ThrowableChain {

	private Class<C> clazz = null;

	/**
	 * constructor
	 * 
	 * @param clazz
	 */
	public ReflectClass(Class<C> clazz) {
		this.clazz = clazz;
	}

	/**
	 * constructor
	 * 
	 * @param className
	 */
	@SuppressWarnings("unchecked")
	public ReflectClass(String className) {
		try {
			this.clazz = (Class<C>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			this.addThrowable(e);
		}
	}

	/**
	 * get static field
	 * 
	 * @param <FieldClass>
	 * @param fieldName
	 * @return ReflectField<C,FieldClass>
	 */
	public <FieldClass> ReflectField<C, FieldClass> getStaticField(String fieldName) {
		return this.getField(fieldName, Modifier.STATIC);
	}

	/**
	 * get declared field
	 * 
	 * @param <FieldClass>
	 * @param fieldName
	 * @return ReflectField<C,FieldClass>
	 */
	public <FieldClass> ReflectField<C, FieldClass> getDeclaredField(String fieldName) {
		return this.getField(fieldName, 0);
	}

	/**
	 * get field
	 * 
	 * @param <FieldClass>
	 * @param fieldName
	 * @return ReflectField<C,FieldClass>
	 */
	private <FieldClass> ReflectField<C, FieldClass> getField(String fieldName, int modifier) {
		return new ReflectField<C, FieldClass>(this.clazz, fieldName, modifier);
	}

	/**
	 * get static method
	 * 
	 * @param <ReturnType>
	 * @param methodName
	 * @param methodParameterType
	 * @return ReflectMethod<C,ReturnType>
	 */
	public <ReturnType> ReflectMethod<C, ReturnType> getStaticMethod(String methodName, Class<?>[] methodParameterType) {
		return this.getMethod(methodName, methodParameterType, Modifier.STATIC);
	}

	/**
	 * get declared method
	 * 
	 * @param <ReturnType>
	 * @param methodName
	 * @param methodParameterType
	 * @return ReflectMethod<C,ReturnType>
	 */
	public <ReturnType> ReflectMethod<C, ReturnType> getDeclaredMethod(String methodName, Class<?>[] methodParameterType) {
		return this.getMethod(methodName, methodParameterType, 0);
	}

	/**
	 * get method
	 * 
	 * @param <ReturnType>
	 * @param methodName
	 * @param methodParameterType
	 * @param modifier
	 * @return ReflectMethod<C,ReturnType>
	 */
	private <ReturnType> ReflectMethod<C, ReturnType> getMethod(String methodName, Class<?>[] methodParameterType, int modifier) {
		return new ReflectMethod<C, ReturnType>(this.clazz, methodName, methodParameterType, modifier);
	}

	/**
	 * @return the clazz
	 */
	public Class<C> getClazz() {
		return clazz;
	}
}
