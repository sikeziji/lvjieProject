package com.yzlm.cyl.cfragment.Communication.CommProtocol.HeBei;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolStruct;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getNowCfgCompNameCde;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCodeFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLastHistory;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.BCDDeccode.bcd2Str;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.floatToBytesBigs;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateSec;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateYear;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getTimer_cycle;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.syncAutoDoSample_topHour;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.System.arraycopy;


/*
 *河北协议2.0
 * **/
public class HeBei {

    private static String[] strCode = new String[30];


    private HeBei() {

        if (strCode[0] == null) {
            strCode = AnalysisCodeFile("Csoft/Protocol/HeBei/", "CodeHB");
        }
    }

    // 返回执行状态
    private static void getQnRtnCmd(Communication port, int sCom, byte[] data, byte qnRtn) {

        byte[] head = {0x23, 0x23};
        byte[] end = {0x26, 0x26};

        byte[] td = copybyte(head, new byte[]{qnRtn}, data);
        byte[] crcData = new byte[td.length - 2];
        arraycopy(td, 2, crcData, 0, crcData.length);
        td = copybyte(td, crc16(crcData, crcData.length), end);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 1000, td);
    }


    /*
    参数上传，组分参数区
    * **/
    private static byte[] getCompParArea(DataStruct DataInfo) {


        byte[] byCode = {0x0, 0x0, 0x0};
        byte[] byHeatTemp = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        byte[] byHeatTime = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        byte[] bySpan2Time = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

        // code;
        String sCode = getCode(DataInfo.getType(), strCode);
        byCode[0] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(0))) + 0x30);
        byCode[1] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(1))) + 0x30);
        byCode[2] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(2))) + 0x30);

        //量程K
        byte[] byK = floatToBytesBigs(getNewestKBF(DataInfo.getType(), getConfigData(DataInfo.getType(), "RANGE"), "K"));
        byte[] byB = floatToBytesBigs(getNewestKBF(DataInfo.getType(), getConfigData(DataInfo.getType(), "RANGE"), "B"));


        if (getSetWinParShow(DataInfo.getType(), "消解温度")) {
            try {
                byHeatTemp = floatToBytesBigs(Float.parseFloat(getConfigData(DataInfo.getType(), "xjwd")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (getSetWinParShow(DataInfo.getType(), "消解时长")) {
            try {
                byHeatTime = floatToBytesBigs(Float.parseFloat(getConfigData(DataInfo.getType(), "xjsc")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] byRange = floatToBytesBigs(Float.parseFloat((getConfigData(DataInfo.getType(), "YBLCH"))));

        String calTime = getConfigData(DataInfo.getType(), "CAL_SUC_TIME");

        if (!calTime.equals("")) {
            String[] sTime = calTime.replace(" ", "-").split("[-:：]");
            int year = Integer.valueOf(sTime[0]);
            int month = Integer.valueOf(sTime[1]);
            int day = Integer.valueOf(sTime[2]);
            int hour = Integer.valueOf(sTime[3]);
            int min = Integer.valueOf(sTime[4]);
            int sec = Integer.valueOf(sTime[5]);
            DataInfo.setDataTime(year, month, day, hour, min, sec);
            int a = new java.math.BigInteger(String.valueOf(DataInfo.getYear() - 2000), 16).intValue();
            int b = new java.math.BigInteger(String.valueOf(DataInfo.getMonth() & 0xff), 16).intValue();
            int c = new java.math.BigInteger(String.valueOf(DataInfo.getDay() & 0xff), 16).intValue();
            int d = new java.math.BigInteger(String.valueOf(DataInfo.getHour() & 0xff), 16).intValue();
            int e = new java.math.BigInteger(String.valueOf(DataInfo.getMin() & 0xff), 16).intValue();
            int f = new java.math.BigInteger(String.valueOf(DataInfo.getSec() & 0xff), 16).intValue();

            bySpan2Time[0] = (byte) a;
            bySpan2Time[1] = (byte) b;
            bySpan2Time[2] = (byte) c;
            bySpan2Time[3] = (byte) d;
            bySpan2Time[4] = (byte) e;
            bySpan2Time[5] = (byte) f;
        }

        return copybyte(byCode, byK, byB, byHeatTemp, byHeatTime, byRange, bySpan2Time);
    }

    // 浓度参数命令封装
    private static void packageCompParUpdate(Communication port, int sCom) {

        byte[] head = {0x23, 0x23};

        byte[] end = {0x26, 0x26};
        // 数据长度
        byte[] len = {0x00, 0x22};
        // 系统编码
        byte[] sysCode = {0x32};
        // 数据类型 仪器参数
        byte[] dataType = {0x50};
        // 参数个数
        byte[] compSum = {0x0};


        byte[][] dataStr = new byte[strComponent.get(1).length][29];


        for (int i = 0; i < strComponent.get(1).length; i++) {

            DataStruct dataStruct = new DataStruct();
            dataStruct.setType(strComponent.get(1)[i]);
            dataStr[i] = getCompParArea(dataStruct);
        }
        byte[] allData = new byte[0];
        /*组装常规仪表不同元素数据字符串*/
        for (byte[] aDataStr : dataStr) {
            if (aDataStr != null) {
                allData = copybyte(allData, aDataStr);
                compSum[0] += 1;
            }
        }
        if (compSum[0] == 0) {
            return;
        }
        // 单组分数据区长度，29;
        int dataLen = (compSum[0] * dataStr[0].length) + 5;
        len[0] = (byte) (dataLen >> 8);
        len[1] = (byte) (dataLen & 0xff);

        byte[] td = copybyte(head, len, sysCode, dataType, compSum, allData);

        byte[] crcData = new byte[td.length - 2];

        arraycopy(td, 2, crcData, 0, crcData.length);

        td = copybyte(td, crc16(crcData, crcData.length), end);

        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, td);

    }

    private static byte[] getDataArea(DataStruct DataInfo) {
        //-R
        byte[] dataR = {0x2D, 0x52};


        byte[] byData = new byte[9];
        int a = new java.math.BigInteger(String.valueOf(DataInfo.getYear() - 2000), 16).intValue();
        int b = new java.math.BigInteger(String.valueOf(DataInfo.getMonth() & 0xff), 16).intValue();
        int c = new java.math.BigInteger(String.valueOf(DataInfo.getDay() & 0xff), 16).intValue();
        int d = new java.math.BigInteger(String.valueOf(DataInfo.getHour() & 0xff), 16).intValue();
        int e = new java.math.BigInteger(String.valueOf(DataInfo.getMin() & 0xff), 16).intValue();
        int f = new java.math.BigInteger(String.valueOf(DataInfo.getSec() & 0xff), 16).intValue();

        byData[0] = (byte) a;
        byData[1] = (byte) b;
        byData[2] = (byte) c;
        byData[3] = (byte) d;
        byData[4] = (byte) e;
        byData[5] = (byte) f;

        String sCode = getCode(DataInfo.getType(), strCode);
        byData[6] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(0))) + 0x30);
        byData[7] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(1))) + 0x30);
        byData[8] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(2))) + 0x30);
        //数据标识 N
        byte[] dataTag = getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), DataInfo).getBytes();

        byte[] dataBytes = copybyte(floatToBytesBigs(DataInfo.getData()));
        return copybyte(byData, dataR, dataTag, dataBytes);
    }

    // 组份，状态，流程号
    private static String getDevStatus(String Component, String info, DataStruct DataInfo) {

        String str = "N";
        if (!info.equals(context.getString(R.string.normal))) {
            return "D";
        }
        String strMode = getConfigData(Component, "runningMode");
        if (strMode.equals("2")) {// 离线
            return "F";//停运
        }
        int iStatus = getJobStatus(Component);
        if (iStatus == 19 || iStatus == 34) {
            return "C";
        }
        if (DataInfo != null) {
            String flow = DataInfo.getFlow();
            if (DataInfo.getDataTag().contains("M")) {
                return "M";
            }
            if (flow.equals("标1") || flow.equals("标2")) {
                return "S";
            }
            if (DataInfo.getDataTag().contains("T")) {
                return "T";
            }
        }
        return str;
    }

    // 浓度上传命令封装
    public static String packageMeaDataUpdate(Communication port, int sCom, DataStruct DataInfo, boolean isHistory) {

        byte[] head = {0x23, 0x23};

        byte[] end = {0x26, 0x26};
        // 数据长度
        byte[] len = {0x00, 0x15};
        // 系统编码
        byte[] sysCode = {0x32};
        // 数据类型 实时数据
        byte[] dataType = {0x52};
        // 参数个数
        byte[] compSum = {0x0};
        new HeBei();

        //时间, 污染物编码， -R ,N ,数据
        byte[][] dataStr = new byte[strComponent.get(1).length][16];
        if (isHistory) {
            String flowName;
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                flowName = null;
            } else {
                flowName = context.getResources().getString(R.string.ZY);
            }
            DataStruct[] dataStruct = new DataStruct[strComponent.get(1).length];
            for (int i = 0; i < strComponent.get(1).length; i++) {

                /*所有元素数据段内容组装*/
                dataStruct[i] = getLastHistory(strComponent.get(1)[i], flowName);
                if ((dataStruct[i].getType() != null) && ((getModePermissions(strComponent.get(1)[i], "历史数据上传")))) {
                    dataStr[i] = getDataArea(dataStruct[i]);

                } else {
                    dataStr[i] = null;
                }
            }
        } else {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                DataInfo.setType(strComponent.get(1)[i]);
                DataInfo.setDataTime(getSystemDateYear(), getSystemDateMonth(), getSystemDateDay(), getSystemDateHour(), getSystemDateMin(), getSystemDateSec());
                dataStr[i] = getDataArea(DataInfo);
            }
        }

        byte[] allData = new byte[0];
        /*组装常规仪表不同元素数据字符串*/
        for (byte[] aDataStr : dataStr) {
            if (aDataStr != null) {
                allData = copybyte(allData, aDataStr);
                compSum[0] += 1;
            }
        }
        if (compSum[0] == 0) {
            return "";
        }
        int dataLen = (compSum[0] * 16) + 5;
        len[0] = (byte) (dataLen >> 8);
        len[1] = (byte) (dataLen & 0xff);

        byte[] td = copybyte(head, len, sysCode, dataType, compSum, allData);

        byte[] crcData = new byte[td.length - 2];
        arraycopy(td, 2, crcData, 0, crcData.length);
        td = copybyte(td, crc16(crcData, crcData.length), end);

        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, td);

        return bytesToHexString(td, td.length);
    }

    public static void ParsingProtocolHeBei(final Communication port, int sCom, byte[] rs) {

        //指令长度限定
        if (rs.length == 17) {
            //帧头 帧尾
            if (rs[0] == 0x23 && rs[1] == 0x23 && rs[15] == 0x26 && rs[16] == 0x26) {
                new HeBei();
                // 支持指令
                switch (rs[2]) {
                    case 5:// 校准
                    {
                        ProtocolStruct protocolStruct = getProtocolStruct(rs);
                        if (!protocolStruct.getCompName().equals("")) {
                            jiangSuDoJobCmdParing(port, sCom, rs, protocolStruct.getCompName(), context.getResources().getString(R.string.SDJZ));
                        } else if (protocolStruct.getQnCode().equals("-49-49-49")) {
                            for (String item : strComponent.get(1)) {
                                jiangSuDoJobCmdParing(port, sCom, rs, item, context.getResources().getString(R.string.SDJZ));
                            }
                        }
                    }
                    break;
                    case 6:// 测量
                    {
                        ProtocolStruct protocolStruct = getProtocolStruct(rs);
                        if (!protocolStruct.getCompName().equals("")) {
                            jiangSuDoJobCmdParing(port, sCom, rs, protocolStruct.getCompName(), context.getResources().getString(R.string.SDZY));
                        } else if (protocolStruct.getQnCode().equals("-49-49-49")) {
                            for (String item : strComponent.get(1)) {
                                jiangSuDoJobCmdParing(port, sCom, rs, item, context.getResources().getString(R.string.SDZY));
                            }
                        }
                    }
                    break;
                    case 9://清洗
                    {

                        ProtocolStruct protocolStruct = getProtocolStruct(rs);
                        if (!protocolStruct.getCompName().equals("")) {
                            jiangSuDoJobCmdParing(port, sCom, rs, protocolStruct.getCompName(), context.getResources().getString(R.string.SDYBQX));
                        } else if (protocolStruct.getQnCode().equals("-49-49-49")) {
                            for (String item : strComponent.get(1)) {
                                jiangSuDoJobCmdParing(port, sCom, rs, item, context.getResources().getString(R.string.SDYBQX));
                            }
                        }
                    }
                    break;
                    case 7://测量： 定点时间，周期时间
                    {

                        int lxclh, lxclm;
                        int lxclt = (short) (rs[5] & 0xff);
                        lxclh = lxclt / 60;
                        lxclm = lxclt - lxclh * 60;
                        if ((lxclm < 60 && lxclm > -1) && ((lxclh < 24 && lxclh >= 0))) {
                            if (lxclh == 0 && lxclm < 2) {
                                return;
                            } else {
                                for (String item : strComponent.get(1)) {
                                    if (getModePermissions(item, "反控")) {
                                        if (doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions))) {
                                            saveOperationLogDataModifyMsg(item, "lxclm", String.valueOf(lxclm), "HeBei-连续测量分", ErrorLog.msgType.操作_信息);
                                            saveOperationLogDataModifyMsg(item, "lxclh", String.valueOf(lxclh), "HeBei-连续测量时", ErrorLog.msgType.操作_信息);
                                            updateConfigData(item, "lxclm", String.valueOf(lxclm));
                                            updateConfigData(item, "lxclh", String.valueOf(lxclh));
                                            int time = (lxclh * 60 + lxclm);
                                            getTimer_cycle(item).setTime(time, 0);
                                            getTimer_cycle(item).resume(0);
                                        }
                                    }
                                }
                            }
                        }
                        if (rs[3] < 24 && rs[3] > -1) {

                            for (final String item : strComponent.get(1)) {
                                if (getModePermissions(item, "反控")) {
                                    if (doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions))) {
                                        ProtocolStruct protocolStruct = new ProtocolStruct();
                                        protocolStruct.setCompName(item);
                                        byte[] data = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
                                        data[0] = rs[3];
                                        data[1] = rs[4];
                                        data[2] = rs[5];

                                        String sCode = getCode(protocolStruct.getCompName(), strCode);
                                        data[3] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(0))) + 0x30);
                                        data[4] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(1))) + 0x30);
                                        data[5] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(2))) + 0x30);
                                        getQnRtnCmd(port, sCom, data, rs[2]);
                                        //
                                        String s = String.valueOf(rs[3]);
                                        saveOperationLogDataModifyMsg(item, "zqclh", s, "HeBei-周期测量时间", ErrorLog.msgType.操作_信息);
                                        updateConfigData(item, "zqclh", s);
                                        syncAutoDoSample_topHour();
                                    }
                                }
                            }
                        } else {
                            return;
                        }

                    }
                    break;
                    case 8://修改时间
                    {
                        try {
                            byte[] str = new byte[6];
                            System.arraycopy(rs, 3, str, 0, 6);
                            String strs = bcd2Str(str);

                            if (strs.length() == 12) {
                                int year = Integer.parseInt(strs.substring(0, 2)) + 2000;
                                int month = Integer.parseInt(strs.substring(2, 4));
                                int day = Integer.parseInt(strs.substring(4, 6));
                                int hour = Integer.parseInt(strs.substring(6, 8));
                                int minute = Integer.parseInt(strs.substring(8, 10));
                                int second = Integer.parseInt(strs.substring(10, 12));
                                if (year > 2000 && year < 3000 && month > 0 && month < 13 && day > 0 && day < 32 && hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60) {

                                    for (String item : strComponent.get(1)) {
                                        byte[] data = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
                                        System.arraycopy(rs, 3, data, 0, 6);

                                        String sCode = getCode(item, strCode);
                                        data[6] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(0))) + 0x30);
                                        data[7] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(1))) + 0x30);
                                        data[8] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(2))) + 0x30);
                                        getQnRtnCmd(port, sCom, data, rs[2]);
                                    }
                                    saveOperationLogMsg("公共", "Hebei-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
                                    /*setDate(year, month - 1, day);
                                    setTime(hour, minute, second);*/
                                    SetDateTime("all",year, month - 1, day,hour, minute, second);
                                    byte[] timeByte = copybyte(toByteArray(year, 4), toByteArray((month), 4),
                                            toByteArray(day, 4), toByteArray(hour, 4),
                                            toByteArray(minute, 4), toByteArray((second), 4));
                                    for (String item : strComponent.get(1)) {
                                        SendManager.SendCmd(item + "_时间管理_06_0", S0, 3, 200, timeByte);
                                        //时间矫正
                                        //AddError(item, 614, 运维_信息);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                    case 0x0b:
                        for (String item : strComponent.get(1)) {
                            if (!getModePermissions(item, "反控")) {
                                return;
                            }
                        }
                        packageCompParUpdate(port, sCom);
                        break;
                    case 0x01:
                        DataStruct DataInfo = new DataStruct();
                        DataInfo.setType(strComponent.get(1)[0]);
                        if ((getModePermissions(strComponent.get(1)[0], "历史数据上传"))) {
                            packageMeaDataUpdate(port, sCom, DataInfo, true);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    }

    // 反控启动流程，解析及反馈指令
    private static void jiangSuDoJobCmdParing(Communication port, int sCom, byte[] rs, String item, String flowName) {
        if (getModePermissions(item, "反控")) {
            if (doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions))) {
                doControlJob(item, flowName);

                byte[] data = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
                String sCode = getCode(item, strCode);
                data[0] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(0))) + 0x30);
                data[1] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(1))) + 0x30);
                data[2] = (byte) (Byte.parseByte(String.valueOf(sCode.charAt(2))) + 0x30);
                getQnRtnCmd(port, sCom, data, rs[2]);
            }
        }
    }

    // 协议指定QN
    private static ProtocolStruct getProtocolStruct(byte[] rs) {
        ProtocolStruct protocolStruct = new ProtocolStruct();
        byte[] qnCode = new byte[3];
        arraycopy(rs, 3, qnCode, 0, qnCode.length);
        qnCode[0] -= 0x30;
        qnCode[1] -= 0x30;
        qnCode[2] -= 0x30;
        protocolStruct.setQnCode(qnCode[0] + String.valueOf(qnCode[1]) + qnCode[2]);

        //根据污染物编码匹配已经配置的组份
        String[] list = getNowCfgCompNameCde(strCode);
        for (int i = 0; i < list.length; i++) {
            if (protocolStruct.getQnCode().equals(list[i])) {
                protocolStruct.setCompName(strComponent.get(1)[i]);
                break;
            }
        }
        return protocolStruct;
    }
}
