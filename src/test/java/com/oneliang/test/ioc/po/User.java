package com.oneliang.test.ioc.po;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1993641840700209456L;
	
	private Admin admin=null;

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Admin admin) {
		System.out.println(admin);
		this.admin = admin;
	}
}
