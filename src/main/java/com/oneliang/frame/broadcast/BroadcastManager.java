package com.oneliang.frame.broadcast;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.oneliang.Constant;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class BroadcastManager implements BroadcastSender, Runnable {

    private static final Logger logger = LoggerManager.getLogger(BroadcastManager.class);

    protected Map<String, List<BroadcastReceiver>> broadcastReceiverMap = new ConcurrentHashMap<String, List<BroadcastReceiver>>();
    protected Queue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
    protected Thread thread = null;

    /**
     * start
     */
    public synchronized void start() {
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    /**
     * interrupt
     */
    public synchronized void interrupt() {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
    }

    /**
     * register broadcast receiver
     * 
     * @param actionFilter
     * @param broadcastReceiver
     */
    public void registerBroadcastReceiver(String[] actionFilter, BroadcastReceiver broadcastReceiver) {
        if (actionFilter == null || actionFilter.length == 0) {
            return;
        }
        for (String actionKey : actionFilter) {
            if (actionKey == null) {
                continue;
            }
            List<BroadcastReceiver> broadcastReceiverList = null;
            if (this.broadcastReceiverMap.containsKey(actionKey)) {
                broadcastReceiverList = this.broadcastReceiverMap.get(actionKey);
            } else {
                broadcastReceiverList = new CopyOnWriteArrayList<BroadcastReceiver>();
                this.broadcastReceiverMap.put(actionKey, broadcastReceiverList);
            }
            broadcastReceiverList.add(broadcastReceiver);
        }
    }

    /**
     * unregister broadcast receiver
     * 
     * @param broadcastReceiver
     */
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver == null) {
            return;
        }
        Iterator<Entry<String, List<BroadcastReceiver>>> iterator = broadcastReceiverMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, List<BroadcastReceiver>> entry = iterator.next();
            List<BroadcastReceiver> broadcastReceiverList = entry.getValue();
            if (broadcastReceiverList.contains(broadcastReceiver)) {
                broadcastReceiverList.remove(broadcastReceiver);
            }
        }
    }

    /**
     * send broadcast message
     * 
     * @param message
     */
    public void sendBroadcast(Message message) {
        if (message != null) {
            this.messageQueue.add(message);
            synchronized (this) {
                this.notify();
            }
        }
    }

    /**
     * run
     */
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (!this.messageQueue.isEmpty()) {
                    Message message = this.messageQueue.poll();
                    handleMessage(message);
                } else {
                    synchronized (this) {
                        this.wait();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(Constant.Base.EXCEPTION, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * handle message
     * 
     * @param message
     */
    protected void handleMessage(Message message) {
        if (message == null) {
            return;
        }
        List<String> actionList = message.getActionList();
        if (actionList == null || actionList.isEmpty()) {
            return;
        }
        for (String action : actionList) {
            if (!broadcastReceiverMap.containsKey(action)) {
                continue;
            }
            List<BroadcastReceiver> broadcastReceiverList = broadcastReceiverMap.get(action);
            if (broadcastReceiverList == null || broadcastReceiverList.isEmpty()) {
                continue;
            }
            List<Class<?>> classList = message.getClassList();
            if (classList == null || classList.isEmpty()) {
                for (BroadcastReceiver broadcastReceiver : broadcastReceiverList) {
                    if (broadcastReceiver != null) {
                        broadcastReceiver.receive(action, message);
                    }
                }
            } else {
                for (BroadcastReceiver broadcastReceiver : broadcastReceiverList) {
                    if (broadcastReceiver != null && classList.contains(broadcastReceiver.getClass())) {
                        broadcastReceiver.receive(action, message);
                    }
                }
            }
        }
    }
}
