package com.oneliang.frame.broadcast;

public abstract interface BroadcastReceiver {

    /**
     * receive
     * @param action
     * @param message
     */
    public abstract void receive(String action,Message message);
}
