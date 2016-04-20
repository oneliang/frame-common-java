package com.oneliang.frame.jdbc;

public class MappingColumnBean{

	public static final String TAG_COLUMN="column";
	
	private String field=null;
	private String column=null;
	private boolean isId=false;
	
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	/**
	 * @return the isId
	 */
	public boolean getIsId() {
		return isId;
	}
	/**
	 * @param isId the isId to set
	 */
	public void setIsId(boolean isId) {
		this.isId = isId;
	}
}
