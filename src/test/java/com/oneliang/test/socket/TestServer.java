package com.oneliang.test.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	private Socket socket;
	private ServerSocket serverSocket;

	public TestServer() throws IOException {
		serverSocket=new ServerSocket(7777);
		while (true) {
			socket=serverSocket.accept();
			Thread thread=new Thread(new ServerRunnableImpl(socket));
			thread.start();
		}
	}

	public static void main(String[] args) {
		try {
			new TestServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
