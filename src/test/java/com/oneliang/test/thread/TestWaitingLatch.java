package com.oneliang.test.thread;

import com.oneliang.util.concurrent.WaitingLatch;


public class TestWaitingLatch{
	
	public static void main(String[] args){
		WaitingLatch waitingLatch=new WaitingLatch();
		waitingLatch.addRunnable(new Runnable(){
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("run 1");
			}
		});
		waitingLatch.addRunnable(new Runnable(){
			public void run() {
				System.out.println("run 2");
			}
		});
		waitingLatch.addRunnable(new Runnable(){
			public void run() {
				System.out.println("run 3");
			}
		});
		waitingLatch.addRunnable(new Runnable(){
			public void run() {
				System.out.println("run 4");
			}
		});
		waitingLatch.startAll();
		waitingLatch.waiting();
		System.out.println("---all finish---");
	}
}