package com.yzlm.cyl.cfragment.Communication.Component.AppFlow;

import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.RecMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.getFlows;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;

/**
 * Created by caoyiliang on 2017/2/7.
 * 某个组分的某一流程封装类（WL）
 */

@SuppressWarnings("unchecked")
public class FlowClass {
    /*该流程的流程名*/
    private String Name;
    /*该流程的流程号*/
    private byte Cmd;
    /*该流程的对应量程*/
    private int range;
    /*该流程的组合流程命令*/
    private String CombinedCommand;
    /*该流程的对应流程步骤列表*/
    private Map<Integer, FlowStep> steps = new HashMap();
    /*记载日志信息类的对象*/
    RecMessage RsMessage = new RecMessage();

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public byte getCmd() {
        return Cmd;
    }

    public void setCmd(byte cmd) {
        Cmd = cmd;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getCombinedCommand() {
        return CombinedCommand;
    }

    public void setCombinedCommand(String combinedCommand) {
        CombinedCommand = combinedCommand;
    }


    public byte[] findFlow(String Component) {
        byte[] rs = new byte[0];
        try {
            if (CombinedCommand == null) {
                rs = new byte[]{Cmd};
            } else {
                FlowTable ft = getFlows(Component);
                for (String item : CombinedCommand.split("&")) {
                    rs = copybyte(rs, ft.getFlow(item + "_" + getRange()).findFlow(Component));
                }
            }
        } catch (Exception e) {
            /*RsMessage.setMesType(ErrorLog.msgType.运行_信息);
            RsMessage.setMessage("组分[" + Component + "]的流程文件有误：" + CombinedCommand);
            Message msg = new Message();
            msg.what = RsMessage.getMesType().ordinal();
            msg.obj = RsMessage.getMessage();
            mHandler.sendMessage(msg);*/
        }
        return rs;
    }

    public int getEnergyCount() {
        int Count = 0;
        Iterator iter = steps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            FlowStep fc = (FlowStep) entry.getValue();
            if (fc.getCode().equals("A_LED1_MEA")) Count++;
        }
        return Count;
    }

    public void Add(int Index, FlowStep fs) {
        steps.put(Index, fs);
    }


    public Map<Integer, FlowStep> getSteps() {
        return steps;
    }

}
