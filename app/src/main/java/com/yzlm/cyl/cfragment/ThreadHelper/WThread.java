package com.yzlm.cyl.cfragment.ThreadHelper;

import android.util.Log;

import java.util.Calendar;

/*
 * Created by WL on 2017/4/20.
 */

public abstract class WThread implements Runnable {
    /*自动流程的各个参数表，分别代表自动做样连续测量间隔时间（分）、自动校准间隔时间（天）、自动校准启动时间（小时整点）和自动清洗间隔时间（次）*/
    public int[] SUSPEND_TIME_MILLISECONDS = new int[]{1,1,8,5};
    private String name;
    private Thread mThread;
    /*自动流程的暂停标志值，分别代表自动做样、自动校准和自动清洗的暂停标志值*/
    private boolean[] suspendFlag = new boolean[]{false,false,false};
    private String TAG = this.getName();
    /*自动校准的计时开始的日期*/
    public Calendar calibDate;

    protected abstract void process();

    protected abstract void onDestory();

    protected WThread(String name, boolean[] suspend) {
        this.suspendFlag = suspend;
        this.name = name;
        this.mThread = new Thread(this, name);
        Log.i(this.TAG, "new Thread: " + name);
        this.mThread.start();
    }

    public void run() {
        try {
            while(true) {
//                synchronized(this) {
//                    while(this.suspendFlag) {
//                        this.wait();
//                    }
//                }

                Thread.sleep(1000);
                this.process();
            }
        } catch (InterruptedException var4) {
            var4.printStackTrace();
            this.onDestory();
            Log.i(this.TAG, this.name + " exited");
        }
    }

    public boolean[] getSuspendFlag(){
        return this.suspendFlag;
    }

    public void suspend(int index) {
        this.suspendFlag[index] = true;
    }

    public synchronized void resume(int index) {
        this.suspendFlag[index] = false;
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
