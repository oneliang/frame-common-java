package com.oneliang.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
	private Socket client;
	private PrintStream printStream;
	
	public TestClient(String text) throws UnknownHostException, IOException {
		client = new Socket("127.0.0.1", 7777);
		PrintWriter printWriter=new PrintWriter(client.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
//		printStream = new PrintStream(client.getOutputStream());
		printWriter.println(text);
		printWriter.flush();
		while(true){
			System.out.println("server say: " + br.readLine());
		}
//		printWriter.close();
	}

	public static void main(String[] args) {
		for(int i=0;i<1;i++){
			final String text=String.valueOf("Thread:"+i);
			new Thread(new Runnable(){
				public void run() {
					try {
						Socket client = new Socket("127.0.0.1", 7777);
						PrintWriter printWriter=new PrintWriter(client.getOutputStream());
						BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						while(true){
							System.out.println("-----send-----");
							printWriter.println(text);
							printWriter.flush();
							while(true){
								System.out.println("server say: " + br.readLine());
							}
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
//		try {
//			new TestClient("hello.");
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
