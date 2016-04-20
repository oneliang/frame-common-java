package com.oneliang.frame.reflect;

public class ReflectException extends Exception {

	private static final long serialVersionUID = -6686119484183341493L;

	public ReflectException(String message) {
		super(message);
	}

	public ReflectException(Throwable cause) {
		super(cause);
	}

	public ReflectException(String message, Throwable cause) {
		super(message, cause);
	}

	public String toString() {
        return getCause() != null ? getClass().getName() + ": " + getCause() : super.toString();
    }
}
