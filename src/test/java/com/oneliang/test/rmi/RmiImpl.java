package com.oneliang.test.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiImpl extends UnicastRemoteObject implements RmiInterface{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8938566970760433895L;

	public RmiImpl() throws RemoteException {
		super();
	}

	public String save(List<ClientBean> clientBeanList)	throws RemoteException {
		System.out.println(clientBeanList.size());
		return "success";
	}
}
