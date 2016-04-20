package com.oneliang.test.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiInterface extends Remote{

	public abstract String save(List<ClientBean> clientBeanList) throws RemoteException;;
}
