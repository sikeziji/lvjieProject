package com.yzlm.cyl.cfragment.Communication.CommProtocol.HeNan;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.Protocol;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolStruct;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCrc16;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataCmd;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.iDataFormat;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.replaceTempData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCmdFileGB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.AnalysisCodeFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017_V2.findKeyString;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorCoolTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorStandTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatCoolTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLastHistory;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeB;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowRangeK;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStringMeaDataTime;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat2;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.TCP1;
import static com.yzlm.cyl.cfragment.Global.TCP2;
import static com.yzlm.cyl.cfragment.Global.TCP3;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/**
 * Created by zwj on 2017/5/12.
 */

public class HeNan extends Protocol {
    private static String[] strCodeHeNan = new String[30];
    private static String[] strCmdHeNan = new String[40];
    final static int COC_CH = 1;               /*1测量值 重要参数上传*/

    public HeNan() {
        if (strCodeHeNan[0] == null) {
            strCodeHeNan = AnalysisCodeFile("Csoft/Protocol/HeNan/", "CodeHeNan");
        }
        if (strCmdHeNan[1] == null) {
            strCmdHeNan = AnalysisCmdFileGB("Csoft/Protocol/HeNan/", "HeNan");
        }
    }

    @Override
    public String getMeaDataValueUpLoading(DataStruct DataInfo, boolean isHistory) {
        if (strCmdHeNan[COC_CH] == null) {
            strCmdHeNan = AnalysisCmdFileGB("Csoft/Protocol/HeNan/", "HeNan");
            if (strCmdHeNan[COC_CH] == null) {
                return "";
            }
        }
        return packageMeaDataUpdate(strCmdHeNan[COC_CH], DataInfo, isHistory);
    }

    // 数据上传封装
    private static String packageMeaDataUpdate(String cmd, DataStruct DataInfo, boolean isHistory) {
        String flowName;
        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
            flowName = null;
        } else {
            flowName = context.getResources().getString(R.string.ZY);
        }

        return getHeNanUpdateCmdString(cmd, DataInfo, isHistory, flowName);


    }

    private static String getHeNanUpdateCmdString(String cmd, DataStruct DataInfo, boolean isHistory, String flowName) {
        DataStruct dataStruct;
        String[] strCmdKey = findKeyString(cmd);
        if (isHistory) {
            dataStruct = getLastHistory(DataInfo.getType(), flowName);
        } else {
            dataStruct = DataInfo;
        }
        if ((dataStruct.getType() != null) && ((getModePermissions(DataInfo.getType(), "历史数据上传")))) {

            if (strCmdKey != null) {
                for (String iStr : strCmdKey) {
                    if (iStr != null) {
                        if (iStr.equals("$HeatTemp$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.digestionTemper))) {
                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        if (iStr.equals("$HeatTime$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.digestionTimer))) {
                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        if (iStr.equals("$ColorTemp$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorTemper))) {

                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        if (iStr.equals("$ColorTime$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorTimer))) {
                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        if (iStr.equals("$HeatCoolTemp$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.dispellingCooling)) || getPublicConfigData("LogInName").equals("0")) {
                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        if (iStr.equals("$ColorCoolTemp$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorCooling)) || getPublicConfigData("LogInName").equals("0")) {
                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        if (iStr.equals("$ColorStandTime$")) {
                            if (!getSetWinParShow(dataStruct.getType(), context.getResources().getString(R.string.colorStand))) {
                                cmd = getRemoveUnCfgParCmd(cmd, iStr);
                                continue;
                            }
                        }
                        cmd = parKeyAnalysis(cmd, iStr, dataStruct, null);
                    } else {
                        break;
                    }
                }
            }
        }
        cmd = parKeyAnalysis(cmd, "XXXX", null, null);
        cmd = parKeyAnalysis(cmd, "CRC", null, null);
        return cmd;
    }

    @NonNull
    private static String getRemoveUnCfgParCmd(String cmd, String iStr) {
        String[] str = cmd.split("[,，;；]");
        for (String s : str) {
            if (s.contains(iStr)) {
                int sStart = cmd.indexOf(s);
                cmd = cmd.replace(cmd.substring(sStart - 1, sStart) + s, "");
                break;
            }
        }
        return cmd;
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
                strMn[0] = "12345678901111";
                if (!getPublicConfigData("HE_NAN_ORG_ADDR").equals(""))
                    strMn[0] = getPublicConfigData("HE_NAN_ORG_ADDR") + getPublicConfigData("HE_NAN_DEV_ADDR");
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
            case "$SysTime$":/*系统时间包含毫秒*/ {
                cmd = cmd.replace(key, getStrNowSysTemTime());
            }
            break;
            case "$JobTime$"://测量数据时间
                cmd = getStringMeaDataTime(cmd, key, DataInfo);
                break;
            case "$Code$":
                //GB2017 替换污染物编码
                cmd = cmd.replace(key, getCode(DataInfo.getType(), strCodeHeNan));
                break;
            case "$MeaData$":
                //替换测量值
                cmd = cmd.replace(key, dataFormat2(String.valueOf(DataInfo.getData()), point));
                break;
            case "$NowRangeK$"://当前量程K
                cmd = cmd.replace(key, dataFormat2(getNowRangeK(DataInfo.getType()), point));
                break;
            case "$NowRangeB$"://当前量程B
                cmd = cmd.replace(key, dataFormat2(getNowRangeB(DataInfo.getType()), point));
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
        }
        return cmd;
    }

    @Override
    public void ParsingProtocolPortData(Communication port, int sCom, byte[] rs) {

    }

    // 测量数据主动上传
    @Override
    public void activeProtocol(Communication port, int sCom) {
        try {
            for (int i = 0; i < strComponent.get(1).length; i++) {

                DataStruct DataInfo = new DataStruct();
                DataInfo.setType(strComponent.get(1)[i]);

                String cmd = new HeNan().getMeaDataValueUpLoading(DataInfo, true);
                if (!cmd.equals("")) {
                    int iSleep;
                    if (port == TCP1 || port == TCP2 || port == TCP3) {
                        iSleep = 3000;
                    } else {
                        iSleep = 500;
                    }

                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "河南协议上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, iSleep, cmd.getBytes());

                }
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public String testProtocolUpLoading(Communication port, int sCom, DataStruct DataInfo) {
        // 测试界面数据上传
        String strSendMsg = new HeNan().getMeaDataValueUpLoading(DataInfo, false);
        if (!strSendMsg.equals("")) {
            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "河南协议上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, strSendMsg.getBytes());
        }
        return strSendMsg;
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
        if(getPublicConfigData("HE_NAN_CRC").equals("0")){
            return 1;
        }else if (getPublicConfigData("HE_NAN_CRC").equals("1")) {
            return 2;
        }else {
            return 0;
        }
    }

}
