package com.yzlm.cyl.cfragment.Communication.Component.Cmd;


import android.os.Bundle;
import android.os.Message;

import com.yzlm.cyl.cfragment.Communication.Communication;

import static com.yzlm.cyl.cfragment.Communication.Thread.SendThread.synchronizationS1;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content2.commTestHandler;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.digitalTest;
import static com.yzlm.cyl.cfragment.Global.protocolList;

/**
 * Created by zwj on 2017/7/5.
 * <p>
 * // 11  12  485 口
 */

public class COM3ReceiveParse extends Thread {

    private byte[] rs;
    private Communication port;


    public COM3ReceiveParse(Communication port, byte[] rs) {
        this.rs = rs;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            synchronizationS1 = true;
            try {
                if (digitalTest && port == S1) {
                    Message msg = new Message();
                    msg.what = 9;
                    Bundle bundleData = new Bundle();
                    bundleData.putByteArray("IOReceiveData", rs);
                    msg.setData(bundleData);
                    commTestHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            {
                ProtocolResponse protocolResponse = new ProtocolResponse();
                protocolResponse.ParsingProtocol(port, 0, protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))], rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

