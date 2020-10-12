package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.clibrary.Thread.CThread;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GuiZhou.GuiZhou.addGuiZhouRegisterData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.addRegisterData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu2.ModbusRtu2.addRtu2RegisterData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan.siChuanModbusRtu.addSiChuanRtuRegisterData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.cfragment.Global.protocolName;

/**
 * Created by zwj on 2018/10/22.
 */

public class ProtocolCfgUpdate extends CThread {
    public ProtocolCfgUpdate(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 1000 * 3;
    }

    @Override
    public void process() {
        try {

            if (((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_1_Protocol"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_2_Protocol"))].equals(protocolName[4])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_3_Protocol"))].equals(protocolName[4])))) {
                addRegisterData();
            }

            if (((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_1_Protocol"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_2_Protocol"))].equals(protocolName[10])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_3_Protocol"))].equals(protocolName[10])))) {
                addRtu2RegisterData();
            }

            if (((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_1_Protocol"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_2_Protocol"))].equals(protocolName[11])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_3_Protocol"))].equals(protocolName[11])))) {
                addGuiZhouRegisterData();
            }

            if (((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_1_Protocol"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_2_Protocol"))].equals(protocolName[15])))
                    || ((protocolList[Integer.parseInt(getPublicConfigData("TCP_3_Protocol"))].equals(protocolName[15])))) {
                addSiChuanRtuRegisterData(null, false);
            }

        } catch (Exception e) {
            Log.i("exception", "addRegisterData" + e.toString());
        }
    }

    @Override
    public void onDestory() {

    }
}
