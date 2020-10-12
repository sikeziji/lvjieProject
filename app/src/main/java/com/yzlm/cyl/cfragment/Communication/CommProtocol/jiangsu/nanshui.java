package com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.R;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.iDataFormat;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.convertStringToHex;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;


/**
 * Created by zwj on 2017/11/24.
 */

public class nanshui {


    public static void ParsingProtocolJiangSu(Communication port, int sCom, byte[] rs) {
        if (rs != null && rs.length > 6) {


            if (rs[0] == 0x3A && rs.length == 8 && ((rs[6] == 0x0D && rs[7] == 0x0A) || (((rs[6] & 0xff) == 0x8D && rs[7] == 0x0A)))) {

                if (getPublicConfigData(sCom == 3 ? "RS485_DIGITAL_ADDR" : (sCom == 1 ? "DIGITAL_ADDR" : "PC_DIGITAL_ADDR")).equals((rs[1] - 0x30) + String.valueOf(rs[2] - 0x30) + (rs[3] - 0x30))) {

                    switch ((rs[4] - 0x30) * 10 + ((rs[5] & 0xff) - 0x30)) {
                        case 0x84:/*数据上传*/
                        case 4:
                            if (getModePermissions(strComponent.get(1)[0], "历史数据上传")) {
                                List<Map> list;
                                History mHistory;
                                mHistory = new History(context);
                                byte[] dataBytes = new byte[256];

                                if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                                    list = mHistory.select(strComponent.get(1)[0], null, null, null, 0, 1);
                                } else {
                                    list = mHistory.select(strComponent.get(1)[0], context.getResources().getString(R.string.ZY)/* "做样"*/, null, null, 0, 1);
                                }
                                if (list.size() > 0) {
                                    float data = Float.parseFloat(list.get(0).get("C").toString());
                                    sendJiangSuProtocol(strComponent.get(1)[0], port, sCom, Byte.parseByte(getPublicConfigData(sCom == 3 ? "RS485_DIGITAL_ADDR" : (sCom == 1 ? "DIGITAL_ADDR" : "PC_DIGITAL_ADDR"))), (byte) 4, data);
                                }
                            }
                            break;
                        case 0x81:
                        case 1:
                            String compName = strComponent.get(1)[0];
                            if (getModePermissions(compName, "反控")) {
                                if (compName != null && (!compName.equals(""))) {
                                    sendJiangSuProtocolStartJob(port, sCom, Byte.parseByte(getPublicConfigData(sCom == 3 ? "RS485_DIGITAL_ADDR" : (sCom == 1 ? "DIGITAL_ADDR" : "PC_DIGITAL_ADDR"))), (byte) 1);
                                    if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
                                        doControlJob(compName, context.getResources().getString(R.string.SDZY));
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
    }


    public static String sendJiangSuProtocol(String compName, Communication port, int sCom, byte addr, byte orde, float data) {

        String sendMsg = "";
        int pointCh = 0;
        int idata = 0;
        int pointPar = 3;
        byte[] head = new byte[16];
        head[0] = 0x3A;
        String straddr = iDataFormat(String.valueOf(addr), 3, null);

        head[1] = (byte) (Byte.parseByte(String.valueOf(straddr.charAt(1))) + 0x30);
        head[2] = (byte) (Byte.parseByte(String.valueOf(straddr.charAt(2))) + 0x30);
        head[3] = (byte) (Byte.parseByte(String.valueOf(straddr.charAt(3))) + 0x30);

        head[4] = (byte) (Byte.parseByte(String.valueOf(iDataFormat(String.valueOf(orde), 3, null).charAt(2))) + 0x30);
        head[5] = (byte) (Byte.parseByte(String.valueOf(iDataFormat(String.valueOf(orde), 3, null).charAt(3))) + 0x30);
        pointCh = String.valueOf(data).indexOf(".");
        /*根据小数点位数取整数*/
        if (getConfigData(compName, "YXWS").equals("") || getConfigData(compName, "YXWS").equals("0")) {
            pointPar = 3;
        }
        idata = (int) (data * Math.pow(10, pointPar));

        String str = Integer.toHexString(idata);
        String strformat = iDataFormat(str.toUpperCase(), 4, null);
        String hex = convertStringToHex(strformat);
        head[6] = (byte) Integer.parseInt(hex.substring(0, 2), 16);
        head[7] = (byte) Integer.parseInt(hex.substring(2, 4), 16);
        head[8] = (byte) Integer.parseInt(hex.substring(4, 6), 16);
        head[9] = (byte) Integer.parseInt(hex.substring(6, 8), 16);

        String strMe = Integer.toHexString(pointPar * -1);
        String meformat = convertStringToHex(strMe.substring(4, 8).toUpperCase());
        head[10] = (byte) Integer.parseInt(meformat.substring(0, 2), 16);
        head[11] = (byte) Integer.parseInt(meformat.substring(2, 4), 16);
        head[12] = (byte) Integer.parseInt(meformat.substring(4, 6), 16);
        head[13] = (byte) Integer.parseInt(meformat.substring(6, 8), 16);
        head[14] = 0x0D;
        head[15] = 0x0A;

        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, head);
        sendMsg = bytesToHexString(head, head.length);
        return sendMsg;
    }


    private static String sendJiangSuProtocolStartJob(Communication port, int sCom, byte addr, byte orde) {
        String sendMsg = "";

        byte[] head = new byte[16];
        for (int i = 0; i < 16; i++) {
            head[i] = 0x30;
        }
        head[0] = 0x3A;
        String straddr = iDataFormat(String.valueOf(addr), 3, null);
        head[1] = (byte) (Byte.parseByte(String.valueOf(straddr.charAt(1))) + 0x30);
        head[2] = (byte) (Byte.parseByte(String.valueOf(straddr.charAt(2))) + 0x30);
        head[3] = (byte) (Byte.parseByte(String.valueOf(straddr.charAt(3))) + 0x30);
        head[4] = (byte) (Byte.parseByte(String.valueOf(iDataFormat(String.valueOf(orde), 3, null).charAt(2))) + 0x30);
        head[5] = (byte) (Byte.parseByte(String.valueOf(iDataFormat(String.valueOf(orde), 3, null).charAt(3))) + 0x30);

        head[14] = 0x0D;
        head[15] = 0x0A;

        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, head);
        sendMsg = bytesToHexString(head, head.length);
        return sendMsg;
    }
}
