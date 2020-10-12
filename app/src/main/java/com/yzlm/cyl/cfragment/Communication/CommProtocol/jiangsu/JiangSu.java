package com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolStruct;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.CheckCrc;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.findCrc;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCrc16;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataCmd;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getMeaData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getMeaDataVol;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getNowCfgCompNameCde;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.iDataFormat;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.replaceTempData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCmdFileGB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCodeFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.getNowSysTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustSettingSpaceHour;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal1Status;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal1Value;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal1Vol;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal2DataTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal2Value;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal2Vol;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevFaultStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLastHistory;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getMeaLimitHigh;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNoLiquidErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeH;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeK;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getOtherErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTimeWithOutms;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStringMeaDataTime;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.isDigit;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat2;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.protocolName;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

public class JiangSu {
    private static String[] strCode = new String[30];
    private static String[] strCmd = new String[40];


    private final static int COC_CH = 1;               /*1测量值上传*/
    private final static int QnRtn_CH = 3;               /*应答指令格式*/
    private final static int ExeRtn_CH = 5;               /*执行回答指令格式*/

    private final static int Par1_CH = 7;               /*参数1*/
    private final static int Par2_CH = 9;               /*参数2*/

    private static String MN = "123456789101112131415161";

    public JiangSu() {

        if (strCode[0] == null) {
            strCode = AnalysisCodeFile("Csoft/Protocol/jiangsu/", "CodeJS");
        }
        if (strCmd[COC_CH] == null) {
            strCmd = AnalysisCmdFileGB("Csoft/Protocol/jiangsu/", "jiangsu");
        }

    }


    // 获取组份上传命令
    public String getMeaDataUpdateCmdJiangSu(DataStruct DataInfo, boolean isHistory) {
        if (strCmd[COC_CH] == null) {
            strCmd = AnalysisCmdFileGB("Csoft/Protocol/jiangsu/", "jiangsu");
            if (strCmd[COC_CH] == null) {
                return "";
            }
        }
        return packageMeaDataUpdate(strCmd[COC_CH], DataInfo, isHistory);
    }


    // 浓度上传命令封装
    private static String packageMeaDataUpdate(String cmd, DataStruct DataInfo, boolean isHistory) {
        String dataTime = "";
        String[] StrHeadType = {"$MN$", "XXXX", "$SysTime2$"};

        String[] StrDataType = {"$Code$", "$MeaData$", "$JobTime$", "$Alarm$", "$State$", "$Vol$", "$Flag$"};
        String[] StrEnd = {"CRC"};
        String[] strings = cmd.split("@");

        String[] dataStr = new String[strComponent.get(1).length];
        DataStruct[] dataStruct = new DataStruct[strComponent.get(1).length];


        if (isHistory) {
            String flowName;
            String strDataFormat = strings[1];
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                flowName = null;
            } else {
                flowName = context.getResources().getString(R.string.ZY);
            }
            /*所有元素数据段内容组装*/
            for (int i = 0; i < strComponent.get(1).length; i++) {

                dataStruct[i] = getLastHistory(strComponent.get(1)[i], flowName);
                dataStr[i] = "";
                if ((dataStruct[i].getType() != null) && ((getModePermissions(strComponent.get(1)[i], "历史数据上传")))) {

                    dataStr[i] = strDataFormat;
                    //对数据区内容进行解析
                    getDataAreaContentParsing(StrDataType, dataStr, dataStruct[i], i);

                }
            }
        } else {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                dataStr[i] = "";

                DataInfo.setType(strComponent.get(1)[i]);
                dataStr[i] = strings[1];
                //对数据区内容进行解析
                getDataAreaContentParsing(StrDataType, dataStr, DataInfo, i);

            }
        }
        /*组装常规仪表不同元素数据字符串*/
        String strData = "";
        for (String aDataStr : dataStr) {
            if (!aDataStr.equals("")) {
                strData += aDataStr;
                strData += ";";
            }
        }

        if (strData.equals("")) {
            return "";
        }
/*
            帧头组装*/
        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.valueOf(time[0]);
        int month = Integer.valueOf(time[1]);
        int day = Integer.valueOf(time[2]);
        int hour = Integer.valueOf(time[3]);
        int min = Integer.valueOf(time[4]);
        int sec = Integer.valueOf(time[5]);
        int ms = Integer.valueOf(time[6]);
        String[] headTemp = new String[3];
        headTemp[0] = getNowSysTime(year, month, day, hour, min, sec);
        cmd = strings[0].replace(StrHeadType[2], headTemp[0]);
        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("JS_ORG_ADDR").equals(""))
            strMn[0] = getPublicConfigData("JS_ORG_ADDR") + getPublicConfigData("JS_DEV_ADDR");
        else
            strMn[0] = MN;
        cmd = cmd.replace(StrHeadType[0], strMn[0]);

        /*头+数据段+尾*/
        cmd += strData;
/*
将数据内容区的最后一个符号";"去除*/

        cmd = cmd.substring(0, cmd.length() - 1);

        cmd += strings[2];
        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        cmd = replaceTempData(cmd, StrHeadType, 1, strLen, 0, 1);
        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrEnd[0] += String.valueOf(findCrc(cmd));
        /*替换CRC*/
        cmd = replaceTempData(cmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
        return cmd;
    }

    //对数据区内容进行解析
    private static void getDataAreaContentParsing(String[] strDataType, String[] dataStr, DataStruct dataStruct, int i) {
        //获取数据区内容
        // 将数据区进行替换
        //替换污染物编码
        dataStr[i] = dataStr[i].replace(strDataType[0], getCode(dataStruct.getType(), strCode));
        //替换测量值
        dataStr[i] = dataStr[i].replace(strDataType[1], getMeaData(dataStruct.getData(), Integer.parseInt(getConfigData(dataStruct.getType(), "YXWS"))));
        //测量数据时间
        dataStr[i] = dataStr[i].replace(strDataType[2], getDataTime(dataStruct.getYear(), dataStruct.getMonth(), dataStruct.getDay(), dataStruct.getHour(), dataStruct.getMin(), dataStruct.getSec()));
        //报警信息
        dataStr[i] = dataStr[i].replace(strDataType[3], iDataFormat(String.valueOf(getAlarmNum(dataStruct.getType())), 2));
        //设备状态
        dataStr[i] = dataStr[i].replace(strDataType[4], iDataFormat(String.valueOf(getDevStatus(dataStruct.getType())), 2));
        //吸光度能量
        dataStr[i] = dataStr[i].replace(strDataType[5], getMeaDataVol(dataStruct.getAi(), 4));
        // 数据标识
        dataStr[i] = dataStr[i].replace(strDataType[6], getDataTag(dataStruct.getType(), dataStruct));
    }


    private static int getAlarmNum(String compName) {

        if (!workState.get(compName).equals(context.getString(R.string.normal))) {
            if (workState.get(compName).contains("-")) {
                try {
                    int getErrorNum = Integer.parseInt(workState.get(compName).replace(" ", "").split("-")[0]);
                    switch (getNoLiquidErrorStatus(getErrorNum)) {
                        case 1:
                            return 2;//缺水样
                        case 2:
                            return 4;// 缺零样
                        case 3:
                        case 4:
                            return 3;//缺试剂
                        default:
                            break;
                    }
                    switch (getOtherErrorStatus(getErrorNum)) {
                        case 8:
                            return 5;// 加热故障
                        case 10:
                            return 7;// 测量值超量程异常
                        case 5:// 漏液
                            return 1;
                    }
                    switch (getDevFaultStatus(getErrorNum)) {
                        case 2:
                            return 5;
                        case 3:
                        case 4:
                            return 1;
                    }
                    return 8;
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
            }
        }
        return 0;
    }


    private static int getDevStatus(String compName) {

        int iNowStatus = getJobStatus(compName);
        String strMode = getConfigData(compName, "runningMode");
        if (iNowStatus == 30) {
            return 6;//故障
        }
        switch (strMode) {
            case "1": // 在线
                switch (iNowStatus) {
                    case 0://待机
                        return 1;
                    case 19:// 校准
                    case 34:
                        return 2;
                    case 10://清洗
                        return 3;
                    case 1://测量
                        return 4;
                    case 28:
                    case 29://标测
                        return 7;
                    default:
                        return 8;
                }
            case "2": // 离线
                return 8;
            default:
                // 维护
                return 5;
        }

    }


    public static String getDataTag(String compName, DataStruct historyData) {
        if (!workState.get(compName).equals(context.getString(R.string.normal))) {
            return "D";
        }
        String strMode = getConfigData(compName, "runningMode");
        if (strMode.equals("2")) {// 离线
            return "F";//停运
        }
        int iStatus = getJobStatus(compName);
        if (iStatus == 19 || iStatus == 34) {
            return "C";
        }
        if (historyData.getFlow().contentEquals(context.getText(R.string.B1)) || historyData.getFlow().contentEquals(context.getText(R.string.B2))) {
            return "S";
        }
        if (historyData.getDataTag().contains("M")) {
            return "M";
        } else if (historyData.getDataTag().contains("T")) {
            return "T";
        } else {
            return "N";
        }
    }


    public static void ParsingProtocolJiangSuDt(Communication port, int sCom, byte[] rs) {
        if (rs != null && rs.length > 18) {
            ProtocolStruct protocolStruct = new ProtocolStruct();

            protocolStruct.setQnMn(getKeySubString(rs, "MN=", 24));
            protocolStruct.setQnPassword(getKeySubString(rs, "PW=", 6));
            protocolStruct.setQnTime(getKeySubString(rs, "QN=", 17));
            protocolStruct.setNowProtocol(protocolName[5]);

            if (CheckCrc(rs)) {
                try {
                    protocolStruct.setQnCode(getKeySubString(rs, "PolId=", 6));
                    protocolStruct.setQnCn(getKeySubString(rs, "CN=", 4));
                    protocolStruct.setQnST(getKeySubString(rs, "ST=", 2));
                    protocolStruct.setQnFlag(getKeySubString(rs, "Flag=", 1));

                    if (protocolStruct.getQnCn().equals("") || devSupportCn(protocolStruct.getQnCn()).equals("")) {
                        //QnRtn 8 CN错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR8_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 8).getBytes());
                        return;
                    } else if (protocolStruct.getQnMn().equals("") || !protocolStruct.getQnMn().equals(getPublicConfigData("JS_ORG_ADDR") + getPublicConfigData("JS_DEV_ADDR"))) {
                        //QnRtn 4 MN错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR4_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 4).getBytes());
                        return;
                    } else if (protocolStruct.getQnPassword().equals("")) {
                        //QnRtn 3 PW错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR3_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 3).getBytes());
                        return;
                    } else if (protocolStruct.getQnST().equals("") || !protocolStruct.getQnST().equals("32")) {
                        // 5 ST 错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR5_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 5).getBytes());
                        return;
                    } else if (protocolStruct.getQnFlag().equals("") || !protocolStruct.getQnFlag().equals("5")) {
                        // 6 FLAG 错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR6_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 6).getBytes());
                        return;
                    } else if (!protocolStruct.getQnTime().equals("")) {
                        if (!isDigit(protocolStruct.getQnTime())) {
                            // 7 QN 错误
                            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR7_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 7).getBytes());
                            return;
                        }
                    }
                    new JiangSu();
                    if (strCode != null) {
                        //根据污染物编码匹配已经配置的组份
                        String[] list = getNowCfgCompNameCde(strCode);
                        for (int i = 0; i < list.length; i++) {
                            if (protocolStruct.getQnCode().equals(list[i])) {
                                protocolStruct.setCompName(strComponent.get(1)[i]);
                                break;
                            }
                        }
                    }
                    if (strCmd[COC_CH] != null) {
                        protocolStruct.setPassword(getKeySubString(strCmd[COC_CH].getBytes(), "PW=", 6));
                    }
                    if (!protocolStruct.getQnPassword().equals(protocolStruct.getPassword())) {
                        //QnRtn 3 PW错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR3_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 3).getBytes());
                        return;
                    }
                    if (!protocolStruct.getCompName().equals("")) {
                        if (getModePermissions(protocolStruct.getCompName(), "反控")) {

                            int exeRtn = 1;
                            switch (devSupportCn(protocolStruct.getQnCn())) {
                                case "3012":
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());

                                    if (doFlowing.get(protocolStruct.getCompName()).equals(context.getString(R.string.waiting_for_instructions))) {
                                        if (!workState.get(protocolStruct.getCompName()).equals(context.getString(R.string.normal))) {
                                            exeRtn = 6;
                                        } else {
                                            doControlJob(protocolStruct.getCompName(), context.getResources().getString(R.string.SDZY));
                                        }
                                    } else {
                                        exeRtn = 5;
                                    }
                                    break;
                                case "3011":
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());

                                    if (doFlowing.get(protocolStruct.getCompName()).equals(context.getString(R.string.waiting_for_instructions))) {
                                        if (!workState.get(protocolStruct.getCompName()).equals(context.getString(R.string.normal))) {
                                            exeRtn = 6;
                                        } else {
                                            doControlJob(protocolStruct.getCompName(), context.getResources().getString(R.string.SDJZ));
                                        }
                                    } else {
                                        exeRtn = 5;
                                    }
                                    break;
                                case "1012":
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());

                                    String strSysTime = getKeySubString(rs, "SystemTime=", 14);
                                    if (strSysTime.equals("")) {
                                        strSysTime = protocolStruct.getQnTime();
                                    }
                                    if (!strSysTime.equals("")) {
                                        int year = Integer.parseInt(strSysTime.substring(0, 4));
                                        int month = Integer.parseInt(strSysTime.substring(4, 6));
                                        int day = Integer.parseInt(strSysTime.substring(6, 8));
                                        int hour = Integer.parseInt(strSysTime.substring(8, 10));
                                        int minute = Integer.parseInt(strSysTime.substring(10, 12));
                                        int second = Integer.parseInt(strSysTime.substring(12, 14));

                                        saveOperationLogMsg(protocolStruct.getCompName(), "JiangSU-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
                                        //时间矫正
                                        /*AddError(protocolStruct.getCompName(), 614, 运维_信息);
                                        setDate(year, month - 1, day);
                                        setTime(hour, minute, second);*/
                                        SetDateTime(protocolStruct.getCompName(),year, month - 1, day,hour, minute, second);
                                        byte[] timeByte = copybyte(toByteArray(year, 4), toByteArray((month), 4),
                                                toByteArray(day, 4), toByteArray(hour, 4),
                                                toByteArray(minute, 4), toByteArray((second), 4));
                                        SendManager.SendCmd(protocolStruct.getCompName() + "_时间管理_06_0", S0, 3, 200, timeByte);
                                    } else {
                                        exeRtn = 3;
                                    }
                                    break;
                                case "3022"://测量参数
                                    DataStruct dataStruct = new DataStruct();
                                    dataStruct.setType(protocolStruct.getCompName());
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getPar1Cmd(dataStruct, protocolStruct).getBytes());
                                    return;
                                case "3020":// 校准信息
                                    DataStruct dataStruct1 = new DataStruct();
                                    dataStruct1.setType(protocolStruct.getCompName());
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getPar2Cmd(dataStruct1, protocolStruct).getBytes());
                                    return;
                            }
                            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getExeRtnCmd(protocolStruct, exeRtn).getBytes());
                            return;
                        } else {
                            // QnRtn 2 请求被拒绝
                            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR2_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 2).getBytes());
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e("江苏协议解析异常", e.toString());
                    // QnRtn 100  未知错误
                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR100_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 100).getBytes());
                    return;
                }
            } else {
                /*回复CRC校验错误指令**/
                //QnRtn 9 CRC 错误
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR9_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 9).getBytes());
            }
        }
    }

    /*设备支持CN 码*/
    private static String devSupportCn(String strCn) {

        switch (strCn) {
            case "3012":
                //反控测量
                break;
            case "3011":
                //反控校准
                break;
            case "1012":
                //校时
                break;
            case "3022"://
                break;
            case "3020":
                break;
            default:
                return "";
        }
        return strCn;
    }


    public static String getKeySubString(byte[] rs, String strKey, int valueLen) {
        String str = new String(rs);
        String strRet = "";

        int cnStart = str.indexOf(strKey);
        if (cnStart != -1) {
            strRet = str.substring(cnStart + strKey.length(), cnStart + strKey.length() + valueLen);
        }
        return strRet;

    }

    public static String packageQnRtnCmd(String strCmd, ProtocolStruct protocolStruct, int qnRtn) {

        protocolStruct.setQnRtn(String.valueOf(qnRtn));

        strCmd = parKeyAnalysis(strCmd, "$QnTime$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$QnMN$", null, protocolStruct);
        // 替换请PW
        strCmd = strCmd.replace(getKeySubString(strCmd.getBytes(), "PW=", 6), protocolStruct.getQnPassword());

        strCmd = parKeyAnalysis(strCmd, "$QnRtn$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "XXXX", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "CRC", null, protocolStruct);
        return strCmd;
    }

    /*Qn RTN*/
    private static String getQnRtnCmd(ProtocolStruct protocolStruct, int qnRtn) {

        if (strCmd[QnRtn_CH] == null) {
            strCmd = AnalysisCmdFileGB("Csoft/Protocol/jiangsu/", "jiangsu");
            if (strCmd[QnRtn_CH] == null) {
                return "";
            }
        }

        return packageQnRtnCmd(strCmd[QnRtn_CH], protocolStruct, qnRtn);

    }


    public static String packageExeRtnCmd(String strCmd, ProtocolStruct protocolStruct, int exeRtn) {

        protocolStruct.setQnExe(String.valueOf(exeRtn));

        strCmd = parKeyAnalysis(strCmd, "$QnTime$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$QnMN$", null, protocolStruct);

        strCmd = strCmd.replace(getKeySubString(strCmd.getBytes(), "PW=", 6), protocolStruct.getQnPassword());

        /*处理ExeRtn 指令*/
        strCmd = parKeyAnalysis(strCmd, "$ExeRtn$", null, protocolStruct);

        strCmd = parKeyAnalysis(strCmd, "XXXX", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "CRC", null, protocolStruct);

        return strCmd;
    }


    /*Exe Rtn 指令**/
    private static String getExeRtnCmd(ProtocolStruct protocolStruct, int exeRtn) {

        if (strCmd[ExeRtn_CH] == null) {
            strCmd = AnalysisCmdFileGB("Csoft/Protocol/jiangsu/", "jiangsu");
            if (strCmd[ExeRtn_CH] == null) {
                return "";
            }
        }

        return packageExeRtnCmd(strCmd[ExeRtn_CH], protocolStruct, exeRtn);

    }


    private static String packagePar1Cmd(String strCmd, DataStruct dataStruct, ProtocolStruct protocolStruct) {


        strCmd = parKeyAnalysis(strCmd, "$SysTime$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$MN$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$SysTime2$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Code$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$State$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Alarm$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$DevRange$", dataStruct, protocolStruct);

        if (getSetWinParShow(dataStruct.getType(), "消解温度")) {
            strCmd = parKeyAnalysis(strCmd, "$HeatTemp$", dataStruct, protocolStruct);
        } else {
            String[] strCmds = strCmd.split("\\$HeatTemp\\$");
            int ch = strCmds[0].lastIndexOf(";");
            strCmd = strCmd.substring(0, ch) + strCmds[1];
        }
        if (getSetWinParShow(dataStruct.getType(), "消解时长")) {
            strCmd = parKeyAnalysis(strCmd, "$HeatTime$", dataStruct, protocolStruct);
        } else {
            String[] strCmds = strCmd.split("\\$HeatTime\\$");
            int ch = strCmds[0].lastIndexOf(";");
            strCmd = strCmd.substring(0, ch) + strCmds[1];
        }
        strCmd = parKeyAnalysis(strCmd, "$NowRangeK$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$NowRangeB$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "XXXX", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "CRC", null, protocolStruct);

        return strCmd;
    }


    /*参数1 当前状态，报警状态，量程 消解温度，消解时长 截距  斜率 指令**/
    private static String getPar1Cmd(DataStruct dataStruct, ProtocolStruct protocolStruct) {

        if (strCmd[Par1_CH] == null) {
            strCmd = AnalysisCmdFileGB("Csoft/Protocol/jiangsu/", "jiangsu");
            if (strCmd[Par1_CH] == null) {
                return "";
            }
        }
        return packagePar1Cmd(strCmd[Par1_CH], dataStruct, protocolStruct);
    }

    private static String packagePar2Cmd(String strCmd, DataStruct dataStruct, ProtocolStruct protocolStruct) {
        String[] StrEnd = {"CRC"};
        strCmd = parKeyAnalysis(strCmd, "$SysTime$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$MN$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Code$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$ZeroResult$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$AutoAdjust$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$AutoAJHour$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$MeaLimit$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Span2Time$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Span1Value$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Span1Vol$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Span2Value$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Span2Vol$", dataStruct, protocolStruct);

        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = iDataFormat(String.valueOf(getDataCmd(strCmd, "ST", "&&").length()), 4);
        strCmd = replaceTempData(strCmd, new String[]{"XXXX"}, 0, strLen, 0, 1);
        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(strCmd, "ST", "&&"), findCrc(strCmd));
        StrEnd[0] += String.valueOf(findCrc(strCmd));
        /*替换CRC*/
        strCmd = replaceTempData(strCmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
        return strCmd;
    }


    /*参数2 检出限 ，校准方式，自动校准时间间隔（小时）。校准时间，最近一次标定时间，调零结果， 调零浓度，调零吸光度，标定浓度，标定吸光度**/
    private static String getPar2Cmd(DataStruct dataStruct, ProtocolStruct protocolStruct) {

        if (strCmd[Par2_CH] == null) {
            strCmd = AnalysisCmdFileGB("Csoft/Protocol/jiangsu/", "jiangsu");
            if (strCmd[Par2_CH] == null) {
                return "";
            }
        }
        return packagePar2Cmd(strCmd[Par2_CH], dataStruct, protocolStruct);

    }

    /*
     * 协议KEY  解析 替换
     * cmd  指令
     * key， 关键字
     *
     * DataInfo 数据包封装
     * protocolStruct 协议封装
     * **/
    private static String parKeyAnalysis(String cmd, String key, DataStruct DataInfo, ProtocolStruct protocolStruct) {

        int point;
        try {
            point = Integer.parseInt(getConfigData(DataInfo.getType(), "YXWS"));
        } catch (Exception e) {
            point = 2;
        }
        switch (key) {
            case "$MN$":
                String[] strMn = new String[1];
                strMn[0] = MN;
                if (protocolStruct.getNowProtocol().equals(protocolName[5])) {
                    /*JSMN号*/
                    if (!getPublicConfigData("JS_ORG_ADDR").equals(""))
                        strMn[0] = getPublicConfigData("JS_ORG_ADDR") + getPublicConfigData("JS_DEV_ADDR");
                }
                cmd = cmd.replace(key, strMn[0]);
                break;
            case "$Code$":
                //GB2017/江苏协议  替换污染物编码
                cmd = cmd.replace(key, getCode(DataInfo.getType(), strCode));
                break;
            case "$MeaData$":
                //替换测量值
                cmd = cmd.replace(key, getMeaData(DataInfo.getData(), point));
                break;
            case "$JobTime$"://测量数据时间
                cmd = getStringMeaDataTime(cmd, key, DataInfo);
                break;
            case "$Alarm$"://报警信息
                cmd = cmd.replace(key, iDataFormat(String.valueOf(getAlarmNum(DataInfo.getType())), 2));
                break;
            case "$State$"://设备状态
                cmd = cmd.replace(key, iDataFormat(String.valueOf(getDevStatus(DataInfo.getType())), 2));
                break;
            case "$Vol$"://吸光度能量
                cmd = cmd.replace(key, dataFormat2(String.valueOf(DataInfo.getAi()), point));
                break;
            case "$Flag$":  // 数据标识
                cmd = cmd.replace(key, getDataTag(DataInfo.getType(), DataInfo));
                break;
            case "CRC":
                /*QN 到 && 之间为数据区
                获取CRC*/
                String[] StrEnd = {key};
                String[] strCrc = new String[1];
                strCrc[0] = getCrc16(getDataCmd(cmd, "QN", "&&"), findCrc(cmd));
                if (cmd.contains("CRC1")) {
                    StrEnd[0] += "1";
                } else if (cmd.contains("CRC2")) {
                    StrEnd[0] += "2";
                } else {
                    StrEnd[0] += "0";
                }
                /*替换CRC*/
                cmd = replaceTempData(cmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
                break;
            case "XXXX":
                 /*QN 到 && 之间为数据区
                /*命令长度*/
                String[] strLen = new String[1];
                strLen[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "QN", "&&").length()), 4);
                cmd = cmd.replace(key, strLen[0]);
                break;
            case "$SysTime2$":/*系统时间不包含毫秒*/ {
                cmd = cmd.replace(key, getStrNowSysTemTimeWithOutms());
            }
            break;
            case "$SysTime$":/*系统时间包含毫秒*/ {
                cmd = cmd.replace(key, getStrNowSysTemTime());
            }
            break;
            case "$DevRange$":// 仪器量程H
                cmd = cmd.replace(key, dataFormat(Float.parseFloat(getNowRangeH(DataInfo.getType())), 2));
                break;
            case "$HeatTemp$"://消解温度
                cmd = cmd.replace(key, dataFormat2(Float.parseFloat(getHeatTemp(DataInfo.getType())), 4, 1));
                break;
            case "$HeatTime$"://消解时长
                cmd = cmd.replace(key, iDataFormat(String.valueOf(getHeatTime(DataInfo.getType())), 4));
                break;
            case "$NowRangeK$"://当前量程K
                cmd = cmd.replace(key, dataFormat2(getNowRangeK(DataInfo.getType()), point));
                break;
            case "$NowRangeB$"://当前量程B
                cmd = cmd.replace(key, dataFormat2(getNowRangeB(DataInfo.getType()), point));
                break;
            case "$ZeroResult$"://标1结果
                cmd = cmd.replace(key, getCal1Status(DataInfo.getType()));
                break;
            case "$AutoAdjust$":// 自动校准开关
                cmd = cmd.replace(key, getAutoJustStatus(DataInfo.getType()).equals("true") ? "1" : "0");
                break;
            case "$AutoAJHour$"://自动校准设置间隔时间
                cmd = cmd.replace(key, String.valueOf(getAutoJustSettingSpaceHour(DataInfo.getType())));
                break;
            case "$MeaLimit$"://测量检出限
                cmd = cmd.replace(key, dataFormat2(getMeaLimitHigh(DataInfo.getType()), point));
                break;
            case "$Span2Time$":// 标2时间
                String cal2_time;
                cal2_time = getCal2DataTime(DataInfo.getType());
                if (cal2_time.equals("")) {
                    cal2_time = "00000000000000";
                }
                cmd = cmd.replace(key, cal2_time);
                break;
            case "$Span1Value$":// 标1浓度
                cmd = cmd.replace(key, dataFormat2(getCal1Value(DataInfo.getType()), point));
                break;
            case "$Span1Vol$"://标1吸光度
                cmd = cmd.replace(key, dataFormat2(getCal1Vol(DataInfo.getType()), point));
                break;
            case "$Span2Value$":// 标2浓度
                cmd = cmd.replace(key, dataFormat2(getCal2Value(DataInfo.getType()), point));
                break;
            case "$Span2Vol$"://标2吸光度
                cmd = cmd.replace(key, dataFormat2(getCal2Vol(DataInfo.getType()), point));
                break;
            case "$QnTime$":// 指令请求时间
                // 替换请求命令时间
                cmd = cmd.replace(key, protocolStruct.getQnTime());
                break;
            case "$QnMN$":
                cmd = cmd.replace(key, protocolStruct.getQnMn());
                break;
            case "$QnRtn$":
                cmd = cmd.replace(key, protocolStruct.getQnRtn());
                break;
            case "$ExeRtn$":
                cmd = cmd.replace(key, protocolStruct.getQnExe());
                break;
        }
        return cmd;

    }


}
