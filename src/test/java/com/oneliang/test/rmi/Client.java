package com.oneliang.test.rmi;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.List;


public class Client {

	public static void main(String[] args){
		try {
			System.setSecurityManager(new RMISecurityManager());
			RmiInterface rmi=(RmiInterface)Naming.lookup("//127.0.0.1:1099/RMI");
			List<ClientBean> clientBeanList=new ArrayList<ClientBean>();
			for(int i=0;i<400000;i++){
				ClientBean clientBean=new ClientBean();
				clientBean.setBusCode("busCode");
				clientBean.setOrganCode("organCode");
				clientBean.setRouteCode("routeCode");
				clientBean.setRunDate("runDate");
				clientBeanList.add(clientBean);
			}
			System.out.println(rmi.save(clientBeanList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}