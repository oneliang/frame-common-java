package com.oneliang.frame.section;

public class UnitSection implements Section {

	private byte[] byteArray=null;

	public UnitSection(byte[] byteArray) {
		this.byteArray=byteArray;
	}

	public byte[] toByteArray() {
		return this.byteArray;
	}
}
