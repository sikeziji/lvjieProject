package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.W300;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;

import java.util.Arrays;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.toBytes;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static java.lang.System.arraycopy;

public class W300 {
    private static String nowCompName = "";

    public static void ParsingProtocolW300(Communication port, int sCom, byte[] rs, String sProtocol) {

        Log.i("协议", "解析模式：" + getPublicConfigData(sProtocol +
                "_ANALYSIS_MODE") + ",校验模式：" + getPublicConfigData(sProtocol + "_CRC_MODE"));


        if (rs != null && rs.length > 6) {
            byte[] crc = new byte[2];
            //
            arraycopy(rs, rs.length - 2, crc, 0, 2);
            byte[] RtuRs = new byte[rs.length - 2];
            arraycopy(rs, 0, RtuRs, 0, RtuRs.length);

            if (Arrays.equals(crc16(RtuRs, RtuRs.length), crc)) {
                nowCompName = getCompName(RtuRs[0]);

                if (!nowCompName.equals("")) {
                    switch (RtuRs[1]) {
                        case 3:
                            modbus_03_Solve(port, sCom, RtuRs);
                            break;
                        case 06:
                            modbus_06_Solve(port, sCom, RtuRs);
                            break;
                        case 16:
                            //modbus_16_Solve(port, sCom, RtuRs);
                            break;
                        default:
                            // 异常码01
                            //sendModbusErrorCode(port, sCom, RtuRs, 1);
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
                if (getConfigData(item, "W300_RTU_ID").equals("")) {
                    continue;
                }
                if (getConfigData(item, "W300_RTU_ID").equals(String.valueOf(reAddr))) {
                    return item;
                }
            }
        }
        return "";
    }

    /***
     * modbus 读寄存器数据
     * */
    private static void modbus_03_Solve(Communication port, int sCom, byte[] rs) {
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff);//获取寄存器起始地址
        int RegNum = ((((short) rs[4]) << 8) | rs[5]) & 0xff;//获取寄存器数量
        byte[] dataBytes;
        switch (startRegAddr) {
            case 2: {
                byte[] head = new byte[3];
                head[0] = rs[0];
                head[1] = rs[1];
                head[2] = (byte) ((RegNum * 2) & 0xff);
                dataBytes = copybyte(head, toBytes(String.valueOf((int) (Double.parseDouble(getConfigData(nowCompName, "fyzs_M")) * 100)), "WORD", RegNum));

                byte[] sendBytes = new byte[dataBytes.length + 2];
                arraycopy(dataBytes, 0, sendBytes, 0, dataBytes.length);
                arraycopy(crc16(dataBytes, dataBytes.length), 0, sendBytes, sendBytes.length - 2, 2);
                rS485_SendData(port, sCom, sendBytes);
            }
            break;
            default:
                byte[] sendBytes = new byte[6];
                sendBytes[0] = rs[0];
                sendBytes[1] = rs[1];
                sendBytes[2] = rs[2];
                sendBytes[3] = rs[3];
                sendBytes[4] = rs[4];
                sendBytes[5] = rs[5];
                sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
                rS485_SendData(port, sCom, sendBytes);
                break;
        }
    }

    /***
     * modbus 写单个保持寄存器数据
     * */
    private static void modbus_06_Solve(Communication port, int sCom, byte[] rs) {
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff);//获取寄存器起始地址
        switch (startRegAddr) {
            case 1: {
                int valRegAddr = (((short) rs[4] & 0xff) << 8) | (rs[5] & 0xff);
                if (valRegAddr == 1) {
                    updateConfigData(nowCompName, "autoCalFlowFlag", "true");
                }
            }
            break;
            default:
                break;
        }

        byte[] sendBytes = new byte[6];
        sendBytes[0] = rs[0];
        sendBytes[1] = rs[1];
        sendBytes[2] = rs[2];
        sendBytes[3] = rs[3];
        sendBytes[4] = rs[4];
        sendBytes[5] = rs[5];
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        rS485_SendData(port, sCom, sendBytes);
    }

    /**
     * 发送数据
     */
    private static String rS485_SendData(Communication port, int sCom, byte[] dataBytes) {

        SendManager.SendCmd("IO" + "_" + ((!IOBoardUsed) ? "打印输出_0_0" : "W300_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, dataBytes);
        return bytesToHexString(dataBytes, dataBytes.length);
    }

}
