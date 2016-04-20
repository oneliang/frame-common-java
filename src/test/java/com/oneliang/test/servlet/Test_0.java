package com.oneliang.test.servlet;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.oneliang.frame.jdbc.BaseQuery;
import com.oneliang.frame.jdbc.ConnectionPool;
import com.oneliang.frame.servlet.action.ActionExecuteException;
import com.oneliang.frame.servlet.action.CommonAction;
import com.oneliang.test.java.TestPO_0;
import com.oneliang.test.java.User;
import com.oneliang.util.common.TimeUtil;

public class Test_0 extends CommonAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6609423112226573822L;

	/**
	 * Constructor of the object.
	 */
	public Test_0() {
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
		
		TestPO_0 po=new TestPO_0();
		try {
			this.requestValuesToObject(request,po);
		} catch (Exception e){
			
		}
		this.setObjectToRequest(request,"po", po);
		List<Object> list=new ArrayList<Object>();
		list.add("1");
		list.add("2");
		list.add("3");
//		System.out.println("po"+po.getField_1());
		ConnectionPool connectionPool=null;//ConnecterImpl.getInstance();
		BaseQuery bc=null;//BaseQuery.getInstance();
		try {
			Connection connection=connectionPool.getResource();
			List<User> list2=bc.executeQueryBySql(connection, User.class,"select * from t_user");
			User use=new User();
			use.setIdTemp(TimeUtil.getMillisId());
			use.setLoginTime(TimeUtil.getTime());
			use.setNameTemp("nameTemp");
			use.setPasswordTemp("passwordTemp");
			bc.executeInsert(connection,use, "t_user");
			use.setNameTemp("LKHGH");
			bc.executeUpdate(connection, use, "t_user");
			if(connection.isClosed()){
				System.out.println("Connection is closed!");
			}else{
				System.out.println("Connection is not closed!!");
			}
			list2=bc.executeQueryBySql(connection, User.class,"select * from t_user");
			System.out.println(list2.size());
			for(int i=0;i<list2.size();i++){
				User user=(User)list2.get(i);
				System.out.println(i+":"+user.getIdTemp());
				System.out.println(i+":"+user.getNameTemp());
				System.out.println(i+":"+user.getPasswordTemp());
				System.out.println(i+":"+user.getLoginTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("po"+po.getField_1());
//		System.out.println("po"+po.getField_2());
//		System.out.println("po"+po.getField_3());
//		System.out.println("po"+po.getField_4());
//		System.out.println("po"+po.getField_5());
//		
//		System.out.println("po"+po.getField_6());
//		System.out.println("po"+po.getField_7());
//		System.out.println("po"+po.getField_8());
//		System.out.println("po"+po.getField_9());
//		System.out.println("po"+po.getField_10());
//		System.out.println("po"+po.getField_11());
//		System.out.println("po"+po.getField_12());
//		this.setObjectToRequest(request,"field_13",RequestUtil.checkBoxString(po.getField_13()));
//		this.setObjectToRequest(request,"field_14",RequestUtil.checkBoxString(po.getField_14()));
//		this.setObjectToRequest(request,"field_15",RequestUtil.checkBoxString(po.getField_15()));
//		this.setObjectToRequest(request,"field_16",RequestUtil.checkBoxString(po.getField_16()));
//		this.setObjectToRequest(request,"field_17",RequestUtil.checkBoxString(po.getField_17()));
//		this.setObjectToRequest(request,"field_18",RequestUtil.checkBoxString(po.getField_18()));
//		this.setObjectToRequest(request,"field_19",RequestUtil.checkBoxString(po.getField_19()));
//		this.setObjectToRequest(request,"field_20",RequestUtil.checkBoxString(po.getField_20()));
//		this.setObjectToRequest(request,"field_21",RequestUtil.checkBoxString(po.getField_21()));
//		this.setObjectToRequest(request,"field_22",RequestUtil.checkBoxString(po.getField_22()));
//		this.setObjectToRequest(request,"field_23",RequestUtil.checkBoxString(po.getField_23()));
//		this.setObjectToRequest(request,"field_24",RequestUtil.checkBoxString(po.getField_24()));
//		this.setObjectToRequest(request,"list", list);
		return "Test_0.page";
	}
	
}
