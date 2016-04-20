package com.oneliang.test.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.oneliang.frame.servlet.action.ActionExecuteException;
import com.oneliang.frame.servlet.action.CommonAction;

public class Test_1 extends CommonAction {

	/**
	 * <p>Property: serialVersionUID</p>
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5369996550861900039L;

	/**
	 * Constructor of the object.
	 */
	public Test_1() {
		super();
	}

	/**
	 * Returns information about the servlet, such as 
	 * author, version, and copyright. 
	 *
	 * @return String information about this servlet
	 */
	public String getServletInfo() {
		return this.getClass().toString();
	}


	public String execute(ServletRequest request, ServletResponse response)
			throws ActionExecuteException {
		// TODO Auto-generated method stub
		System.out.println("~~~~~Test_1 execute()~~~~~");
		System.out.println("~~~~~Test_1 "+request.getParameter("name"));
		
		return null;
	}

}
