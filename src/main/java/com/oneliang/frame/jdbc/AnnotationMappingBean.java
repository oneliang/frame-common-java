package com.oneliang.frame.jdbc;

import java.util.Arrays;


public class AnnotationMappingBean extends MappingBean {

	private String condition=null;
	private boolean dropIfExist=false;
	private String[] createTableSqls=null;

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
	 * @return the createTableSqls
	 */
	public String[] getCreateTableSqls() {
		return createTableSqls;
	}

	/**
	 * @param createTableSqls the createTableSqls to set
	 */
	public void setCreateTableSqls(String[] createTableSqls) {
		if(createTableSqls!=null){
			this.createTableSqls = Arrays.copyOf(createTableSqls,createTableSqls.length);
		}
	}

	/**
	 * @return the dropIfExist
	 */
	public boolean isDropIfExist() {
		return dropIfExist;
	}

	/**
	 * @param dropIfExist the dropIfExist to set
	 */
	public void setDropIfExist(boolean dropIfExist) {
		this.dropIfExist = dropIfExist;
	}
}
