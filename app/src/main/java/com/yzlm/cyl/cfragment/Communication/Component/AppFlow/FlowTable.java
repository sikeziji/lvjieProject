package com.yzlm.cyl.cfragment.Communication.Component.AppFlow;

import com.yzlm.cyl.cfragment.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * Created by caoyiliang on 2017/2/17.
 * 某个组分的所有流程的封装类（WL）
 */

@SuppressWarnings("unchecked")
public class FlowTable {
    /*某个组分的所有流程列表*/
    private Map<String, FlowClass> LFlow = new HashMap();

    /*向某个组分的流程表中增加一个流程，key如做样_量程1等*/
    public void Add(FlowClass flow) {
        LFlow.put(flow.getName() + "_" + flow.getRange(), flow);
    }

    /*通过Key值获取某个组分的指定流程*/
    public FlowClass getFlow(String key) {
        return LFlow.get(key);
    }

    /*通过流程号获取某个组分的指定流程*/
    public FlowClass getFlowByCmd(Object value) {
        try {
            byte status = (byte) value;

            /*若流程的流程号为255，为仪器设备正在自检，没有流程*/
            if ((status & 0xFF) == 0xFF) return null;

            Iterator iter = LFlow.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                FlowClass fc = (FlowClass) entry.getValue();
                if (fc.getCmd() == status) return fc;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /*通过流程号获取某个组分的流程名作为判断仪器设备当前的状态*/
    public String GetStatus(Object value) {
        try {
            byte status = (byte) value;

            /*若流程的流程号为255，仪器状态为仪器设备正在自检*/
            if ((status & 0xFF) == 0xFF)
                return context.getResources().getString(R.string.selfCheck);

            Iterator iter = LFlow.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                FlowClass fc = (FlowClass) entry.getValue();
                if (fc.getCmd() == status) return fc.getName();
            }
        } catch (Exception ex) {
        }
        return context.getResources().getString(R.string.waiting_for_instructions);
    }
}
