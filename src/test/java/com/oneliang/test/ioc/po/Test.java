package com.oneliang.test.ioc.po;

import java.io.Serializable;

public class Test implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9202888199997465104L;
	
	private User user=null;
	private Admin admin=null;
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		System.out.println(user);
		this.user = user;
	}
	/**
	 * @return the admin
	 */
	public Admin getAdmin() {
		return admin;
	}
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Admin admin) {
		System.out.println(admin);
		this.admin = admin;
	}
}
