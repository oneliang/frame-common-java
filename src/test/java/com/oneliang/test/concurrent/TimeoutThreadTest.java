package com.oneliang.test.concurrent;

import com.oneliang.util.logging.BaseLogger;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class TimeoutThreadTest {

    public static void main(String[] args) throws Exception {
        LoggerManager.registerLogger("*", new BaseLogger(Logger.Level.INFO));
        TimeoutThread timeoutThread = new TimeoutThread();
        timeoutThread.start();
        timeoutThread.addTimeoutItem(new TimeoutItem(5000));
        Thread.sleep(3000);
        timeoutThread.addTimeoutItem(new TimeoutItem(5000));
    }
}
