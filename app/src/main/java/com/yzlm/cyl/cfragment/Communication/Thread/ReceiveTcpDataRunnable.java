package com.yzlm.cyl.cfragment.Communication.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;

/**
 * Created by zwj on 2017/5/23.
 */

public class ReceiveTcpDataRunnable implements Runnable {

    private Communication tcpPort;
    private Callback callback;
    private int iSleepTime;

    public ReceiveTcpDataRunnable(Communication tcpPort, int iSleepTime, Callback callback) {
        this.tcpPort = tcpPort;
        this.iSleepTime = iSleepTime;
        this.callback = callback;
    }

    public interface Callback {
        void receiveParse(Communication port, byte[] tempData);
    }

    public void run() {
        byte[] rs;
        while (this.tcpPort != null) {
            try {
                rs = new byte[4096];
                rs = tcpPort.receiveData(rs);
                if (rs != null) {
                    callback.receiveParse(this.tcpPort, rs);
                }
                try {
                    Thread.sleep(iSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                Log.e("exception",e.toString());
            }
        }
    }
}
