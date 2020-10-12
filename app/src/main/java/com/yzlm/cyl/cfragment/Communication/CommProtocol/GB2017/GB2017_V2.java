package com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.Protocol;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolStruct;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.CheckCrc;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCrc16;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataCmd;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getNowCfgCompNameCde;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.iDataFormat;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.replaceTempData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCmdFileGB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCodeFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustSettingSpaceHour;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal1Value;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal1Vol;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal2Value;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCal2Vol;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCalSucTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorCoolTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorStandTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getContinueMeaSetting;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevMeaMode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevNowRangeNum;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevRangeH;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatCoolTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLastHistory;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getMeaLimitHigh;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getMeaPoint;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNoLiquidErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeExtRangeB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeExtRangeK;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeH;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeK;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeL;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getOtherErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getRunningMode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTimeWithOutms;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStringMeaDataTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.setOnLineModeSetMeaMode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setColorCoolTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setColorStandTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setColorTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setColorTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setHeatCoolTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setHeatTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setHeatTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setOxidantVolume;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setReductantCoc;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setReductantVolume;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setTitrationVolume;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setWaitStand;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolWrite.setWaterVolume;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu.getDataTag;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu.getKeySubString;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu.packageExeRtnCmd;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu.packageQnRtnCmd;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.isDigit;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat2;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.protocolName;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

public class GB2017_V2 extends Protocol {

    private static String[] strCode2017 = new String[30];
    private static String[] strCmd2017 = new String[40];
    private final static int COC_CH = 1;               /*1测量值上传*/


    public GB2017_V2() {
        if (strCode2017[0] == null) {
            strCode2017 = AnalysisCodeFile("Csoft/Protocol/GB/", "Code2017V2");
        }
        if (strCmd2017[1] == null) {
            strCmd2017 = AnalysisCmdFileGB("Csoft/Protocol/GB/", "GB2017V2");
        }
    }

    @Override
    public String getMeaDataValueUpLoading(DataStruct DataInfo, boolean isHistory) {
        if (strCmd2017[COC_CH] == null) {
            strCmd2017 = AnalysisCmdFileGB("Csoft/Protocol/GB/", "GB2017V2");
            if (strCmd2017[COC_CH] == null) {
                return "";
            }
        }
        return packageMeaDataUpdate(strCmd2017[COC_CH], DataInfo, isHistory);
    }


    // 测量值数据上传中已经包含
    public String getMeaDataVolUpdate() {

        return null;
    }

    /*设备支持CN 码*/
    private static String devSupportCn(String strCn) {

        switch (strCn) {
            case "1011":
                //提取时间
                break;
          /*  case "3017":
                //提取采样时间周期
                break;*/
            case "3020":
                //提取现场机工作状态、报警状态、消解温度等
                break;
            case "3011":
                //反控校准
                break;
            case "3012":
                //反控测量
                break;
            case "3013":
                //反控清洗
                break;
            case "3014":
                //反控标样2测量
                break;
            case "3022":
                //反控停止
                break;
            case "3023":
                //反控冲洗
                break;
            case "3024":
                //反控零样测量
                break;
            case "3025":
                //反控标样1测量
                break;
            case "1012":
                //设置现场机时间
                break;
            case "3016":
                //设置定点做样 间隔时间
                break;
            case "3021":
                //设置消解温度、显色温度等参数
                break;
            case "3026":
                //设置测量模式
                break;
            default:
                return "";
        }
        return strCn;
    }

    /*Qn RTN*/
    private static String getQnRtnCmd(ProtocolStruct protocolStruct, int qnRtn) {
        String strQnRtn = "##XXXXQN=$QnTime$;ST=91;CN=9011;PW=123456;MN=$QnMN$;Flag=4;CP=&&QnRtn=$QnRtn$&&CRC1";

        return packageQnRtnCmd(strQnRtn, protocolStruct, qnRtn);

    }

    /*Exe Rtn 指令**/
    private static String getExeRtnCmd(ProtocolStruct protocolStruct, int exeRtn) {
        String strExeRtn = "##XXXXQN=$QnTime$;ST=91;CN=9012;PW=123456;MN=$QnMN$;Flag=4;CP=&&ExeRtn=$ExeRtn$&&CRC1";

        return packageExeRtnCmd(strExeRtn, protocolStruct, exeRtn);

    }


    /*工作状态*/
    private static int getJobState(DataStruct dataStruct) {

        int status = getJobStatus(dataStruct.getType());
        int iRunningMode = getRunningMode(dataStruct.getType());
        if (iRunningMode == 3) {
            return 1;
        }
        switch (status) {
            case 19:
                return 3;
            case 34:
                return 8;
            case 30:
                return 2;
            case 10:
                return 5;
            case 1:
                return 7;
            case 0:
                return 9;
            case 5:
                return 11;
            case 28:
                return 12;
            case 29:
                return 13;
            case 32:
                return 14;
            case 35:
                return 15;
            case 3:
                return 16;
            case 4:
                return 17;
            case 2:
                return 18;
            case 6:
                return 19;
            case 7:
                return 20;
        }
        return 0;
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
                            return 3;// 缺零样
                        case 3:
                            return 4;// 缺标样
                        case 4:
                            return 1;//缺试剂
                        default:
                            break;
                    }
                    switch (getOtherErrorStatus(getErrorNum)) {
                        case 0:
                            break;
                        case 6:
                            return 6;// 标定异常
                        case 8:
                            return 8;// 加热故障
                        case 7:
                            return 7;// 测量值超量程异常
                        case 5:
                            return 5;// 漏液
                        case 9:
                            return 9;// 低试剂预警
                        case 10:
                            return 10;// 超上限
                        case 11:
                            return 11;// 超下限
                        case 13:
                            return 13;// 滴定异常
                        case 14:
                            return 14;// 电极异常
                        case 15:
                            return 15;// 量程切换
                        default:
                            return 12;// 其它异常
                    }
                    /*switch (getDevFaultStatus(getErrorNum)) {
                        case 2:
                            return 5;
                        case 3:
                        case 4:
                            return 1;
                    }
                    return 8;*/
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
            }
        }
        return 0;
    }


    @Override
    public void ParsingProtocolPortData(Communication port, int sCom, byte[] rs) {
        if (rs != null && rs.length > 18) {
            ProtocolStruct protocolStruct = new ProtocolStruct();

            protocolStruct.setQnMn(getKeySubString(rs, "MN=", 24));
            protocolStruct.setQnPassword(getKeySubString(rs, "PW=", 6));
            protocolStruct.setQnTime(getKeySubString(rs, "QN=", 17));
            protocolStruct.setNowProtocol(protocolName[7]);
            // 存本地MN
            protocolStruct.setMn(getPublicConfigData("GB2017V2ORG_ADDR") + getPublicConfigData("GB2017V2DEV_ADDR"));

            if (CheckCrc(rs)) {
                try {
                    protocolStruct.setQnCode(getKeySubString(rs, "PolId=", 6));
                    protocolStruct.setQnCn(getKeySubString(rs, "CN=", 4));
                    protocolStruct.setQnST(getKeySubString(rs, "ST=", 2));
                    protocolStruct.setQnFlag(getKeySubString(rs, "Flag=", 1));

                    if (protocolStruct.getQnCn().equals("") || devSupportCn(protocolStruct.getQnCn()).equals("")) {
                        //QnRtn 8 CN错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR8_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 8).getBytes());
                        return;
                    } else if (protocolStruct.getQnMn().equals("") || !protocolStruct.getQnMn().equals(protocolStruct.getMn())) {
                        //QnRtn 4 MN错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR4_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 4).getBytes());
                        return;
                    } else if (protocolStruct.getQnPassword().equals("")) {
                        //QnRtn 3 PW错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR3_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 3).getBytes());
                        return;
                    } else if (protocolStruct.getQnST().equals("") || !protocolStruct.getQnST().equals("32")) {
                        // 5 ST 错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR5_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 5).getBytes());
                        return;
                    } else if (protocolStruct.getQnFlag().equals("") || !protocolStruct.getQnFlag().equals("5")) {
                        // 6 FLAG 错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR6_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 6).getBytes());
                        return;
                    } else if (!protocolStruct.getQnTime().equals("")) {
                        if (!isDigit(protocolStruct.getQnTime())) {
                            // 7 QN 错误
                            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR7_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 7).getBytes());
                            return;
                        }
                    }
                    new GB2017_V2();
                    if (strCode2017 != null) {
                        //根据污染物编码匹配已经配置的组份
                        String[] list = getNowCfgCompNameCde(strCode2017);
                        for (int i = 0; i < list.length; i++) {
                            if (protocolStruct.getQnCode().equals(list[i])) {
                                protocolStruct.setCompName(strComponent.get(1)[i]);
                                break;
                            }
                        }
                    }
                    if (strCmd2017[COC_CH] != null) {
                        protocolStruct.setPassword(getKeySubString(strCmd2017[COC_CH].getBytes(), "PW=", 6));
                    }
                    if (!protocolStruct.getQnPassword().equals(protocolStruct.getPassword())) {
                        //QnRtn 3 PW错误
                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR3_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 1000, getQnRtnCmd(protocolStruct, 3).getBytes());
                        return;
                    }
                    if (!protocolStruct.getCompName().equals("")) {
                        if (getModePermissions(protocolStruct.getCompName(), "反控") || devSupportCn(protocolStruct.getQnCn()).equals("3026")) {

                            int exeRtn = 1;
                            switch (devSupportCn(protocolStruct.getQnCn())) {
                                case "1011":// 读取时间
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 1000, getQnRtnCmd(protocolStruct, 1).getBytes());
                                    DataStruct dataStruct = new DataStruct();
                                    dataStruct.setType(protocolStruct.getCompName());
                                    String strCmd = getDevSysTime(protocolStruct, dataStruct);
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 1000, strCmd.getBytes());
                                    break;
                               /* case "3017"://读取采样时间周期
                                    break;*/
                                case "3020":// 提取参数状态
                                {
                                    DataStruct dataStruct1 = new DataStruct();
                                    dataStruct1.setType(protocolStruct.getCompName());
                                    String strCmd1 = getParData(rs, protocolStruct, dataStruct1);
                                    if (strCmd1 == null) {
                                        exeRtn = 100;
                                    } else {
                                        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, strCmd1.getBytes());
                                        return;
                                    }
                                }
                                break;
                                // 反控设置
                                case "3011"://校准
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDJZ));
                                    break;
                                case "3012"://测量
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDZY));
                                    break;
                                case "3013"://清洗
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDYBQX));
                                    break;
                                case "3014": //标样2测量
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDBY2CL));
                                    break;
                                case "3022"://停止测量
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());

                                    stopWorking(protocolStruct.getCompName(), false);
                                    saveRunInfo2File("组分[" + protocolStruct.getCompName() + "] " + protocolStruct.getNowProtocol() + "-停止测量");
                                    break;
                                case "3023"://冲洗
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDGLCX));
                                    break;
                                case "3024"://零样测量
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDLYCL));
                                    break;
                                case "3025"://标样1测量
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDBYCL));
                                    break;
                                case "3027"://标1（零点）校准
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDB1));
                                    break;
                                case "3028"://标2（标样）校准
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDB2));
                                    break;
                                case "3029"://初始装液
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDCSZY));
                                    break;
                                case "3030"://零点核查
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDLDHC));
                                    break;
                                case "3031"://跨度核查
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDKDHC));
                                    break;
                                case "3032"://标样核查
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDBYHC));
                                    break;
                                case "3033"://平行样
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDPXY));
                                    break;
                                case "3034"://加标回收
                                    exeRtn = getDoJobExeRtn(port, sCom, protocolStruct, exeRtn, context.getString(R.string.SDJBHS));
                                    break;
                                case "1012":
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());
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

                                        saveOperationLogMsg(protocolStruct.getCompName(), protocolStruct.getNowProtocol() + "-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
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
                                case "3026":// 测量模式   runningMode  meaMode 1：连续模式，2：周期模式，3：定点模式，4：受控模式，5：手动模式
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());
                                    String strMPattern = getKeySubString(rs, "MPattern=", 1);
                                    if (!strMPattern.equals("")) {
                                        int iMPattern = Integer.valueOf(strMPattern);
                                        if (iMPattern < 5 && iMPattern > 0) {
                                            if (!setOnLineModeSetMeaMode(protocolStruct.getCompName(), iMPattern)) {
                                                exeRtn = 3;
                                            }
                                        } else {
                                            exeRtn = 3;
                                        }
                                    } else {
                                        exeRtn = 3;
                                    }
                                    break;
                                case "3021": {
                                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());
                                    DataStruct dataStruct1 = new DataStruct();
                                    dataStruct1.setType(protocolStruct.getCompName());
                                    if (!setParData(rs, protocolStruct, dataStruct1)) {
                                        exeRtn = 3;
                                    }
                                }
                                break;
                            }
                            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getExeRtnCmd(protocolStruct, exeRtn).getBytes());
                            return;
                        } else {
                            // QnRtn 2 请求被拒绝
                            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR2_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 2).getBytes());
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e(protocolStruct.getNowProtocol() + "协议解析异常", e.toString());
                    // QnRtn 100  未知错误
                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR100_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 100).getBytes());
                    return;
                }
            } else {
                /*回复CRC校验错误指令**/
                //QnRtn 9 CRC 错误
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR9_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 9).getBytes());
            }
        }
    }

    @Override
    public void activeProtocol(Communication port, int sCom) {
        try {
            // 测量数据主动上传
            String cmd = new GB2017_V2().getMeaDataValueUpLoading(new DataStruct(), true);
            if (!cmd.equals("")) {
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (sCom == 3 ? "08" : (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")))), port, 1, 500, cmd.getBytes());
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public String testProtocolUpLoading(Communication port, int sCom, DataStruct DataInfo) {
        // 测试界面数据上传
        String strSendMsg = new GB2017_V2().getMeaDataValueUpLoading(DataInfo, false);
        if (!strSendMsg.equals("")) {
            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 2000, strSendMsg.getBytes());
        }
        return strSendMsg;
    }

    // 反控启动流程
    private int getDoJobExeRtn(Communication port, int sCom, ProtocolStruct protocolStruct, int exeRtn, String flowName) {
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : protocolStruct.getNowProtocol() + "QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, getQnRtnCmd(protocolStruct, 1).getBytes());

        if (doFlowing.get(protocolStruct.getCompName()).equals(context.getString(R.string.waiting_for_instructions))) {
            doControlJob(protocolStruct.getCompName(), flowName);
            saveRunInfo2File("组分[" + protocolStruct.getCompName() + "] " + protocolStruct.getNowProtocol() + "-启动流程：" + flowName);
        } else {
            exeRtn = 5;
        }
        return exeRtn;
    }


    /*找配置文件中$$包含的关键字符*/
    public static String[] findKeyString(String strCmd) {

        int iIndex = 0, iIndex2 = 0;
        String[] str = new String[50];
        int num = 0;
        while (true) {
            iIndex = strCmd.indexOf("$", iIndex2);
            if (iIndex == -1) {
                break;
            }
            iIndex2 = strCmd.indexOf("$", iIndex + 1);
            if (iIndex2 == -1) {
                break;
            }
            str[num++] = strCmd.substring(iIndex, ++iIndex2);
        }
        if (num == 0) {
            return null;
        }
        return str;
    }

    // 数据上传封装
    private static String packageMeaDataUpdate(String cmd, DataStruct DataInfo, boolean isHistory) {

        String[] dataStr = new String[strComponent.get(1).length];
        DataStruct[] dataStruct = new DataStruct[strComponent.get(1).length];
        String[] strings = cmd.split("@");
        String[] strHeadKey = findKeyString(strings[0]);
        if (getPublicConfigData("ABSORB_UPDATE").equals("true") || !isHistory) {
            strings[1] += ",$Code$-Vol=$Vol$";
        }
        String[] strDataKey = findKeyString(strings[1]);
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
                    dataStr[i] = strings[1];
                    dataStr[i] = strDataFormat;
                    //对数据区内容进行解析
                    if (strDataKey != null) {
                        for (String iStr : strDataKey) {
                            if (iStr != null) {
                                dataStr[i] = parKeyAnalysis(dataStr[i], iStr, dataStruct[i], null);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                dataStr[i] = "";
                DataInfo.setType(strComponent.get(1)[i]);
                dataStr[i] = strings[1];
                //对数据区内容进行解析
                if (strDataKey != null) {
                    for (String iStr : strDataKey) {
                        if (iStr != null) {
                            dataStr[i] = parKeyAnalysis(dataStr[i], iStr, DataInfo, null);
                        } else {
                            break;
                        }
                    }
                }
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
        if (strHeadKey != null) {
            for (String iStr : strHeadKey) {
                if (iStr != null) {
                    strings[0] = parKeyAnalysis(strings[0], iStr, null, null);
                } else {
                    break;
                }
            }
        }
        cmd = strings[0];

        /*头+数据段+尾*/
        cmd += strData;
        /*将数据内容区的最后一个符号";"去除*/
        cmd = cmd.substring(0, cmd.length() - 1);

        cmd += strings[2];
        cmd = parKeyAnalysis(cmd, "XXXX", null, null);
        cmd = parKeyAnalysis(cmd, "CRC", null, null);
        return cmd;

    }

    // CM3020 读取参数
    private static String getParData(byte[] rs, ProtocolStruct protocolStruct, DataStruct dataStruct) {
        String strCmdHead = "##XXXXQN=$QnTime$;ST=32;CN=3020;PW=123456;MN=$MN$;Flag=4;CP=&&DataTime=$SysTime2$;PolId=$Code$;";
        String strCmdData = "";
        String strCmdEnd = "&&CRC1";
        String strCmd;

        String reStrCmd = new String(rs);
        // 编码和关键字对应位置
        String[] parQnKey = new String[]{"i12001", "i12003", "i12004", "i12005", "i12006", "i13001", "i13002", "i13003", "i13004", "i13005",
                "i13006", "i13007", "i13008", "i13009", "i13010", "i13011", "i13012", "i13013",
                "i13014", "i13015", "i13016", "i13017", "i13018", "i13019", "i13028", "i13029",
                "i13030", "i13031", "i13032", "i13033"};
        String[] parKey = new String[]{"$JobState$", "$AlarmStatus$", "$MeasureMethod$", "$AlarmType$", "$NowRange$", "$NowRangH$", "$MeaPoint$", "$MeasureInterval$", "$HeatTemp$", "$HeatTime$",
                "$CalSucTime$", "$NowRangeB$", "$NowRangeK$", "$MeaLimit$", "$CorrectionFactor$", "$AutoAdjust$", "$AutoAJHour$", "$Span1Value$",
                "$Span1Vol$", "$Span2Value$", "$Span2Vol$", "$DevRange$", "$ColorTemp$", "$ColorTime$", "$FixedTime$", "$HeatCoolTemp$",
                "$ColorCoolTemp$", "$ColorStandTime$", "$NowRangL$", "$AmendFactor$"};

        for (int i = 0; i < parKey.length; i++) {
            if (reStrCmd.contains(parQnKey[i])) {

                if (parQnKey[i].equals("i13004")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.digestionTemper))) {
                        continue;
                    }
                }
                if (parQnKey[i].equals("i13005")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.digestionTimer))) {
                        continue;
                    }
                }
                if (parQnKey[i].equals("i13018")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorTemper))) {
                        continue;
                    }
                }
                if (parQnKey[i].equals("i13019")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorTimer))) {
                        continue;
                    }
                }
                if (parQnKey[i].equals("i13029")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.dispellingCooling)) || getPublicConfigData("LogInName").equals("0")) {
                        continue;
                    }
                }
                if (parQnKey[i].equals("i13030")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorCooling)) || getPublicConfigData("LogInName").equals("0")) {
                        continue;
                    }
                }
                if (parQnKey[i].equals("i13031")) {
                    if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorStand))) {
                        continue;
                    }
                }
                strCmdData += (parQnKey[i] + "-Info=" + parKey[i] + ";");
                strCmdData = parKeyAnalysis(strCmdData, parKey[i], dataStruct, protocolStruct);
            }
        }
        if (strCmdData.equals("")) {
            return null;
        }

        /*将数据内容区的最后一个符号";"去除*/
        strCmdData = strCmdData.substring(0, strCmdData.length() - 1);

        strCmd = strCmdHead + strCmdData + strCmdEnd;

        strCmd = parKeyAnalysis(strCmd, "$QnTime$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Code$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$SysTime2$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$MN$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "XXXX", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "CRC", null, protocolStruct);

        return strCmd;
    }


    // CN3021 设置参数
    private static boolean setParData(byte[] rs, ProtocolStruct protocolStruct, DataStruct dataStruct) {

        String[] parQnKey = new String[]{"i13004-Info", "i13005-Info", "i13018-Info", "i13019-Info", "i13029-Info", "i13030-Info", "i13031-Info"};
        String[] parKey = new String[]{"$SetHeatTemp$", "$SetHeatTime$", "$SetColorTemp$", "$SetColorTime$", "$SetHeatCoolTemp$", "$SetColorCoolTemp$", "$SetColorStandTime$"};
        String reStrCmd = new String(rs);
        String[] strCmd = reStrCmd.split("[;/,]");
        boolean blSetFlag = true;// 参数写入完成标记
        boolean blFindFlag = false;

        for (String item : strCmd) {
            for (int i = 0; i < parQnKey.length; i++) {
                if (item.contains(parQnKey[i])) {
                    blFindFlag = true;
                    int iStart = item.indexOf("=");
                    String sStr = item.substring(iStart + 1);
                    String[] value = sStr.split("[;,&]");

                    if (value[0] != null) {
                        int iPointNum = value[0].indexOf(".");
                        if (iPointNum != -1) {
                            value[0] = value[0].replaceAll("[^\\d]", "");
                            value[0] = value[0].substring(0, iPointNum) + "." + value[0].substring(iPointNum);
                        }
                        if (!setParKeyAnalysis(dataStruct, parKey[i], value[0])) {
                            blSetFlag = false;
                        }
                    }
                }
            }
        }
        if (!blFindFlag) {
            blSetFlag = false;
        }
        return blSetFlag;
    }


    /*读取现场机时间*/
    private static String getDevSysTime(ProtocolStruct protocolStruct, DataStruct dataStruct) {

        String strCmd = "##XXXXQN=$QnTime$;ST=32;CN=1011;PW=123456;MN=$MN$;Flag=4;CP=&&PolId=$Code$;SystemTime=$SysTime2$&&CRC1";

        strCmd = parKeyAnalysis(strCmd, "$QnTime$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$Code$", dataStruct, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$SysTime2$", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "$MN$", null, protocolStruct);


        strCmd = parKeyAnalysis(strCmd, "XXXX", null, protocolStruct);
        strCmd = parKeyAnalysis(strCmd, "CRC", null, protocolStruct);
        return strCmd;
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
                strMn[0] = "123456789101112131415161";
                if (!getPublicConfigData("GB2017V2ORG_ADDR").equals(""))
                    strMn[0] = getPublicConfigData("GB2017V2ORG_ADDR") + getPublicConfigData("GB2017V2DEV_ADDR");
                cmd = cmd.replace(key, strMn[0]);
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
            case "$JobTime$"://测量数据时间
                cmd = getStringMeaDataTime(cmd, key, DataInfo);
                break;
            case "$Code$":
                //GB2017 替换污染物编码
                cmd = cmd.replace(key, getCode(DataInfo.getType(), strCode2017));
                break;
            case "$MeaData$":
                //替换测量值
                cmd = cmd.replace(key, dataFormat2(String.valueOf(DataInfo.getData()), point));
                break;
            case "$Flag$":  // 数据标识
                cmd = cmd.replace(key, getDataTag(DataInfo.getType(), DataInfo));
                break;
            case "$Vol$"://吸光度能量
                cmd = cmd.replace(key, dataFormat2(String.valueOf(DataInfo.getAi()), point));
                break;
            case "$QnTime$":// 指令请求时间
                // 替换请求命令时间
                cmd = cmd.replace(key, protocolStruct.getQnTime());
                break;
            case "$QnMN$":
                cmd = cmd.replace(key, protocolStruct.getQnMn());
                break;
            case "$HeatTemp$"://消解温度
                cmd = cmd.replace(key, dataFormat2(String.valueOf(Float.parseFloat(getHeatTemp(DataInfo.getType()))), 1));
                break;
            case "$HeatTime$"://消解时长
                cmd = cmd.replace(key, iDataFormat(String.valueOf((getHeatTime(DataInfo.getType()))), 0));
                break;
            case "$ColorTemp$"://显色温度
                cmd = cmd.replace(key, dataFormat2(String.valueOf(Float.parseFloat(getColorTemp(DataInfo.getType()))), 1));
                break;
            case "$ColorTime$"://显色时长
                cmd = cmd.replace(key, iDataFormat(String.valueOf((getColorTime(DataInfo.getType()))), 0));
                break;
            case "$HeatCoolTemp$"://消解降温温度
                cmd = cmd.replace(key, dataFormat2(String.valueOf(Float.parseFloat(getHeatCoolTemp(DataInfo.getType()))), 1));
                break;
            case "$ColorCoolTemp$"://显色降温温度
                cmd = cmd.replace(key, dataFormat2(String.valueOf(Float.parseFloat(getColorCoolTemp(DataInfo.getType()))), 1));
                break;
            case "$ColorStandTime$"://显色静置时间
                cmd = cmd.replace(key, iDataFormat(String.valueOf(getColorStandTime(DataInfo.getType())), 0));
                break;

            case "$AlarmStatus$"://报错状态  0 ：无  1：有
                cmd = cmd.replace(key, (workState.get(DataInfo.getType()).equals(context.getString(R.string.normal)) ? "0" : "1"));
                break;
            case "$JobState$":// 工作状态
                cmd = cmd.replace(key, String.valueOf(getJobState(DataInfo)));
                break;
            case "$AlarmType$"://仪器报警信息
                cmd = cmd.replace(key, String.valueOf(getAlarmNum(DataInfo.getType())));
                break;
            case "$NowRange$":
                cmd = cmd.replace(key, String.valueOf(getDevNowRangeNum(DataInfo.getType())));
                break;
            case "$NowRangL$"://当前量程L
                cmd = cmd.replace(key, dataFormat2(String.valueOf(getNowRangeL(DataInfo.getType())), point));
                break;
            case "$NowRangH$"://当前量程H
                cmd = cmd.replace(key, dataFormat2(String.valueOf(getNowRangeH(DataInfo.getType())), point));
                break;
            case "$MeaPoint$"://测量精度
                cmd = cmd.replace(key, String.valueOf(getMeaPoint(DataInfo.getType())));
                break;
            case "$CalSucTime$":// 校准成功时间
                cmd = cmd.replace(key, String.valueOf(getCalSucTime(DataInfo.getType())));
                break;
            case "$DevRange$":// 仪器量程H
                cmd = cmd.replace(key, dataFormat2((getDevRangeH(DataInfo.getType())), point));
                break;
            case "$NowRangeK$"://当前量程K
                cmd = cmd.replace(key, dataFormat2(getNowRangeK(DataInfo.getType()), point));
                break;
            case "$NowRangeB$"://当前量程B
                cmd = cmd.replace(key, dataFormat2(getNowRangeB(DataInfo.getType()), point));
                break;
            case "$NowRangeF$"://当前量程F
                //cmd = cmd.replace(key, dataFormat2(getNowRangeF(DataInfo.getType()), point));
                cmd = cmd.replace(key, dataFormat2("1", point));
                break;
            case "$MeaLimit$"://测量检出限
                cmd = cmd.replace(key, dataFormat2(getMeaLimitHigh(DataInfo.getType()), point));
                break;
            case "$AutoAdjust$":// 自动校准开关
                cmd = cmd.replace(key, getAutoJustStatus(DataInfo.getType()).equals("true") ? "1" : "0");
                break;
            case "$AutoAJHour$"://自动校准设置间隔时间
                cmd = cmd.replace(key, String.valueOf(getAutoJustSettingSpaceHour(DataInfo.getType())));
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
            case "$CorrectionFactor$"://校正因子
                cmd = cmd.replace(key, dataFormat2(getNowRangeExtRangeK(DataInfo.getType()), point));
                break;
            case "$AmendFactor$"://修正因子
                cmd = cmd.replace(key, dataFormat2(getNowRangeExtRangeB(DataInfo.getType()), point));
                break;
            case "$MeasureInterval$"://测量间隔
                String strTime = getContinueMeaSetting(DataInfo.getType());
                cmd = cmd.replace(key, strTime);
                break;
            case "$FixedTime$"://定点时间
                String[] Hs = getConfigData(DataInfo.getType(), "zqclh").split("[,，]");
                String str = "";
                boolean find = false;
                for (int i = 0; i < 24; i++) {
                    for (String h : Hs) {
                        // 从1点开始
                        if ((i + 1) == Integer.valueOf(h)) {
                            find = true;
                            break;
                        }
                    }
                    // 检查 0点有没有设置
                    if (i == 23) {
                        if ((0) == Integer.valueOf(Hs[0])) {
                            str += "1";
                        } else {
                            str += "0";
                        }
                    } else {
                        if (find) {
                            str += "1";
                        } else {
                            str += "0";
                        }
                    }
                    find = false;
                }
                cmd = cmd.replace(key, str);
                break;
            case "$MeasureMethod$"://测量方式
                String method = "";
                switch (getDevMeaMode(DataInfo.getType())) {
                    case 1:
                    case 2:
                    case 3:
                        method = "1";
                        break;
                    case 4:
                    case 5:
                    case 6:
                    default:
                        method = "0";
                        break;
                }
                cmd = cmd.replace(key, method);
                break;
        }
        return cmd;
    }


    /**
     * 设置关键参数
     */
    private static boolean setParKeyAnalysis(DataStruct DataInfo, String key, String value) {

        switch (key) {
            case "$SetHeatTemp$":
                return setHeatTemp(DataInfo.getType(), value);
            case "$SetHeatTime$":
                return setHeatTime(DataInfo.getType(), value);
            case "$SetColorTemp$":
                return setColorTemp(DataInfo.getType(), value);
            case "$SetColorTime$":
                return setColorTime(DataInfo.getType(), value);
            case "$SetHeatCoolTemp$":
                return setHeatCoolTemp(DataInfo.getType(), value);
            case "$SetColorCoolTemp$":
                return setColorCoolTemp(DataInfo.getType(), value);
            case "$SetColorStandTime$":
                return setColorStandTime(DataInfo.getType(), value);
            case "$SetWaitStand$":
                return setWaitStand(DataInfo.getType(), value);
            case "$SetWaterVolume$":
                return setWaterVolume(DataInfo.getType(), value);
            case "$SetOxidantVolume$":
                return setOxidantVolume(DataInfo.getType(), value);
            case "$SetReductantVolume$":
                return setReductantVolume(DataInfo.getType(), value);
            case "$SetReductantCoc$":
                return setReductantCoc(DataInfo.getType(), value);
            case "$SetTitrationVolume$":
                return setTitrationVolume(DataInfo.getType(), value);
        }
        return false;
    }

    // 查找是什么校验算法
    public static int findCrc(String cmd) {
        //天津力合协议算法
        /*if (cmd.contains("CRC1")) {
            return 1;
            //212协议算法
        } else if (cmd.contains("CRC2")) {
            return 2;
        } else {
            return 0;
        }*/
        if (getPublicConfigData("GB2017V2_CRC").equals("0")) {
            return 1;
        } else if (getPublicConfigData("GB2017V2_CRC").equals("1")) {
            return 2;
        } else {
            return 0;
        }
    }

}
