package com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan;

import android.os.Bundle;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtuTable;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuDataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.DBConvert.CloneUtils;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.findFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.getNowSysTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getComponentUnit;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getMeaLimitHigh;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getRunningMode;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getDataStruct;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastHistoryFlowData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat2;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.cfragment.Global.lSiChuanRtuCmdData;
import static com.yzlm.cyl.cfragment.Global.lSiChuanRtuCompRegMap;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.siChuanProtocolStatusParIsChange;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static java.lang.Integer.parseInt;

public class siChuanModbusRtu {
    private static String[] strRtu2Code = new String[30];

    public static void syncListSiChuanModbusRtu() {

        String modbusRtuDir;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                return;
            } else {
                modbusRtuDir = extSdcardPath;
            }
        } else {
            modbusRtuDir = SdcardPath;
        }
        modbusRtuDir += "Csoft" + "/Protocol/SiChuan/";
        String errorPath = modbusRtuDir + "SiChuanRtu" + ".txt";
        File dbFile = new File(errorPath);
        if (dbFile.exists()) {
            String strData = readString(errorPath, "GBK");

            String[] strsData = strData.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");

            for (int i = 0; i < strsData.length; i++) {
                String strCmd = "";
                String strCmdInfo = "";
                ModbusRtuTable rrt = new ModbusRtuTable();
                try {
                    if (strsData[i].contains("//")) {
                        continue;
                    }
                    if (strsData[i].contains("READ") || strsData[i].contains("CONTROL")) {
                        strCmd = strsData[i].split("_")[0];
                        strCmdInfo = strsData[i];
                        for (i += 1; i < strsData.length; i++) {
                            RtuDataStruct rrds = new RtuDataStruct();
                            if (strsData[i].contains("end") || strsData[i].contains("END")) {
                                break;
                            }
                            if (strsData[i].contains("//")) {
                                continue;
                            }
                            String[] strsItem = strsData[i].split("[,，]");
                            switch (strsItem.length) {
                                case 7:
                                    rrds.setUp(strsItem[6]);
                                case 6:
                                    rrds.setDown(strsItem[5]);
                                case 5:
                                    rrds.setRw(strsItem[4]);
                                case 4:
                                    rrds.setDescribe(strsItem[3]);
                                case 3:
                                    rrds.setDataType(strsItem[2]);
                                case 2:
                                    rrds.setDataLen(Integer.parseInt(strsItem[1]));
                                case 1:
                                    rrds.setRegAddr(Integer.parseInt(strsItem[0]));
                                    rrds.setData("0");
                                    break;
                            }
                            rrt.Add(rrds);
                        }
                    }
                    if (rrt.getSize() > 0) {
                        if (strCmd.contains("READ") || strCmd.contains("CONTROL")) {
                            /*常规因子存储*/
                            lSiChuanRtuCmdData.put(strCmd, rrt);
                            lSiChuanRtuCmdData.get(strCmd).setMapInfo(strCmdInfo);
                        }
                    }
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", "协议文件有误 ！");
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
            }
            if (lSiChuanRtuCmdData.size() > 0) {
                addSiChuanRtuRegisterData(null, true);
            }
        } else {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle bundle = new Bundle();
            bundle.putString("alert-err", "协议文件丢失！");
            st.setArguments(bundle);
            st.show(fm, "Dialog_err");
        }
    }

    private static boolean AnalysisSiChuanRtuCodeFile() {
        String fileContent = null;
        fileContent = findFile("Csoft/Protocol/SiChuan/", "SiChuanRtuCode");
        if (fileContent != null) {
            strRtu2Code = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
            return true;
        } else {
            return false;
        }
    }

    /*
     * 增加寄存器数据到全局Map
     */
    public static void addSiChuanRtuRegisterData(String compName, boolean blFisrtOnces) {
        /*常规仪表*/
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                if (compName != null) {
                    if (!item.equals(compName)) {
                        continue;
                    }
                }
                int startRegAddr = parseInt(lSiChuanRtuCmdData.get("READ").getMapInfo().split("_")[1]);
                try {
                    setHistoryData(lSiChuanRtuCmdData, "READ", item, 0, startRegAddr, blFisrtOnces);
                } catch (Exception e) {
                    saveExceptInfo2File("组分[" + item + "]" + "setHistoryData()" + e.toString());
                }
                putRtu2RegMapToCompRegMap(item, 0);
            }
        }
    }

    /*
    将全局的缓存存储至寄存器数据map中
    * ***/
    private static void putRtu2RegMapToCompRegMap(String CompName, int what) {
        /*将解析后的数据放置全局寄存器map中**/
        Map<Integer, RtuDataStruct> RegMap = new HashMap();
        if (what == 0) {
            RegMap = addModbusRegMap(lSiChuanRtuCmdData, RegMap);
        }
        Map<Integer, RtuDataStruct> RegNewMap;
        RegNewMap = (Map<Integer, RtuDataStruct>) CloneUtils.deepClone(RegMap);
        lSiChuanRtuCompRegMap.put(CompName, RegNewMap);
    }

    /**
     * 组份，组份类型（常规还是五参数），开始寄存器地址，单通道总的寄存器数
     */
    private static void setHistoryData(Map<String, ModbusRtuTable> map, String mapKey, String compName, int compFlag, int startReg, boolean blFisrtOnces) {
        int regAddr = startReg;

        String strHisData = "0";

        int point = Integer.parseInt(getConfigData(compName, "YXWS"));
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量
        List<Map> listzy = null, listby2 = null, listlycl = null, listldhc = null, listkdhc = null, listjbhs = null, listpxy = null, listby1 = null, listbyhc = null;
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量,标样核查
        DataStruct[] dataStruct = new DataStruct[1];
        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.parseInt(time[0]);
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        int hour = Integer.parseInt(time[3]);
        int min = Integer.parseInt(time[4]);
        int sec = Integer.parseInt(time[5]);
        if (compFlag == 0) {
            if (!getModePermissions(compName, "历史数据上传")) {
                return;
            }
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                listzy = getLastHistoryFlowData(compName, null);
            } else {
                listzy = getLastHistoryFlowData(compName, context.getString(R.string.ZY));
            }
            listby1 = getLastHistoryFlowData(compName, context.getString(R.string.B1));
            listby2 = getLastHistoryFlowData(compName, context.getString(R.string.B2));
            //做样
            dataStruct[0] = getDataStruct(listzy, 0);
            strHisData = dataFormat2(String.valueOf(Float.valueOf(dataStruct[0].getData())), point);
        }
        for (Integer key : map.get(mapKey).getLModbusRweReg().keySet()) {
            if (map.get(mapKey).getRegObj(key).getDescribe() != null) {
                switch (map.get(mapKey).getRegObj(key).getDescribe()) {
                    case "水样测量值":
                        map.get(mapKey).getRegObj(key).setData(strHisData);
                        break;
                    case "数据标记":
                        map.get(mapKey).getRegObj(key).setData(String.valueOf(getDataFlow(compName, dataStruct[0].getFlow(), Double.parseDouble(strHisData))));
                        break;
                    case "参数代码":
                        AnalysisSiChuanRtuCodeFile();
                        int code = 0;
                        String strCode = getCode(compName, strRtu2Code);
                        if (!strCode.equals("")) {
                            code = Integer.parseInt(strCode);
                        }
                        map.get(mapKey).getRegObj(key).setData(String.valueOf(code));
                        break;
                    case "运行状态":
                        String sRun = String.valueOf(getSiChuanRtuJobStatus(compName));
                        map.get(mapKey).getRegObj(key).setData(sRun);
                        setValueStatus(compName, blFisrtOnces, key, sRun, "1");
                        break;
                    case "报警状态":
                        int iError = 0;
                        if (!workState.get(compName).equals(context.getString(R.string.normal))) {
                            iError = 1;
                        }
                        String sError = String.valueOf(iError);
                        map.get(mapKey).getRegObj(key).setData(sError);
                        setValueStatus(compName, blFisrtOnces, key, sError, "1");
                        break;
                    case "状态和参数反馈":
                        String sStatus = siChuanProtocolStatusParIsChange.get(compName);
                        map.get(mapKey).getRegObj(key).setData(sStatus == null ? "0" : sStatus);
                        break;
                    case "当前值测量吸光度":
                        String sMeaA = "0";
                        if (listzy != null) {
                            if (listzy.size() > 0) {
                                sMeaA = (listzy.get(0).get("A").toString());
                            }
                        }
                        map.get(mapKey).getRegObj(key).setData(sMeaA);
                        setValueStatus(compName, blFisrtOnces, key, sMeaA, "2");
                        break;
                    case "取样时间"://
                        int ihour = (getConfigData(compName, "lxclh")).equals("") ? 0 : Integer.parseInt(getConfigData(compName, "lxclh"));
                        int imin = getConfigData(compName, "lxclm").equals("") ? 0 : Integer.parseInt(getConfigData(compName, "lxclm"));
                        String sJobTime = String.valueOf(ihour * 60 + imin);
                        map.get(mapKey).getRegObj(key).setData(sJobTime);
                        setValueStatus(compName, blFisrtOnces, key, sJobTime, "2");
                        break;
                    case "当前量程低位": {
                        String range = getConfigData(compName, "RANGE");
                        String sRangeL;
                        if (!getConfigData(compName, "LC" + range + "L").equals("")) {
                            sRangeL = (getConfigData(compName, "LC" + range + "L"));
                        } else {
                            sRangeL = "0";
                        }
                        map.get(mapKey).getRegObj(key).setData(sRangeL);
                        setValueStatus(compName, blFisrtOnces, key, sRangeL, "2");
                    }
                    break;
                    case "当前量程高位": {
                        String range = getConfigData(compName, "RANGE");
                        String sRangeH;
                        if (!getConfigData(compName, "LC" + range + "H").equals("")) {
                            sRangeH = (getConfigData(compName, "LC" + range + "H"));
                        } else {
                            sRangeH = "0";
                        }
                        map.get(mapKey).getRegObj(key).setData(sRangeH);
                        setValueStatus(compName, blFisrtOnces, key, sRangeH, "2");
                    }
                    break;

                    case "标样1浓度": {
                        int range = Integer.parseInt(getConfigData(compName, "RANGE"));
                        String sValue = getConfigData(compName, "C" + ((range - 1) * 2));
                        if (sValue.equals("")) {
                            sValue = "0";
                        }
                        map.get(mapKey).getRegObj(key).setData(sValue);
                        setValueStatus(compName, blFisrtOnces, key, sValue, "2");
                    }
                    break;
                    case "标样2浓度": {
                        int range = Integer.parseInt(getConfigData(compName, "RANGE"));
                        String sValue = getConfigData(compName, "C" + ((range - 1) * 2 + 1));
                        if (sValue.equals("")) {
                            sValue = "0";
                        }
                        map.get(mapKey).getRegObj(key).setData(sValue);
                        setValueStatus(compName, blFisrtOnces, key, sValue, "2");
                    }
                    break;
                    case "标样3浓度": {
                        map.get(mapKey).getRegObj(key).setData("0");
//                        AddError(compName, 638, 运维_信息);
                    }
                    break;
                    case "工作曲线K":
                        String sWorkK = String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K"));
                        map.get(mapKey).getRegObj(key).setData(sWorkK);
                        setValueStatus(compName, blFisrtOnces, key, sWorkK, "2");
                        break;
                    case "工作曲线B":
                        String sWorkB = String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B"));
                        map.get(mapKey).getRegObj(key).setData(sWorkB);
                        setValueStatus(compName, blFisrtOnces, key, sWorkB, "2");
                        break;
                    case "修正斜率K":
                        String sExtK = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "K");
                        map.get(mapKey).getRegObj(key).setData(sExtK);
                        setValueStatus(compName, blFisrtOnces, key, sExtK, "2");
                        break;
                    case "修正截距B":
                        String sExtB = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "B");
                        map.get(mapKey).getRegObj(key).setData(sExtB);
                        setValueStatus(compName, blFisrtOnces, key, sExtB, "2");
                        break;
                    case "消解温度":
                        String sHeatT = "0";
                        if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTemper))) {
                            sHeatT = getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd");
                        }
                        map.get(mapKey).getRegObj(key).setData(sHeatT);
                        setValueStatus(compName, blFisrtOnces, key, sHeatT, "2");
                        break;
                    case "显色时长":
                        String sColorTime = "0";
                        if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTimer))) {
                            sColorTime = (getConfigData(compName, "xssc").equals("") ? "0" : getConfigData(compName, "xssc"));
                        }
                        map.get(mapKey).getRegObj(key).setData(sColorTime);
                        setValueStatus(compName, blFisrtOnces, key, sColorTime, "2");
                        break;
                    case "显色温度":
                        String sColorT = "0";
                        if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTemper))) {
                            sColorT = (getConfigData(compName, "xswd").equals("") ? "0" : getConfigData(compName, "xswd"));
                        }
                        map.get(mapKey).getRegObj(key).setData(sColorT);
                        setValueStatus(compName, blFisrtOnces, key, sColorT, "2");
                        break;
                    case "消解时间":
                        String sHeatTime = "0";
                        if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTimer))) {
                            sHeatTime = (getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc"));
                        }
                        map.get(mapKey).getRegObj(key).setData(sHeatTime);
                        setValueStatus(compName, blFisrtOnces, key, sHeatTime, "2");
                        break;
                    case "量程下限":
                        String sDevRangeL = "0";
                        if (!getConfigData(compName, "YBLCL").equals("")) {
                            sDevRangeL = (getConfigData(compName, "YBLCL"));
                        }
                        map.get(mapKey).getRegObj(key).setData(sDevRangeL);
                        setValueStatus(compName, blFisrtOnces, key, sDevRangeL, "2");
                        break;
                    case "量程上限":
                        String sDevRangeH = "0";
                        if (!getConfigData(compName, "YBLCH").equals("")) {
                            sDevRangeH = (getConfigData(compName, "YBLCH"));
                        }
                        map.get(mapKey).getRegObj(key).setData(sDevRangeH);
                        setValueStatus(compName, blFisrtOnces, key, sDevRangeH, "2");
                        break;
                    case "标样1吸光度":
                        String sSpan1A = "0";
                        if (listby1 != null && listby1.size() > 0) {
                            sSpan1A = (listby1.get(0).get("A").toString());
                        }
                        map.get(mapKey).getRegObj(key).setData(sSpan1A);
                        setValueStatus(compName, blFisrtOnces, key, sSpan1A, "2");
                        break;
                    case "标样2吸光度":
                        String sSpan2A = "0";
                        if (listby2 != null && listby2.size() > 0) {
                            sSpan2A = (listby2.get(0).get("A").toString());
                        }
                        map.get(mapKey).getRegObj(key).setData(sSpan2A);
                        setValueStatus(compName, blFisrtOnces, key, sSpan2A, "2");
                        break;
                    case "检出限值":
                        String value = (ConvertUnit("mg/L", getComponentUnit(compName), getMeaLimitHigh(compName), 6));
                        map.get(mapKey).getRegObj(key).setData(value);
                        break;
                }
            }
        }
    }

    /**
     * 设置数据，及更新状态
     *
     * @param compName     组分
     * @param blFisrtOnces 是否第一次加载数据
     * @param key          数据key
     * @param sValue       数据值
     * @param sType        状态标识
     */
    private static void setValueStatus(String compName, boolean blFisrtOnces, Integer key, String sValue, String sType) {
        if (!blFisrtOnces) {
            if (!lSiChuanRtuCompRegMap.get(compName).get(key).getData().equals(sValue)) {
                setStatusParChange(compName, sType);
            }
        }
    }

    /*
    获取 工作状态
    * **/
    private static int getSiChuanRtuJobStatus(String compName) {
        int iStatus = getJobStatus(compName);
        int iMode = getRunningMode(compName);
        if (iMode == 3) {
            // 维护状态
            if (iStatus == 0) {
                return 6;
            }
        } else if (iMode == 2) {
            return 12;//停运
        }
        switch (iStatus) {
            case 2:
                return 13;
            case 19:
            case 34:
                return 2;
            case 5:
                return 3;
            case 28:
            case 29:
                return 4;
            case 10:
                return 5;
            case 30:
                return 7;
            case 6:
                return 8;
            case 32:
                return 9;
            case 35:
                return 10;
            case 33:
                return 11;
            case 37:
                return 14;
            case 36:
                return 15;
            case 3:
                return 16;
            case 4:
                return 17;
            case 7:
                return 18;
            default:
                return iStatus;
        }

    }

    private static int getModbusRtu2MeaMode(String compName) {
        switch (getConfigData(compName, "runningMode")) {
            case "2":
                return 0;
            case "1":
            case "3":
                // 维护情况下，4 为手动测量，返回5
                if (getConfigData(compName, "meaMode").equals("4") || getConfigData(compName, "meaMode").equals("5")) {
                    return 0;
                }
                break;
        }
        return 1;
    }


    /**
     * @param flow 流程名称
     * @return id号
     */
    private static int getDataFlow(String compName, String flow, double fData) {
        int iNum = 0;
        if (flow == null) {
            return 16;
        }
        /*超标数据*/
        if (getPublicConfigData("SYS_RELAYCONF").equals("2")) {
            if (flow.equals(context.getString(R.string.ZY))) {
                double al = Double.parseDouble(getConfigData(compName, "ALARM_L"));
                double ah = Double.parseDouble(getConfigData(compName, "ALARM_H"));
                if (fData > ah || fData < al) {
                    iNum = 5;
                    return iNum;
                }
            }
        }
        if (flow.equals(context.getString(R.string.ZY))) {
            iNum = 0;
        } else if (flow.equals(context.getString(R.string.LYCL))) {
            iNum = 1;
        } else if (flow.equals(context.getString(R.string.BY2CL))) {
            iNum = 2;
        } else if (flow.equals(context.getString(R.string.B2))) {
            iNum = 3;
        } else if (flow.equals(context.getString(R.string.PXY))) {
            iNum = 4;
        } else if (flow.equals(context.getString(R.string.JBHS))) {
            iNum = 7;
        } else if (flow.equals(context.getString(R.string.BYCL))) {
            iNum = 8;
        } else if (flow.equals(context.getString(R.string.B1))) {
            iNum = 9;
        } else if (flow.equals(context.getString(R.string.BYHC))) {
            iNum = 10;
        } else if (flow.equals(context.getString(R.string.LDHC))) {
            iNum = 11;
        } else if (flow.equals(context.getString(R.string.JYWDX))) {
            iNum = 12;
        } else if (flow.equals(context.getString(R.string.GYWDX))) {
            iNum = 13;
        } else if (flow.equals(context.getString(R.string.KDHC))) {
            iNum = 14;
        } else if (flow.equals(context.getString(R.string.XSJZ))) {
            iNum = 15;
        } else {
            iNum = 16;
        }
        return iNum;
    }

    /*
     * 将解析后的数据放至全局寄存器map中
     **/
    private static Map<Integer, RtuDataStruct> addModbusRegMap(Map<String, ModbusRtuTable> srcMap, Map<Integer, RtuDataStruct> map) {
        for (Map.Entry<String, ModbusRtuTable> entry : srcMap.entrySet()) {
            String key = entry.getKey();
            ModbusRtuTable value = entry.getValue();
            for (Map.Entry<Integer, RtuDataStruct> entry1 : value.getLModbusRweReg().entrySet()) {
                Integer keyReg = entry1.getKey();
                RtuDataStruct valueReg = entry1.getValue();
                map.put(keyReg, valueReg);
            }
        }
        return map;
    }


    /**
     * @param compName  组分
     * @param sSetValue 设置变化参数类型    0：无变化，1：运行状态发生变化，2：工作参数发生变化，3：运行状态和工作参数都发生变化
     */
    private static void setStatusParChange(String compName, String sSetValue) {

        String sValue = siChuanProtocolStatusParIsChange.get(compName);
        if (sValue != null) {
            switch (sValue) {
                case "1":
                    // 原来 运行状态变化 增加工作参数变化，
                    if (sSetValue.equals("2")) {
                        sSetValue = "3";
                    }
                    break;
                case "2":
                    // 原来 工作参数变化 增加 运行状态变化，
                    if (sSetValue.equals("1")) {
                        sSetValue = "3";
                    }
                    break;
                case "3":
                    // 已经在 工作参数变化  运行状态变化，则不改变
                    sSetValue = "3";
                default:
                    break;
            }
        }
        siChuanProtocolStatusParIsChange.put(compName, sSetValue);
    }


    public static void clearStatusParChange(String compName, String sType) {
        String sNowStatus = siChuanProtocolStatusParIsChange.get(compName);
        if (sNowStatus != null) {
            if (sType.equals("1")) {
                switch (sNowStatus) {
                    case "1":
                        siChuanProtocolStatusParIsChange.put(compName, "0");
                        break;
                    case "2":
                        break;
                    case "3":
                        siChuanProtocolStatusParIsChange.put(compName, "2");
                        break;
                }
            } else {
                switch (sNowStatus) {
                    case "1":
                        break;
                    case "2":
                        siChuanProtocolStatusParIsChange.put(compName, "0");
                        break;
                    case "3":
                        siChuanProtocolStatusParIsChange.put(compName, "1");
                        break;
                }
            }
        }
    }

}
