package com.oneliang.frame.section;

import com.oneliang.util.common.StringUtil;

public class SectionPosition {

	private int fromIndex=-1;
	private int toIndex=-1;
	private byte[] byteArray=null;

	public SectionPosition(int fromIndex) {
		this(fromIndex,-1);
	}

	public SectionPosition(int fromIndex,int toIndex){
		this(fromIndex,toIndex,null);
	}

	public SectionPosition(int fromIndex,int toIndex,byte[] byteArray) {
		this.fromIndex=fromIndex;
		this.toIndex=toIndex;
		this.byteArray=byteArray;
	}

	/**
	 * @return the fromIndex
	 */
	public int getFromIndex() {
		return fromIndex;
	}

	/**
	 * @return the toIndex
	 */
	public int getToIndex() {
		return toIndex;
	}

	public String toString() {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("(from,to");
		if(this.byteArray!=null){
			stringBuilder.append(",value");
		}
		stringBuilder.append(")(");
		stringBuilder.append(this.fromIndex+",");
		stringBuilder.append(this.toIndex);
		if(this.byteArray!=null){
			stringBuilder.append(","+StringUtil.byteArrayToHexString(this.byteArray));
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	/**
	 * @return the byteArray
	 */
	public byte[] getByteArray() {
		return byteArray;
	}
}
