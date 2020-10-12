package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getTimeHaveSec;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;

public class OpenDoorCheck {


    /**
     * 刷新主控板 开门检测状态
     */
    public OpenDoorCheck() {
        try {
            for (String item : strComponent.get(1)) {
                /*存储开门信号源状态 1:开 2关**/
                String sDoorStatus;
                if (QueryMeasBoardType(item).equals("2")) {
                    sDoorStatus = (getCmds(item).getCmd(67).getValue() == null ? "" : getCmds(item).getCmd(67).getValue()).toString();
                    String sStatus = sDoorStatus.equals("2") ? "close" : "open";
                    updateConfigData(item, "DoorSignal1", sStatus);
                    setDoorStatus(item, sStatus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * strDoor 1 开门  2关门
     */
    private  void setDoorStatus(String item, String sDoorStatus) {

        boolean isDoorStatus;// 默认开门
        // 开门
        isDoorStatus = sDoorStatus.equals("1");

        //原来门状态
        String strDoorStatus = getConfigData(item, "MasterCtrlDoorStatus");
        // 开门
        if (isDoorStatus) {
            if (strDoorStatus.equals("close")) {
                // 记录开门状态
                saveOperationLogDataModifyMsg(item, "MasterCtrlDoorStatus", "open", "开门", ErrorLog.msgType.操作_信息);
                updateConfigData(item, "MasterCtrlDoorStatus", "open");
                updateConfigData(item, "DoorCloseDataCounter", "0");

                saveRunInfo2File("组分[" + item + "]" + "开门");
                updateConfigData(item, "MasterCtrlDoorStatusOpenTime", getTimeHaveSec());
                openDoorClearStatus(item);
            }
        } else {
            // 关门
            if (strDoorStatus.equals("open")) {
                //关门状态
                saveOperationLogDataModifyMsg(item, "MasterCtrlDoorStatus", "close", "关门", ErrorLog.msgType.操作_信息);
                updateConfigData(item, "MasterCtrlDoorStatus", "close");
                saveRunInfo2File("组分[" + item + "]" + "关门");
                saveOperationLogMsg(item, "组分[" + item + "]" + "关门", ErrorLog.msgType.操作_信息);
            }
        }
    }

    /**
     * 最近一小时是否有开门
     *
     * @return False:没开过没，True ：开过门
     */
    public static boolean getMasterCtrlDoorStatusToNowDate(String sCompName, String sDateTime) {

        String checkEndTime = getTimeHaveSec();

        //获取最新一次开门时间
        String strDoorStatusTime = getConfigData(sCompName, "MasterCtrlDoorStatusOpenTime");
        if (!strDoorStatusTime.equals("")) {
            // 检查该数据整点内有没有开过门
            return strDoorStatusTime.compareTo(checkEndTime) < 0 && strDoorStatusTime.compareTo(sDateTime) > 0;
        }
        return false;
    }


    /**
     * 开门清除状态
     *
     * @param sCompName 组分名称
     */
    public void openDoorClearStatus(String sCompName) {

        if (!getConfigData(sCompName, "MeaAValueEn").equals("")) {
            updateConfigData(sCompName, "MeaAValueEn", "");
        }
        if (getConfigData(sCompName, "DoorCloseDataCounter").equals("1")) {
            updateConfigData(sCompName, "DoorCloseDataCounter", "0");
        }
    }
}
