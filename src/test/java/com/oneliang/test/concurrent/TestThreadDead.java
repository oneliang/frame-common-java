package com.oneliang.test.concurrent;

public class TestThreadDead {
	public static void main(String[] args) throws Exception{
		Thread thread=new Thread(){
			public void run() {
				int i=0;
				while(true){
					try {
						String a=null;
						if(i%5==0){
							a.length();
						}
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						continue;
					}
					i++;
				}
			}
		};
		System.out.println(thread.isAlive());
		thread.start();
		while(true){
			System.out.println(thread.isAlive());
			Thread.State state=thread.getState();
			if(state==Thread.State.TERMINATED){
				thread=new Thread(){
					public void run() {
						int i=0;
						while(true){
							try {
								String a=null;
								if(i%5==0){
									a.length();
								}
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
								continue;
							}
							i++;
						}
					}
				};
				thread.start();
			}
			Thread.sleep(5000);
		}
	}
}
