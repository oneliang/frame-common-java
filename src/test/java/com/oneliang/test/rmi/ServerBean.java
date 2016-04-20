package com.oneliang.test.rmi;

import java.io.Serializable;

public class ServerBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2128549188743596188L;
	private String field=null;

	/**
	 * @return the field
	 */
	public final String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public final void setField(String field) {
		this.field = field;
	}
}
