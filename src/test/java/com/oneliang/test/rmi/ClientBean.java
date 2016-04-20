package com.oneliang.test.rmi;

import java.io.Serializable;

public class ClientBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8352074071292606618L;
	
	private String organCode=null;
	private String routeCode=null;
	private String runDate=null;
	private String busCode=null;
	/**
	 * @return the organCode
	 */
	public final String getOrganCode() {
		return organCode;
	}
	/**
	 * @param organCode the organCode to set
	 */
	public final void setOrganCode(String organCode) {
		this.organCode = organCode;
	}
	/**
	 * @return the routeCode
	 */
	public final String getRouteCode() {
		return routeCode;
	}
	/**
	 * @param routeCode the routeCode to set
	 */
	public final void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	/**
	 * @return the runDate
	 */
	public final String getRunDate() {
		return runDate;
	}
	/**
	 * @param runDate the runDate to set
	 */
	public final void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	/**
	 * @return the busCode
	 */
	public final String getBusCode() {
		return busCode;
	}
	/**
	 * @param busCode the busCode to set
	 */
	public final void setBusCode(String busCode) {
		this.busCode = busCode;
	}
}
