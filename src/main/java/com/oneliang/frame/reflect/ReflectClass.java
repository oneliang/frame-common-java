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
	 * @param <FieldType>
	 * @param fieldName
	 * @return ReflectField<C,FieldType>
	 */
	public <FieldType> ReflectField<C, FieldType> getStaticField(String fieldName) {
		return this.getField(fieldName, Modifier.STATIC);
	}

	/**
	 * get declared field
	 * 
	 * @param <FieldType>
	 * @param fieldName
	 * @return ReflectField<C,FieldType>
	 */
	public <FieldType> ReflectField<C, FieldType> getDeclaredField(String fieldName) {
		return this.getField(fieldName, 0);
	}

	/**
	 * get field
	 * 
	 * @param <FieldType>
	 * @param fieldName
	 * @return ReflectField<C,FieldType>
	 */
	private <FieldType> ReflectField<C, FieldType> getField(String fieldName, int modifier) {
		return new ReflectField<C, FieldType>(this.clazz, fieldName, modifier);
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
