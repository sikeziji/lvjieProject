package com.yzlm.cyl.cfragment.Communication.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;

/**
 * Created by caoyiliang on 2017/1/19.
 */

public class SendManager {

    public static void SendCmd(final String stringExtra, final Communication sp, final int reTry, final int sleep, final Object object) {
        Thread thr = new Thread(new Runnable() {
            public void run() {
                Send(stringExtra, sp, reTry, sleep, object);
            }
        });
        thr.start();
    }

    private static void Send(String stringExtra, Communication sp, int reTry, int sleep, Object object) {
        try {
            synchronized (sp) {
                SendThread thread = new SendThread(stringExtra, sp, reTry, sleep, object);
                thread.start();
                try {
                    thread.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
