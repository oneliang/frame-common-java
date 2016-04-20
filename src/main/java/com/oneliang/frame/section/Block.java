package com.oneliang.frame.section;

import java.io.InputStream;

public interface Block extends Section{

	/**
	 * set initial size
	 * @param initialSize
	 */
	public void setInitialSize(int initialSize);

	/**
	 * set value
	 * @param value
	 */
	public void setValue(byte[] value);

	/**
	 * @return the value
	 */
	public byte[] getValue();

	/**
	 * @return the totalSize
	 */
	public int getTotalSize();

	/**
	 * parse
	 * 
	 * @param inputStream
	 * @throws Exception
	 */
	public abstract void parse(InputStream inputStream) throws Exception;
}
