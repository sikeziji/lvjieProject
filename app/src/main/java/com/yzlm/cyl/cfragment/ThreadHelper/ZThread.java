package com.yzlm.cyl.cfragment.ThreadHelper;

import android.util.Log;

/*
 * Created by zwj on 2018/1/17.
 */

public abstract class ZThread implements Runnable {
    protected int SUSPEND_TIME_MILLISECONDS = 50;
    private String name;
    private Thread mThread;
    private boolean suspendFlag = false;
    private String TAG = this.getName();

    protected abstract void process();

    protected abstract void onDestory();

    protected ZThread(String name, boolean suspend) {
        this.suspendFlag = suspend;
        this.name = name;
        this.mThread = new Thread(this, name);
        Log.i(this.TAG, "new Thread: " + name);
        this.mThread.start();
    }

    public void run() {
        try {
            while(true) {
                synchronized(this) {
                    while(this.suspendFlag) {
                        this.wait();
                    }
                }
                Thread.sleep((long)this.SUSPEND_TIME_MILLISECONDS);
                synchronized(this) {
                    while(this.suspendFlag) {
                        this.wait();
                    }
                }
                this.process();
            }
        } catch (InterruptedException var4) {
            var4.printStackTrace();
            this.onDestory();
            Log.i(this.TAG, this.name + " exited");
        }
    }

    public void suspend() {
        this.suspendFlag = true;
    }

    public synchronized void resume() {
        this.suspendFlag = false;
        this.notify();
    }

    private String getName() {
        return this.name;
    }

    public Thread getT() {
        return this.mThread;
    }

    public void stop() {
        if(this.mThread != null) {
            this.mThread.interrupt();
            this.mThread = null;
        }

    }
}

