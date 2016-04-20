package com.oneliang.test.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
	public static void main(String[] args) {
		try {
			//启动RMI注册服务，指定端口为1099　（1099为默认端口）
			//也可以通过命令 ＄java_home/bin/rmiregistry 1099启动
			//这里用这种方式避免了再打开一个DOS窗口
			//而且用命令rmiregistry启动注册服务还必须事先用RMIC生成一个stub类为它所用
			LocateRegistry.createRegistry(1099);

			//创建远程对象的一个或多个实例，下面是hello对象
			//可以用不同名字注册不同的实例
			RmiInterface rmi = new RmiImpl();

			//把hello注册到RMI注册服务器上，命名为Hello
			Naming.rebind("//127.0.0.1:1099/RMI", rmi);

			//如果要把hello实例注册到另一台启动了RMI注册服务的机器上
			//Naming.rebind("//192.168.1.105:1099/Hello",hello);

			System.out.println("Hello Server is ready.");
		} catch (Exception e) {
			System.out.println("Hello Server failed: " + e);
		}
	}
}
