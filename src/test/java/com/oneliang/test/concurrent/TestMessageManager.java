package com.oneliang.test.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestMessageManager implements Runnable{
    private final Queue<Object> messageQueue=new ConcurrentLinkedQueue<Object>();
    private Thread thread=null;

    public synchronized void start(){
        if(this.thread==null){
            this.thread=new Thread(this);
            this.thread.start();
        }
    }
    public void run() {
        while(true){
            try{
                if(!this.messageQueue.isEmpty()){
                    Object message=messageQueue.poll();
                    if(message!=null){
                        handleMessage(message);
                    }
                }else{
                    synchronized (this) {
                        this.wait();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void handleMessage(Object message){
        
    }

    public void addMessage(Object message) {
        if(message!=null){
            this.messageQueue.add(message);
            synchronized (this) {
                this.notify();
            }
        }
    }

    public static void main(String[] args){
        TestMessageManager messageManager=new TestMessageManager();
        messageManager.start();
    }
}
