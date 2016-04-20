package com.oneliang.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerRunnableImpl implements Runnable{

	private Socket socket=null;
	private BufferedReader bufferedReader=null;
	private PrintWriter printWriter=null;
	
	public ServerRunnableImpl(Socket socket){
		this.socket=socket;
		try {
			printWriter=new PrintWriter(this.socket.getOutputStream());
			bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
//		while(true){
			try{
				printWriter.println("welcome!");
				printWriter.flush();
				String str=bufferedReader.readLine();
				System.out.println(socket.hashCode()+"you input is : " + str);
				Thread.sleep(10000);
				printWriter.println("10 seconds latter..");
				printWriter.flush();
			}catch (Exception e) {
				e.printStackTrace();
			}
//		}
	}
}
