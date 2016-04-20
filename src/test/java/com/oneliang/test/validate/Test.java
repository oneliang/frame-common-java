package com.oneliang.test.validate;

import com.oneliang.util.validate.annotation.Decimal;
import com.oneliang.util.validate.annotation.Numeric;
import com.oneliang.util.validate.annotation.Regex;

public class Test {

	@Regex("^[A-Za-z]*$")
	private String test=null;

	@Regex(Regex.POSITIVE_INTEGER)
	private int number=00012;

	@Numeric(min=1,max=20)
	private int numeric=0;

	@Decimal(min=1.0,max=5)
	private float aabb=0f;

	/**
	 * @return the test
	 */
	public String getTest() {
		return test;
	}

	/**
	 * @param test the test to set
	 */
	public void setTest(String test) {
		this.test = test;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the numeric
	 */
	public int getNumeric() {
		return numeric;
	}

	/**
	 * @param numeric the numeric to set
	 */
	public void setNumeric(int numeric) {
		this.numeric = numeric;
	}

	/**
	 * @return the aabb
	 */
	public float getAabb() {
		return aabb;
	}

	/**
	 * @param aabb the aabb to set
	 */
	public void setAabb(float aabb) {
		this.aabb = aabb;
	}
}
