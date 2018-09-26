package com.oneliang.test.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.oneliang.Constants;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class TimeoutThread implements Runnable {

    private static final Logger logger = LoggerManager.getLogger(TimeoutThread.class);

    private Queue<TimeoutItem> timeoutItemQueue = new ConcurrentLinkedQueue<TimeoutItem>();
    private Thread thread = null;
    private boolean needToInterrupt = false;

    private long lastLoopTime = 0l;
    private long maxTimeout = 0;
    private boolean timeoutWait = false;

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (lastLoopTime == 0) {
                lastLoopTime = System.currentTimeMillis();
            }
            try {
                if (!this.timeoutItemQueue.isEmpty()) {
                    TimeoutItem timeoutItem = this.timeoutItemQueue.poll();
                    int timeout = timeoutItem.timeout;
                    if (timeout == 0) {
                        continue;
                    }
                    logger.info("before caculate:" + maxTimeout);
                    if (maxTimeout <= 0) {
                        maxTimeout = timeout;
                    } else {
                        long elapseTime = System.currentTimeMillis() - lastLoopTime;
                        maxTimeout = maxTimeout - elapseTime;
                        logger.info("next timeout:" + maxTimeout + ",elapse time:" + elapseTime);
                        if (maxTimeout <= timeoutItem.timeout) {
                            maxTimeout = timeoutItem.timeout;
                        }
                    }
                    logger.info("after caculate:" + maxTimeout);
                } else {
                    synchronized (this) {
                        // check for the scene which notify first,so do it in
                        // synchronized block
                        if (this.needToInterrupt) {
                            this.realInterrupt();
                        }
                        if (maxTimeout > 0 && !timeoutWait) {
                            logger.info("wait for " + maxTimeout);
                            timeoutWait = true;
                            this.wait(maxTimeout);
                        } else {
                            logger.info("long wait");
                            maxTimeout = 0;
                            timeoutWait = false;
                            this.wait();
                        }
                        logger.info("after timeout..");
                    }
                }
            } catch (InterruptedException e) {
                logger.verbose("need to interrupt:" + e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error(Constants.Base.EXCEPTION, e);
            }
            lastLoopTime = System.currentTimeMillis();
        }
    }

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
    public void interrupt() {
        this.needToInterrupt = true;
        this.dealNotify();
    }

    /**
     * real interrupt
     */
    private void realInterrupt() {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
            this.needToInterrupt = false;
        }
    }

    /**
     * add timeout item
     * 
     * @param timeoutItem
     */
    public void addTimeoutItem(TimeoutItem timeoutItem) {
        if (timeoutItem != null) {
            this.timeoutItemQueue.add(timeoutItem);
            this.dealNotify();
        }
    }

    /**
     * deal notify
     */
    private void dealNotify() {
        synchronized (this) {
            if (timeoutWait) {
                timeoutWait = false;
            }
            this.notify();
        }
    }

    /**
     * finalize
     */
    protected void finalize() throws Throwable {
        super.finalize();
        this.timeoutItemQueue = null;
    }
}
