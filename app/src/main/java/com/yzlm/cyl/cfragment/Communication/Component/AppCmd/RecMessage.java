package com.yzlm.cyl.cfragment.Communication.Component.AppCmd;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;

/**
 * Created by caoyiliang on 2017/2/9.
 */

public class RecMessage {
    private boolean canParsing = false;
    private Object Message;
    private ErrorLog.msgType mesType= ErrorLog.msgType.通讯_错误;


    public Object getMessage() {
        return Message;
    }

    public void setMessage(Object message) {
        Message = message;
    }

    public boolean isCanParsing() {
        return canParsing;
    }

    public void setCanParsing(boolean canParsing) {
        this.canParsing = canParsing;
    }

    public ErrorLog.msgType getMesType(){
        return mesType;
    }

    public void  setMesType(ErrorLog.msgType logType){
        mesType=logType;
    }
}
