package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.BCDDeccode.str2Bcd;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.floatToBytesBigs;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateSec;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateYear;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static java.lang.System.arraycopy;

/**
 * Created by zwj on 2017/8/2.
 */

public class RtuProtocol {

    /**
     * MODBUSRTU1.0版本
     */
    public static void ParsingProtocolRtu(Communication port, int sCom, byte[] rs, String sProtocol) {


        if (rs != null && rs.length > 6) {
            byte[] crc = new byte[2];
            arraycopy(rs, rs.length - 2, crc, 0, 2);
            byte[] RtuRs = new byte[rs.length - 2];
            arraycopy(rs, 0, RtuRs, 0, RtuRs.length);

            //若校验模式是低位，则先进行反转
            if ("2".equals(getPublicConfigData(sProtocol + "_CRC_MODE"))) {
                DataUtil.reverse(crc);
            }

            if (Arrays.equals(crc16(RtuRs, RtuRs.length), crc)) {
                int addr = 0;
                addr = Integer.parseInt(getPublicConfigData(sCom == 3 ? "RS485_DIGITAL_ADDR" : (sCom == 1 ? "DIGITAL_ADDR" : "PC_DIGITAL_ADDR")));
                if (addr == RtuRs[0]) {
                    switch (RtuRs[1]) {
                        case 3:
                            List<Map> list;
                            History mHistory;
                            mHistory = new History(context);
                            byte[] dataBytes = new byte[256];

                            switch (RtuRs[2] * 0x100 + RtuRs[3]) {
                                case 0x00:
                                    if (getModePermissions(strComponent.get(1)[0], "历史数据上传")) {
                                        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                                            list = mHistory.select(strComponent.get(1)[0], null, null, null, 0, 1);
                                        } else {
                                            list = mHistory.select(strComponent.get(1)[0], context.getString(R.string.ZY), null, null, 0, 1);
                                        }
                                        if (list.size() > 0) {
                                            float data = Float.parseFloat(list.get(0).get("C").toString());
                                            dataBytes = copybyte(floatToBytesBigs(data));

                                            arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
                                            ModbusSend(port, sCom, (byte) addr, dataBytes);
                                        }
                                    }
                                    break;
                                case 0x02:
                                    if (getModePermissions(strComponent.get(1)[1], "历史数据上传")) {
                                        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                                            list = mHistory.select(strComponent.get(1)[1], null, null, null, 0, 1);
                                        } else {
                                            list = mHistory.select(strComponent.get(1)[1], context.getString(R.string.ZY), null, null, 0, 1);
                                        }
                                        if (list.size() > 0) {
                                            float data = Float.parseFloat(list.get(0).get("C").toString());
                                            dataBytes = copybyte(floatToBytesBigs(data));

                                            arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
                                            ModbusSend(port, sCom, (byte) addr, dataBytes);
                                        }
                                    }
                                    break;
                                case 0x04:
                                    if (getModePermissions(strComponent.get(1)[2], "历史数据上传")) {
                                        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                                            list = mHistory.select(strComponent.get(1)[2], null, null, null, 0, 1);
                                        } else {
                                            list = mHistory.select(strComponent.get(1)[2], context.getString(R.string.ZY), null, null, 0, 1);
                                        }
                                        if (list.size() > 0) {
                                            float data = Float.parseFloat(list.get(0).get("C").toString());
                                            dataBytes = copybyte(floatToBytesBigs(data));

                                            arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
                                            ModbusSend(port, sCom, (byte) addr, dataBytes);
                                        }
                                    }
                                    break;
                                case 0x06:
                                    if (getModePermissions(strComponent.get(1)[3], "历史数据上传")) {
                                        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                                            list = mHistory.select(strComponent.get(1)[3], null, null, null, 0, 1);
                                        } else {
                                            list = mHistory.select(strComponent.get(1)[3], context.getString(R.string.ZY), null, null, 0, 1);
                                        }
                                        if (list.size() > 0) {
                                            float data = Float.parseFloat(list.get(0).get("C").toString());
                                            dataBytes = copybyte(floatToBytesBigs(data));

                                            arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
                                            ModbusSend(port, sCom, (byte) addr, dataBytes);
                                        }
                                    }
                                    break;
                                case 0x08:
                                    if (getModePermissions(strComponent.get(1)[4], "历史数据上传")) {
                                        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                                            list = mHistory.select(strComponent.get(1)[4], null, null, null, 0, 1);
                                        } else {
                                            list = mHistory.select(strComponent.get(1)[4], context.getString(R.string.ZY), null, null, 0, 1);
                                        }
                                        if (list.size() > 0) {
                                            float data = Float.parseFloat(list.get(0).get("C").toString());
                                            dataBytes = copybyte(floatToBytesBigs(data));

                                            arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
                                            ModbusSend(port, sCom, (byte) addr, dataBytes);
                                        }
                                    }
                                    break;

                                case 0x63:
                                    dataBytes = copybyte(str2Bcd(String.valueOf((short) (getSystemDateYear() - 2000))), str2Bcd(String.valueOf((short) (getSystemDateMonth()))),
                                            str2Bcd(String.valueOf((short) (getSystemDateDay()))), str2Bcd(String.valueOf((short) (getSystemDateHour()))),
                                            str2Bcd(String.valueOf((short) (getSystemDateMin()))), str2Bcd(String.valueOf((short) (getSystemDateSec()))));

                                    arraycopy(dataBytes, 0, dataBytes, 0, dataBytes.length);
                                    ModbusSend(port, sCom, (byte) addr, dataBytes);
                                    break;

                            }
                        case 6:
                            String compName = null;
                            switch (RtuRs[2] * 0x100 + RtuRs[3]) {
                                case 0x21:
                                    if (getModePermissions(strComponent.get(1)[0], "反控")) {
                                        compName = strComponent.get(1)[0];
                                    }
                                    break;
                                case 0x22:
                                    if (getModePermissions(strComponent.get(1)[1], "反控")) {
                                        compName = strComponent.get(1)[1];
                                    }
                                    break;
                                case 0x23:
                                    if (getModePermissions(strComponent.get(1)[2], "反控")) {
                                        compName = strComponent.get(1)[2];
                                    }
                                    break;
                                case 0x24:
                                    if (getModePermissions(strComponent.get(1)[3], "反控")) {
                                        compName = strComponent.get(1)[3];
                                    }
                                    break;
                                case 0x25:
                                    if (getModePermissions(strComponent.get(1)[4], "反控")) {
                                        compName = strComponent.get(1)[4];
                                    }
                                    break;

                                default:
                                    return;
                            }
                            if (compName != null) {
                                List<String> flows = new ArrayList<>();
                                String[] flowStr = {"反控" + context.getString(R.string.SDZY), "反控" + context.getString(R.string.SDJZ)};
                                if (RtuRs[4] * 0x100 + RtuRs[5] < flowStr.length) {
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, RtuRs);
                                    if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
                                        doControlJob(compName, flowStr[RtuRs[4] * 0x100 + RtuRs[5]]);
                                    }
                                }
                            } else {
                                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, RtuRs);
                            }
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
                if (getConfigData(item, "RTU_ID").equals("")) {
                    continue;
                }
                if (getConfigData(item, "RTU_ID").equals(String.valueOf(reAddr))) {
                    return item;
                }
            }
            if (getPublicConfigData("SYS_RTU_ID").equals(String.valueOf(reAddr))) {
                return "SYSTEM";
            }
        }
        return "";
    }


    public static String ModbusSend(Communication port, int sCom, byte addr, byte[] dataBytes) {
        String sendMsg = "";
        byte[] head = new byte[3];
        head[0] = addr;
        head[1] = 0x03;
        head[2] = (byte) dataBytes.length;
        byte[] sendBytes = copybyte(head, dataBytes);


        if (getPublicConfigData("Modbus_Rtu_CRC_MODE").equals("2")) {
            byte[] crc = crc16(sendBytes, sendBytes.length);
            DataUtil.reverse(crc);
            sendBytes = copybyte(sendBytes, crc);
        } else {
            sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        }

        //SendManager.SendCmd("IO" + "_" + "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")), S1, 1, 500, sendBytes);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, sendBytes);
        sendMsg = bytesToHexString(sendBytes, sendBytes.length);
        return sendMsg;
    }

/*
    MODBUS 2.0
    public static String ModbusSend(int scom, byte addr, byte[] dataBytes) {
        String sendMsg = "";
        byte[] head = new byte[3];
        head[0] = addr;
        head[1] = 0x03;
        head[2] = (byte) dataBytes.length;
        byte[] sendBytes = copybyte(head, dataBytes);
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        SendManager.SendCmd("IO" + "_" + "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")), S1, 1, 500, sendBytes);
        sendMsg = bytesToHexString(sendBytes, sendBytes.length);
        return sendMsg;
    }

    public static void ParsingProtocolRtu(int scom, byte[] rs) {
        if (rs != null && rs.length > 10) {
            byte[] crc = new byte[2];
            arraycopy(rs, rs.length - 4, crc, 0, 2);
            byte[] RtuRs = new byte[rs.length - 8];
            System.arraycopy(rs, 6, RtuRs, 0, RtuRs.length);
            if (Arrays.equals(crc16(RtuRs, RtuRs.length - 2), crc) == true) {
                int addr = (scom == 1 ? getPublicConfigData("DIGITAL_ADDR").toString().equals("") ? 0 : Integer.valueOf(getPublicConfigData("DIGITAL_ADDR").toString()) :
                        getPublicConfigData("PC_DIGITAL_ADDR").toString().equals("") ? 0 : Integer.valueOf(getPublicConfigData("PC_DIGITAL_ADDR").toString()));

                byte[] codeByte = new byte[]{RtuRs[2], RtuRs[3]};
                reverse(codeByte);
                int reReg = toInt(codeByte);
                int who = 0;
                for (int i = 0; i < strComponent.get(1).length; i++) {
                    if (reReg < (400 + (i * 300))) {
                        who = i;
                        break;
                    }
                }
                if (addr == RtuRs[0]) {
                    switch (RtuRs[1]) {
                        case 03:
                            List<Map> list;
                            History mHistory;
                            mHistory = new History(context);
                            byte[] allDataBytes = new byte[256];
                            int allDataCh = 0;
                            byte[] dataBytes = new byte[0];
                            byte[] reRegNumCodeByte = new byte[]{RtuRs[4], RtuRs[5]};
                            reverse(reRegNumCodeByte);
                            int reRegNum = toInt(reRegNumCodeByte);

                            int copyFlag = 1;
                            int startAddr = lRtuCmdData.get(RtuRs[1] == 3 ? "03" : "06").getRegObj((reReg - (who * 300))).getRegAddr();
                            int regAddr = startAddr;
                            for (int i = 0; i < reRegNum; regAddr += lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((regAddr)).getDataLen()) {
                                i += lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((regAddr)).getDataLen();
                                if (i > reRegNum) {
                                    return;
                                }
                                switch (lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((regAddr)).getDescribe()) {
                                    case "测量值":
                                        who = lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((regAddr)).getRegAddr() / 2;
                                        if (who < strComponent.get(1).length) {
                                            if (getConfigData(strComponent.get(1)[who] , "CAL_DATA_UPDATE").equals("true")) {
                                                list = mHistory.select(strComponent.get(1)[who], null, null, null, 0, 1);
                                            } else {
                                                list = mHistory.select(strComponent.get(1)[who], "做样", null, null, 0, 1);
                                            }
                                            if (list.size() > 0) {
                                                float data = Float.parseFloat(list.get(0).get("C").toString());
                                                dataBytes = copybyte(floatToBytesBigs(data));
                                            } else {
                                            *//*无数据不返回*//*
                                                return;
                                            }
                                        } else {
                                            return;
                                        }
                                        break;
                                    case "工作状态":
                                        int status = getCmds(strComponent.get(1)[who]).getCmd(51).getValue() == null ? 0 : Integer.parseInt((getCmds(strComponent.get(1)[who]).getCmd(51).getValue()).toString());
                                        dataBytes = copybyte(shortToByteArray(status));
                                        break;
                                    case "工作模式":

                                        break;
                                    case "量程低":
                                        dataBytes = copybyte(floatToBytesBigs(Float.valueOf(getConfigData(strComponent.get(1)[who] , "YBLCL").toString())));
                                        break;
                                    case "量程高":
                                        dataBytes = copybyte(floatToBytesBigs(Float.valueOf(getConfigData(strComponent.get(1)[who] , "YBLCH").toString())));
                                        break;
                                    case "当前报错":
                                        int errorNum = 0;
                                        if (hasDigit(workState.get(strComponent.get(1)[who])) == true) {
                                            errorNum = Integer.parseInt(getNumbers(workState.get(strComponent.get(1)[who])));
                                        }
                                        dataBytes = copybyte(shortToByteArray(errorNum));
                                        break;
                                    case "间隔做样小时":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "lxclh").toString())));
                                        break;
                                    case "间隔做样分钟":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "lxclm").toString())));
                                        break;
                                    case "整点测量0":
                                    case "整点测量1":
                                    case "整点测量2":
                                    case "整点测量3":
                                    case "整点测量4":
                                    case "整点测量5":
                                    case "整点测量6":
                                    case "整点测量7":
                                    case "整点测量8":
                                    case "整点测量9":
                                    case "整点测量10":
                                    case "整点测量11":
                                    case "整点测量12":
                                    case "整点测量13":
                                    case "整点测量14":
                                    case "整点测量15":
                                    case "整点测量16":
                                    case "整点测量17":
                                    case "整点测量18":
                                    case "整点测量19":
                                    case "整点测量20":
                                    case "整点测量21":
                                    case "整点测量22":
                                    case "整点测量23":
                                        int findTime = 0;
                                        String[] strzqclh = getConfigData(strComponent.get(1)[who] , "zqclh").toString().split("[，,]");
                                        for (int j = 0; j < strzqclh.length; j++) {
                                            if (getNumbers(lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((regAddr)).getDescribe()).equals(strzqclh[j])) {
                                                findTime = 1;
                                                break;
                                            }
                                        }
                                        dataBytes = copybyte(shortToByteArray(findTime));
                                        break;
                                    case "消解温度":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xjwd").toString())));
                                        break;
                                    case "消解时长":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xjsc").toString())));
                                        break;
                                    case "显色温度":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xswd").toString())));
                                        break;
                                    case "显示时长":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xssc").toString())));
                                        break;
                                    case "消解降温温度":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xjjw").toString())));
                                        break;
                                    case "显色降温温度":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xsjw").toString())));
                                        break;
                                    case "显色静置":
                                        dataBytes = copybyte(shortToByteArray(Integer.parseInt(getConfigData(strComponent.get(1)[who] , "xsjz").toString())));
                                        break;
                                    case "量程1标1":
                                        dataBytes = copybyte(floatToBytesBigs(Float.parseFloat(getConfigData(strComponent.get(1)[who] , "C0").toString())));
                                        break;
                                    case "量程2标1":
                                        dataBytes = copybyte(floatToBytesBigs(Float.parseFloat(getConfigData(strComponent.get(1)[who] , "C2").toString())));
                                        break;
                                    case "量程3标1":
                                        dataBytes = copybyte(floatToBytesBigs(Float.parseFloat(getConfigData(strComponent.get(1)[who] , "C4").toString())));
                                        break;
                                    case "量程1标2":
                                        dataBytes = copybyte(floatToBytesBigs(Float.parseFloat(getConfigData(strComponent.get(1)[who] , "C1").toString())));
                                        break;
                                    case "量程2标2":
                                        dataBytes = copybyte(floatToBytesBigs(Float.parseFloat(getConfigData(strComponent.get(1)[who] , "C3").toString())));
                                        break;
                                    case "量程3标2":
                                        dataBytes = copybyte(floatToBytesBigs(Float.parseFloat(getConfigData(strComponent.get(1)[who] , "C5").toString())));
                                        break;
                                    case "量程1K":
                                        dataBytes = copybyte(floatToBytesBigs(getNewestKBF(strComponent.get(1)[who], "1", "K")));
                                        break;
                                    case "量程2K":
                                        dataBytes = copybyte(floatToBytesBigs(getNewestKBF(strComponent.get(1)[who], "2", "K")));
                                        break;
                                    case "量程3K":
                                        dataBytes = copybyte(floatToBytesBigs(getNewestKBF(strComponent.get(1)[who], "3", "K")));
                                        break;
                                    case "量程1B":
                                        dataBytes = copybyte(floatToBytesBigs(getNewestKBF(strComponent.get(1)[who], "1", "B")));
                                        break;
                                    case "量程2B":
                                        dataBytes = copybyte(floatToBytesBigs(getNewestKBF(strComponent.get(1)[who], "2", "B")));
                                        break;
                                    case "量程3B":
                                        dataBytes = copybyte(floatToBytesBigs(getNewestKBF(strComponent.get(1)[who], "3", "B")));
                                        break;
                                    default:
                                        copyFlag = 0;
                                        break;
                                }
                                if (copyFlag == 1) {
                                    System.arraycopy(dataBytes, 0, allDataBytes, allDataCh, dataBytes.length);
                                    allDataCh += dataBytes.length;
                                }
                            }
                            if (allDataCh != 0) {
                                byte[] sendData = new byte[allDataCh];
                                System.arraycopy(allDataBytes, 0, sendData, 0, allDataCh);
                                ModbusSend(scom, (byte) addr, sendData);
                            }
                            break;
                        case 06:
                            int setAddr = lRtuCmdData.get(RtuRs[1] == 3 ? "03" : "06").getRegObj((reReg - (who * 300))).getRegAddr();
                            switch (lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((setAddr)).getDescribe()) {
                                case "反控":
                                    who = lRtuCmdData.get((RtuRs[1] == 3 ? "03" : "06")).getRegObj((setAddr)).getRegAddr() - 33;
                                    if (who < strComponent.get(1).length) {
                                        List<String> flows = new ArrayList<>();
                                        String[] flowStr = {"手动做样", "手动校准", "仪表清洗", "零样测量", "标样1测量", "标样2测量"};

                                        byte[] reRegDataByte = new byte[]{RtuRs[4], RtuRs[5]};
                                        reverse(reRegDataByte);
                                        int iRegData = toInt(reRegDataByte);

                                        if (iRegData == 0xFF) {
                                            stopWorking(strComponent.get(1)[who]);
                                        } else {
                                            if (iRegData < flowStr.length) {
                                                SendManager.SendCmd("IO" + "_" + "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")), S1, 1, 500, RtuRs);
                                                flows.add(flowStr[iRegData]);
                                                runFlows(strComponent.get(1)[who], flows);
                                            }
                                        }
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        }
    }*/
}
