package com.oneliang.frame.reflect;

import java.lang.reflect.Field;

public class ReflectField<C, FieldType> extends ThrowableChain {

	private Field field = null;

	/**
	 * constructor
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param modified
	 */
	public ReflectField(Class<?> clazz, String fieldName, int modified) {
		if (clazz != null) {
			try {
				this.field = clazz.getDeclaredField(fieldName);
				if (modified > 0 && (field.getModifiers() & modified) != modified) {
					this.addThrowable(new ReflectException(field + " does not match modifiers: " + modified));
				}
				this.field.setAccessible(true);
			} catch (Exception e) {
				this.addThrowable(e);
			}
		}
	}

	/**
	 * get
	 * 
	 * @param object
	 * @return T
	 * @throws ReflectException
	 */
	@SuppressWarnings("unchecked")
	public FieldType get(C object) throws ReflectException {
		FieldType value = null;
		if (this.field != null) {
			try {
				return (FieldType) this.field.get(object);
			} catch (Exception e) {
				throw new ReflectException(e);
			}
		}
		return value;
	}

	/**
	 * set
	 * 
	 * @param object
	 * @param value
	 * @throws ReflectException
	 */
	public void set(C object, FieldType value) throws ReflectException {
		try {
			this.field.set(object, value);
		} catch (Exception e) {
			throw new ReflectException(e);
		}
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}
}
