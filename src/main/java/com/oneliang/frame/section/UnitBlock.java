package com.oneliang.frame.section;

import java.io.InputStream;

import com.oneliang.util.common.MathUtil;

public class UnitBlock implements Block{

	public enum Endian{
		BIG,LITTLE
	}

	private Endian endian=Endian.BIG;
	private int initialSize=0;
	private byte[] value=null;
	protected int totalSize=0;
	public UnitBlock(Endian endian){
		this(endian,0);
	}
	public UnitBlock(int initialSize){
		this(Endian.BIG,initialSize);
	}
	public UnitBlock(Endian endian,int initialSize) {
		this.endian=endian;
		this.initialSize=initialSize;
	}

	public void parse(InputStream inputStream) throws Exception {
		byte[] buffer=new byte[this.initialSize];
		int length=inputStream.read(buffer);
		if(length==buffer.length){
			switch(this.endian){
			case LITTLE:
				buffer=MathUtil.byteArrayReverse(buffer);
				break;
			default:
				break;
			}
			this.value=buffer;
		}else{
			this.value=new byte[0];
		}
		this.totalSize=this.value.length;
	}

	/**
	 * @return the endian
	 */
	public Endian getEndian() {
		return endian;
	}
	/**
	 * @param initialSize the initialSize to set
	 */
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	/**
	 * set value
	 * @param value
	 */
	public void setValue(byte[] value) {
		this.value=value;
	}
	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}
	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * to byte array
	 * @return byte[]
	 */
	public byte[] toByteArray() {
		byte[] buffer=null;
		switch(this.endian){
		case LITTLE:
			buffer=MathUtil.byteArrayReverse(this.value);
			break;
		default:
			buffer=this.value;
			break;
		}
		return buffer;
	}
}
