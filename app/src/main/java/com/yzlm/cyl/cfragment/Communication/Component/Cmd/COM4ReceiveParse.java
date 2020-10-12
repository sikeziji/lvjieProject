package com.yzlm.cyl.cfragment.Communication.Component.Cmd;


import static com.yzlm.cyl.cfragment.Communication.Thread.SendThread.synchronizationS3;

/**
 * Created by zwj on 2018/05/16.
 */

public class COM4ReceiveParse extends Thread {


    public COM4ReceiveParse(byte[] rs) {
        byte[] rs1 = rs;
    }

    @Override
    public void run() {
        try {
            synchronizationS3 = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

