package com.oneliang.test.ioc.po;

import java.io.Serializable;

public class Admin implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5344144802674734924L;
	
	private User user=null;

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		System.out.println(user);
		this.user = user;
	}
}
