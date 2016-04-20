package com.oneliang.test.schedule;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -137709175285285258L;

	private String name=null;
	private Date beginDate=null;
	private Date endDate=null;
	private long beginDateInMillis=0;
	private long endDateInMillis=0;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the beginDateInMillis
	 */
	public long getBeginDateInMillis() {
		return beginDateInMillis;
	}
	/**
	 * @param beginDateInMillis the beginDateInMillis to set
	 */
	public void setBeginDateInMillis(long beginDateInMillis) {
		this.beginDateInMillis = beginDateInMillis;
	}
	/**
	 * @return the endDateInMillis
	 */
	public long getEndDateInMillis() {
		return endDateInMillis;
	}
	/**
	 * @param endDateInMillis the endDateInMillis to set
	 */
	public void setEndDateInMillis(long endDateInMillis) {
		this.endDateInMillis = endDateInMillis;
	}
}
