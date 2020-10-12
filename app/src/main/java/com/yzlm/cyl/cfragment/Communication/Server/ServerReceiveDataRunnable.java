package com.yzlm.cyl.cfragment.Communication.Server;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;

/**
 * Create by duyang
 * Create on 2020-9-2 16:23
 * description  服务端接收数据做处理
 */
public class ServerReceiveDataRunnable implements Runnable {

    private Communication serverPort;
    private ServerReceiveDataRunnable.Callback callback;
    private int iSleepTime;

    public ServerReceiveDataRunnable(Communication serverPort, int iSleepTime, ServerReceiveDataRunnable.Callback callback) {
        this.serverPort = serverPort;
        this.iSleepTime = iSleepTime;
        this.callback = callback;
    }

    @Override
    public void run() {
        byte[] rs;
        while (this.serverPort != null) {
            try {

                rs = new byte[4096];
                rs = serverPort.receiveData(rs);
                if (rs != null) {
                    callback.receiveParse(this.serverPort, rs);
                }

                try {
                    Thread.sleep(iSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
        }
    }


    public interface Callback {
        void receiveParse(Communication serverPort, byte[] tempData);

        void sendData(Communication serverPort, byte[] data);
    }

}
