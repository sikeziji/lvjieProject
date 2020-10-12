package com.yzlm.cyl.cfragment.Communication.Thread;

import android.os.Message;

import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.RecMessage;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.dataParsing;
import static com.yzlm.cyl.cfragment.Global.mHandler;

/**
 * 测控板回发协议解析线程类
 * Created by caoyiliang on 2017/2/9.
 */

class ReceiveParsing extends Thread {
    private String str;
    private byte[] rs;
    private Object object;

    public ReceiveParsing(String str, byte[] rs, Object object) {
        this.str = str;
        this.rs = rs;
        this.object = object;
    }

    @Override
    public void run() {
        RecMessage rsMessage = dataParsing(str, rs, object);
        Message msg = new Message();
        msg.what=rsMessage.getMesType().ordinal();
//        if (rsMessage.isCanParsing()) {
//            msg.what = ErrorLog.LogType.通讯_信息.ordinal();
//        } else {
//            msg.what = ErrorLog.LogType.通讯_错误.ordinal();
//        }
        msg.obj = rsMessage.getMessage();
        mHandler.sendMessage(msg);
    }
}
