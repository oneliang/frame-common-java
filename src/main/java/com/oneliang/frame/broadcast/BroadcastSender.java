package com.oneliang.frame.broadcast;

public abstract interface BroadcastSender {

    /**
     * send broadcast message
     * @param message
     */
    public abstract void sendBroadcast(Message message);
}
