package com.yzlm.cyl.cfragment.Communication.CommProtocol.GuiZhou;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.toBytes;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.lGuiZhouCompRegMap;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static java.lang.System.arraycopy;

public class GuiZhouProtocol {

    public static String ModbusSend(Communication port, int sCom, byte addr, byte[] dataBytes) {
        String sendMsg = "";
        byte[] head = new byte[3];
        head[0] = addr;
        head[1] = 0x04;
        head[2] = (byte) dataBytes.length;
        byte[] sendBytes = copybyte(head, dataBytes);
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        //SendManager.SendCmd("IO" + "_" + "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")), S1, 1, 500, sendBytes);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, sendBytes);
        sendMsg = bytesToHexString(sendBytes, sendBytes.length);
        return sendMsg;
    }

    /**
     * 数据标识  compFlag =0 常规组份， 1 五参数组份
     */
    private static String getDataFlag(String compName, List<Map> list, int compFlag) {

        String ret;
        {
            if (!workState.get(compName).equals(context.getString(R.string.normal))) {
                return "D";
            }
            String strMode = getConfigData(compName, "runningMode");
            if (strMode.equals("2")) {// 离线
                return "B";
            }
            if (list.size() > 0 && compFlag == 0) {
                String flowTag = list.get(0).get("tag").toString();
                String flow = list.get(0).get("flow").toString();
                if (flowTag.contains("M")) {
                    ret = "M";
                } else if (flowTag.contains("T")) {
                    ret = "T";
                } else if (flowTag.contains("L")) {
                    ret = "L";
                } else {
                    ret = "N";
                }
                return ret;
            } else {
                return "N";
            }
        }
    }

    private static String nowCompName = "";

    public static void ParsingProtocolGuiZhou(Communication port, int sCom, byte[] rs, String sProtocol) {

        Log.i("协议", "解析模式：" + getPublicConfigData(sProtocol +
                "_ANALYSIS_MODE") + ",校验模式：" + getPublicConfigData(sProtocol + "_CRC_MODE"));

        String analysisMode = getPublicConfigData(sProtocol + "_ANALYSIS_MODE");
        String crcMode = getPublicConfigData(sProtocol + "_CRC_MODE");

        if (rs != null && rs.length > 6) {
            byte[] crc = new byte[2];
            //
            arraycopy(rs, rs.length - 2, crc, 0, 2);
            byte[] RtuRs = new byte[rs.length - 2];
            arraycopy(rs, 0, RtuRs, 0, RtuRs.length);


            if (Arrays.equals(DataUtil.crc16(RtuRs, RtuRs.length), crc)) {
                nowCompName = getCompName(RtuRs[0]);

                if (!nowCompName.equals("")) {
                    switch (RtuRs[1]) {
                        case 4:
                            modbus_04_Solve(port, sCom, RtuRs);
                            break;
                        default:
                            // 异常码01
                            sendModbusErrorCode(port, sCom, RtuRs, 1);
                            break;
                    }
                }
            }
        }
    }

    /*
     * 通过接收到地址找到对应组份
     */
    public static String getCompName(int reAddr) {
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                if (getConfigData(item, "GZ_RTU_ID").equals("")) {
                    continue;
                }
                if (getConfigData(item, "GZ_RTU_ID").equals(String.valueOf(reAddr))) {
                    return item;
                }
            }
        }
        return "";
    }

    /**
     * Modbus功能码03处理程序
     * 读保持寄存器
     */
    private static void modbus_04_Solve(Communication port, int sCom, byte[] rs) {
        int i;
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff);//获取寄存器起始地址
        int RegNum = ((((short) rs[4]) << 8) | rs[5]) & 0xff;//获取寄存器数量
        byte[] allDataBytes = new byte[256];
        int allDataCh = 0;
        int regAddr = startRegAddr;
        byte[] dataBytes;

        //非在线下不可读取参数,历史数据区域可读取
        if (!getModePermissions(nowCompName, "反控") && (!((regAddr + RegNum) < 4224))) {
            sendModbusErrorCode(port, sCom, rs, 6);
            return;
        }
        if (RegNum < lGuiZhouCompRegMap.get(nowCompName).size() || RegNum < 128) {

            byte[] head = new byte[3];
            head[0] = rs[0];
            head[1] = rs[1];
            head[2] = (byte) ((RegNum * 2) & 0xff);
            if (lGuiZhouCompRegMap.get(nowCompName).get(regAddr) == null) {
                sendModbusErrorCode(port, sCom, rs, 2);
                return;
            }
            for (i = 0; i < RegNum; ) {
                i += lGuiZhouCompRegMap.get(nowCompName).get(regAddr).getDataLen();
                if (i > RegNum) {
                    sendModbusErrorCode(port, sCom, rs, 1);
                    return;
                }
                dataBytes = copybyte(toBytes(lGuiZhouCompRegMap.get(nowCompName).get(regAddr).getData(), lGuiZhouCompRegMap.get(nowCompName).get(regAddr).getDataType(), lGuiZhouCompRegMap.get(nowCompName).get(regAddr).getDataLen()));

                arraycopy(dataBytes, 0, allDataBytes, allDataCh, dataBytes.length);
                allDataCh += dataBytes.length;

                regAddr += lGuiZhouCompRegMap.get(nowCompName).get(regAddr).getDataLen();
                if (lGuiZhouCompRegMap.get(nowCompName).get(regAddr) == null) {
                    sendModbusErrorCode(port, sCom, rs, 2);
                    return;
                }
            }
            byte[] sendBytes = new byte[allDataCh + 3 + 2];
            arraycopy(head, 0, sendBytes, 0, 3);
            arraycopy(allDataBytes, 0, sendBytes, 3, allDataCh);
            arraycopy(crc16(sendBytes, sendBytes.length - 2), 0, sendBytes, sendBytes.length - 2, 2);
            rS485_SendData(port, sCom, sendBytes);
        } else {
            //寄存器地址+数量超出范围
            sendModbusErrorCode(port, sCom, rs, 2);
        }
    }

    /**
     * 发送异常码
     */
    private static void sendModbusErrorCode(Communication port, int sCom, byte[] rs, int err) {
        //寄存器地址+数量超出范围
        byte[] sendBytes = new byte[3];
        sendBytes[0] = rs[0];
        sendBytes[1] = (byte) ((rs[1] | 0x80) & 0xff);
        sendBytes[2] = (byte) err;//异常码
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        rS485_SendData(port, sCom, sendBytes);
    }

    /**
     * 发送数据
     */
    private static String rS485_SendData(Communication port, int sCom, byte[] dataBytes) {

        SendManager.SendCmd("IO" + "_" + ((!IOBoardUsed) ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, dataBytes);
        return bytesToHexString(dataBytes, dataBytes.length);
    }
}
