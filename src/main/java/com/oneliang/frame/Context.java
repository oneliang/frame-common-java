package com.oneliang.frame;

public abstract interface Context{

	/**
	 * <p>
	 * Method:context initialize
	 * </p>
	 * 
	 * @param parameters
	 */
	public abstract void initialize(String parameters);

	/**
	 * <p>
	 * Method:destroy
	 * </p>
	 * @throws Exception
	 */
	public abstract void destroy();
}
