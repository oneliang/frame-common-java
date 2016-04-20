package com.oneliang.frame.workflow;

import java.io.Serializable;
/**
 * the relation between the TaskNodes
 * @author Dandelion
 * @since 2009-6-15
 */
public class TaskNodeRelation implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6162961214862649223L;
	
	private String condition=null;
	private String betweenText=null;
	private Boolean relative=false;
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	/**
	 * @return the betweenText
	 */
	public String getBetweenText() {
		return betweenText;
	}
	/**
	 * @param betweenText the betweenText to set
	 */
	public void setBetweenText(String betweenText) {
		this.betweenText = betweenText;
	}
	public Boolean getRelative() {
		return relative;
	}
	public void setRelative(Boolean relative) {
		this.relative = relative;
	}
}
