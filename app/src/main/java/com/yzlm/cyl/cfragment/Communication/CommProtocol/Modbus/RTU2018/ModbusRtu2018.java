package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018;

import android.os.Bundle;
import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuDataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParCfg;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParTable;
import com.yzlm.cyl.cfragment.DBConvert.CloneUtils;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.R;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.findFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoCleanStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoCleanTimeSetting;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustSettingDay;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustSettingHour;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getAutoJustStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getComponentUnit;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getComponentUnitCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getContinueMeaSetting;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getCycMeaSetting;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevFaultStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDigestionTubeTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getFlowActionCodeNum;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLogCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getMeaLimitHigh;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNoLiquidErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNowWorkStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getOtherErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getRunningMode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getSpan1Value;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getSpan2Value;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getDataStruct;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastHistoryFlowData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getMeaModeSpinnerSelect;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.calSampleTheReagent;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.minimumReagentBottle;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getNumbers;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateSec;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateYear;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.allAlarmInfo;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.cfragment.Global.lMeaPar;
import static com.yzlm.cyl.cfragment.Global.lRtu2018CmdData;
import static com.yzlm.cyl.cfragment.Global.lRtu2018CompRegMap;
import static com.yzlm.cyl.cfragment.Global.lRtu2018FiveParCmdData;
import static com.yzlm.cyl.cfragment.Global.lRtu2018SysCmdData;
import static com.yzlm.cyl.cfragment.Global.lsurPlus;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.AppUtil.getVersionName;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static java.lang.Integer.parseInt;

/**
 * Created by zwj on 2017/12/19.
 */

@SuppressWarnings("unchecked")
public class ModbusRtu2018 {

    private static String[] strRtuCode2018 = new String[30];

    public static void syncListModbusRtu2018() {

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
        modbusRtuDir += "Csoft" + "/Protocol/Modbus/";
        String errorPath = modbusRtuDir + "Rtu2018" + ".txt";
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
                    if (strsData[i].contains("HISDATA") || strsData[i].contains("STATUS")
                            || strsData[i].contains("KEYPAR") || strsData[i].contains("CONTROL")
                            || strsData[i].contains("FIVEDATA") || strsData[i].contains("FIVEPAR")
                            || strsData[i].contains("BACKBOARDSYSCONS")) {
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
//                            System.out.println("打印 ： " + Arrays.toString(strsItem));
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
                                    rrds.setDataLen(Integer.valueOf(strsItem[1]));
                                case 1:
                                    rrds.setRegAddr(Integer.valueOf(strsItem[0]));
                                    rrds.setData("0");
                                    break;
                            }
                            rrt.Add(rrds);
                        }
                    }
                    if (rrt.getSize() > 0) {

//                        System.out.println(rrt.toString());
//                        System.out.println("strCmd = " + strCmd);
//                        System.out.println("strCmdInfo = " + strCmdInfo);
//

                        if (strCmd.contains("HISDATA") || strCmd.contains("STATUS")
                                || strCmd.contains("KEYPAR") || strCmd.contains("CONTROL")) {
                            /*常规因子存储*/
                            lRtu2018CmdData.put(strCmd, rrt);
                            lRtu2018CmdData.get(strCmd).setMapInfo(strCmdInfo);
                        } else if (strCmd.contains("FIVEDATA")) {
                            /*五参数因子*/
                            lRtu2018FiveParCmdData.put(strCmd, rrt);
                            lRtu2018FiveParCmdData.get(strCmd).setMapInfo(strCmdInfo);
                        } else if (strCmd.contains("BACKBOARDSYSCONS")) {
                            lRtu2018SysCmdData.put(strCmd, rrt);
                            lRtu2018SysCmdData.get(strCmd).setMapInfo(strCmdInfo);
                        }
                    }
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", "ModbusRtu2018 文件有误 ！");
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
            }
            if (lRtu2018CmdData.size() > 0 || lRtu2018FiveParCmdData.size() > 0 || lRtu2018SysCmdData.size() > 0) {
                addRegisterData();
            }
        } else {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle bundle = new Bundle();
            bundle.putString("alert-err", " MudbusRtu2018文件丢失！");
            st.setArguments(bundle);
            st.show(fm, "Dialog_err");
        }
    }

    private static boolean AnalysisRtuCode2018File() {
        String fileContent = null;
        fileContent = findFile("Csoft/Protocol/Modbus/", "RtuCode2018");
        if (fileContent != null) {
            strRtuCode2018 = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
            return true;
        } else {
            return false;
        }
    }

    private static void setRegDateYearMonth(Map<String, ModbusRtuTable> map, String regStrInfo, int regAddr, int year, int month) {
        int b = new java.math.BigInteger(String.valueOf(year - 2000), 16).intValue();
        int cd = new java.math.BigInteger(String.valueOf(month & 0xff), 16).intValue();
        short bcd = (short) (b * 256 + cd);
        map.get(regStrInfo).getRegObj(regAddr).setData(String.valueOf(bcd));
    }

    private static void setRegDateDayHour(Map<String, ModbusRtuTable> map, String regStrInfo, int regAddr, int day, int hour) {
        int b = new java.math.BigInteger(String.valueOf(day), 16).intValue();
        int cd = new java.math.BigInteger(String.valueOf(hour & 0xff), 16).intValue();
        short bcd = (short) (((b) << 8) | cd);
        map.get(regStrInfo).getRegObj(regAddr).setData(String.valueOf(bcd));
    }

    private static void setRegDateMinSec(Map<String, ModbusRtuTable> map, String regStrInfo, int regAddr, int min, int sec) {
        int b = new java.math.BigInteger(String.valueOf(min), 16).intValue();
        int cd = new java.math.BigInteger(String.valueOf(sec & 0xff), 16).intValue();
        short bcd = (short) (((b) << 8) | cd);
        map.get(regStrInfo).getRegObj(regAddr).setData(String.valueOf(bcd));
    }


    /*日志状态***/
    private static short getDevNotesStatus(String workStatus, int flowActionNum) {

        int aCode = getNowWorkStatus(workStatus);
        int bCode = getFlowActionCodeNum(flowActionNum);
        return (short) (aCode & 0xff << 8 + bCode);
    }

    /**
     * 数据标识  compFlag =0 常规组份， 1 五参数组份
     */
    private static String getDataFlag(String compName, List<Map> list, int compFlag) {

        String ret;
        {
            if (!workState.get(compName).equals(context.getString(R.string.normal))) {
                try {
                    int getErrorNum = Integer.parseInt(workState.get(compName).replace(" ", "").split("-")[0]);
                    //超当前量程报警，数据标识和超总量呈一样为T
                    if (getErrorNum >= 570 && getErrorNum <= 572) {
                        return "T";
                    }
                    switch (getNoLiquidErrorStatus(getErrorNum)) {
                        case 1:
                            return "lw";
                        case 2:
                            return "lp";
                        case 3:
                            return "ls";
                        case 4:
                            return "lr";
                        default:
                            break;
                    }
                } catch (Exception e) {
                }
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
                } else if (flow.equals(context.getString(R.string.JBHS))) {
                    ret = "ra";
                } else if (flow.equals(context.getString(R.string.PXY))) {
                    ret = "pt";
                } else {
                    ret = "N";
                }
                return ret;
            } else {
                return "N";
            }
        }
    }


    /**
     * 组份，组份类型（常规还是五参数），开始寄存器地址，单通道总的寄存器数
     */
    private static void setHistoryData(Map<String, ModbusRtuTable> map, String mapKey, String compName, int compFlag, int startReg, int regLen) {
        int regAddr = startReg;

        String strHisData = "0";
        String strBY2CKND = "0";
        String strBY1CKND = "0";
        int point = Integer.parseInt(getConfigData(compName, "YXWS"));
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量
        List<Map> listzy = null, listby2 = null, listlycl = null, listldhc = null, listkdhc = null, listjbhs = null, listpxy = null, listby1 = null, listbyhc = null;
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量,标样核查
        DataStruct[] dataStruct = new DataStruct[9];

        if (compFlag == 0) {
            if (!getModePermissions(compName, "历史数据上传")) {
                return;
            }
            listzy = getLastHistoryFlowData(compName, context.getString(R.string.ZY));
            listby2 = getLastHistoryFlowData(compName, context.getString(R.string.BY2CL));
            listlycl = getLastHistoryFlowData(compName, context.getString(R.string.LYCL));
            listldhc = getLastHistoryFlowData(compName, context.getString(R.string.LDHC));
            listkdhc = getLastHistoryFlowData(compName, context.getString(R.string.KDHC));
            listjbhs = getLastHistoryFlowData(compName, context.getString(R.string.JBHS));
            listpxy = getLastHistoryFlowData(compName, context.getString(R.string.PXY));
            listby1 = getLastHistoryFlowData(compName, context.getString(R.string.BYCL));
            listbyhc = getLastHistoryFlowData(compName, context.getString(R.string.BYHC));
            //做样
            dataStruct[0] = getDataStruct(listzy, 0);

            // 标样2参考值
            strBY2CKND = getSpan2Value(compName);
            if (strBY2CKND == null) {
                strBY2CKND = "0";
            }
            // 标样1参考值
            strBY1CKND = getSpan1Value(compName);
            if (strBY1CKND == null) {
                strBY1CKND = "0";
            }

            // 标样2测量
            dataStruct[1] = getDataStruct(listby2, 0);
            // 零样测量
            dataStruct[2] = getDataStruct(listlycl, 0);
            // 零点核查
            dataStruct[3] = getDataStruct(listldhc, 0);
            // 跨度核查
            dataStruct[4] = getDataStruct(listkdhc, 0);
            // 加标回收
            dataStruct[5] = getDataStruct(listjbhs, 0);
            // 平行样
            dataStruct[6] = getDataStruct(listpxy, 0);
            // 标1测量
            dataStruct[7] = getDataStruct(listby1, 0);
            // 标样核查
            dataStruct[8] = getDataStruct(listbyhc, 0);
        } else {

        }
        for (; regAddr < (startReg + regLen); regAddr += map.get(mapKey).getRegObj(regAddr).getDataLen()) {
            if (map.get(mapKey).getRegObj(regAddr) != null) {
                if (map.get(mapKey).getRegObj(regAddr).getDescribe() != null)
                    switch (map.get(mapKey).getRegObj(regAddr).getDescribe()) {
                        case "因子编码":
                            AnalysisRtuCode2018File();
                            int code = 0;
                            String strCode = getCode(compName, strRtuCode2018);
                            if (!strCode.equals("")) {
                                if (strCode.contains("w")) {
                                    code = Integer.parseInt(strCode.split("w")[1]);
                                } else {
                                    code = Integer.parseInt(strCode);
                                }
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(code));
                            break;
                        case "单位":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getComponentUnitCode(compName)));
                            break;
                        case "标样2参考值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), strBY2CKND, point)));
                            break;
                        case "水样数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[0].getYear(), dataStruct[0].getMonth());
                            break;
                        case "水样数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[0].getDay(), dataStruct[0].getHour());
                            break;
                        case "水样数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[0].getMin(), 0);
                            break;
                        case "水样实测值":
                            strHisData = (String.valueOf(Float.valueOf(dataStruct[0].getData())));
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), strHisData, point)));
                            break;
                        case "水样数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listzy, compFlag)));
                            break;
                        case "标2测量数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[1].getYear(), dataStruct[1].getMonth());
                            break;
                        case "标2测量数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[1].getDay(), dataStruct[1].getHour());
                            break;
                        case "标2测量数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[1].getMin(), 0);
                            break;
                        case "标2测量实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[1].getData()))), point)));
                            break;
                        case "标2测量数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listby2, compFlag)));
                            break;
                        case "空白测量数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[2].getYear(), dataStruct[2].getMonth());
                            break;
                        case "空白测量数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[2].getDay(), dataStruct[2].getHour());
                            break;
                        case "空白测量数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[2].getMin(), 0);
                            break;
                        case "空白测量实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[2].getData()))), point)));
                            break;
                        case "空白测量数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listlycl, compFlag)));
                            break;
                        case "零点核查数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[3].getYear(), dataStruct[3].getMonth());
                            break;
                        case "零点核查数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[3].getDay(), dataStruct[3].getHour());
                            break;
                        case "零点核查数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[3].getMin(), 0);
                            break;
                        case "零点核查实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[3].getData()))), point)));
                            break;
                        case "零点核查数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listldhc, compFlag)));
                            break;
                        case "跨度核查数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[4].getYear(), dataStruct[4].getMonth());
                            break;
                        case "跨度核查数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[4].getDay(), dataStruct[4].getHour());
                            break;
                        case "跨度核查数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[4].getMin(), 0);
                            break;
                        case "跨度核查实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[4].getData()))), point)));
                            break;
                        case "跨度核查数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listkdhc, compFlag)));
                            break;
                        case "加标回收数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[5].getYear(), dataStruct[5].getMonth());
                            break;
                        case "加标回收数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[5].getDay(), dataStruct[5].getHour());
                            break;
                        case "加标回收数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[5].getMin(), 0);
                            break;
                        case "加标回收实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[5].getData()))), point)));
                            break;
                        case "加标回收数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listjbhs, compFlag)));
                            break;
                        case "平行样数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[6].getYear(), dataStruct[6].getMonth());
                            break;
                        case "平行样数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[6].getDay(), dataStruct[6].getHour());
                            break;
                        case "平行样数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[6].getMin(), 0);
                            break;
                        case "平行样实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[6].getData()))), point)));
                            break;
                        case "平行样数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listpxy, compFlag)));
                            break;
                        case "标样1参考值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), strBY1CKND, point)));
                            break;
                        case "标1测量数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[7].getYear(), dataStruct[7].getMonth());
                            break;
                        case "标1测量数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[7].getDay(), dataStruct[7].getHour());
                            break;
                        case "标1测量数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[7].getMin(), 0);
                            break;
                        case "标1测量实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[7].getData()))), point)));
                            break;
                        case "标1测量数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listby1, compFlag)));
                            break;
                        case "标样核查数据时间_年月":
                            setRegDateYearMonth(map, mapKey, regAddr, dataStruct[8].getYear(), dataStruct[8].getMonth());
                            break;
                        case "标样核查数据时间_日时":
                            setRegDateDayHour(map, mapKey, regAddr, dataStruct[8].getDay(), dataStruct[8].getHour());
                            break;
                        case "标样核查数据时间_分秒":
                            setRegDateMinSec(map, mapKey, regAddr, dataStruct[8].getMin(), 0);
                            break;
                        case "标样核查实测值":
                            map.get(mapKey).getRegObj(regAddr).setData((ConvertUnit("mg/L", getComponentUnit(compName), (String.valueOf(Float.valueOf(dataStruct[8].getData()))), point)));
                            break;
                        case "标样核查数据标识":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getDataFlag(compName, listbyhc, compFlag)));
                            break;
                    }
            } else {
                break;
            }
        }
    }


    /*
     * 设置常规因子通道数据
     */
    private static void setHistoryData(String compName) {
        int startRegAddr = parseInt(lRtu2018CmdData.get("HISDATA").getMapInfo().split("_")[1]);
        int regLen = parseInt(lRtu2018CmdData.get("HISDATA").getMapInfo().split("_")[2]);
        try {
            setHistoryData(lRtu2018CmdData, "HISDATA", compName, 0, startRegAddr, regLen);
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "]" + "setHistoryData()" + e.toString());
        }
    }


    /*
     * 获取工作状态相关
     */
    private static void setStatusData(String compName) {
        String keyString = "STATUS";
        if (!lRtu2018CmdData.containsKey(keyString)) {
            return;
        }
        int startReg = parseInt(lRtu2018CmdData.get(keyString).getMapInfo().split("_")[1]);
        int regLen = parseInt(lRtu2018CmdData.get(keyString).getMapInfo().split("_")[2]);
        int regAddr = startReg;
        ErrorLog mError = new ErrorLog(context);
        // 读取运行日志界面内容
        ErrorLog.msgType[] types = {ErrorLog.msgType.运行_信息, ErrorLog.msgType.报错_信息};
        List<Map> list = mError.select(compName, "", "", types, 0);
        String logNum = "";
        if (list.size() > 0) {
            logNum = list.get(0).get("errNum").toString();
        }
        for (; regAddr < (startReg + regLen); regAddr += lRtu2018CmdData.get(keyString).getRegObj(regAddr).getDataLen()) {
            try {
                if (lRtu2018CmdData.get(keyString).getRegObj(regAddr).getDescribe() != null)
                    switch (lRtu2018CmdData.get(keyString).getRegObj(regAddr).getDescribe()) {
                        case "系统时间_年月":
                            setRegDateYearMonth(lRtu2018CmdData, keyString, regAddr, getSystemDateYear(), getSystemDateMonth());
                            break;
                        case "系统时间_日时":
                            setRegDateDayHour(lRtu2018CmdData, keyString, regAddr, getSystemDateDay(), getSystemDateHour());
                            break;
                        case "系统时间_分秒":
                            setRegDateMinSec(lRtu2018CmdData, keyString, regAddr, getSystemDateMin(), getSystemDateSec());
                            break;
                        case "工作状态":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getModbus2018JobStatus(compName)));
                            break;
                        case "测量模式":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getMeaModeSpinnerSelect(compName)));
                            break;
                        case "告警代码":
                            int alarmNum = 0;
                            if (!allAlarmInfo.get(compName).equals(context.getString(R.string.normal))) {
                                if (allAlarmInfo.get(compName).contains("-")) {
                                    switch (getNoLiquidErrorStatus(Integer.parseInt(allAlarmInfo.get(compName).replace(" ", "").split("-")[0]))) {
                                        case 1:
                                            alarmNum = 2;
                                            break;
                                        case 2:
                                            alarmNum = 3;
                                            break;
                                        case 3:
                                            alarmNum = 4;
                                            break;
                                        case 4:
                                            alarmNum = 1;
                                            break;
                                        default:
                                            alarmNum = getOtherErrorStatus(Integer.parseInt(allAlarmInfo.get(compName).replace(" ", "").split("-")[0]));
                                            if (alarmNum == 0) {
                                                if (getDevFaultStatus(Integer.parseInt(allAlarmInfo.get(compName).replace(" ", "").split("-")[0])) == 0) {
                                                    // 报错不算故障，定义为其它异常
                                                    alarmNum = 12;
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(alarmNum));
                            break;
                        case "故障代码":
                            int faultNum = 0;
                            if (!allAlarmInfo.get(compName).equals(context.getString(R.string.normal))) {
                                if (allAlarmInfo.get(compName).contains("-")) {
                                    faultNum = getDevFaultStatus(Integer.parseInt(allAlarmInfo.get(compName).replace(" ", "").split("-")[0]));
                                }
                            }
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(faultNum));
                            break;
                        case "日志代码":
                            //  String strNowAction = (getCmds(compName).getCmd(52).getValue() == null ? "0" : (String) getCmds(compName).getCmd(52).getValue());
                            //  lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getDevNotesStatus(doFlowing.get(compName), Integer.parseInt(strNowAction))));

                            int ilogNum = getLogCode(logNum);
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(ilogNum));
                            break;
                        case "软件版本":
                            String strVer = getVersionName(context).replace(".", "");
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(getNumbers(strVer));
                            break;
                        case "测量间隔":
                            String strTime = getContinueMeaSetting(compName);
                            if (strTime == null) {
                                lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData("0");
                            } else {
                                lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(strTime);
                            }
                            break;
                        case "消解池实时温度":
                            try {
                                lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(getDigestionTubeTemp(compName));
                            } catch (Exception e) {
                                lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData("0");
                            }
                            break;
                        case "校准间隔_天":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getAutoJustSettingDay(compName)));
                            break;
                        case "校准间隔_时":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getAutoJustSettingHour(compName)));
                            break;
                        case "周期测量":
                            String strTNum = getCycMeaSetting(compName);
                            if (strTNum == null) {
                                lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(0));
                            } else {
                                lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(strTNum);
                            }
                            break;
                        case "累计做样时间_天":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(0));
                            break;
                        case "累计做样时间_时":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(0));
                            break;
                        case "清洗周期":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getAutoCleanTimeSetting(compName)));
                            break;
                        case "运行模式":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(String.valueOf(getRunningMode(compName)));
                            break;
                        case "自动校准状态":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(getAutoJustStatus(compName).equals("true") ? "1" : "2");
                            break;
                        case "自动清洗状态":
                            lRtu2018CmdData.get(keyString).getRegObj(regAddr).setData(getAutoCleanStatus(compName).equals("true") ? "1" : "2");
                            break;
                    }
            } catch (Exception e) {
                Log.i("exception", "setStatusData" + e.toString());
            }
        }
    }


    /*
     * 查看是否配置了测量参数
     */
    public static boolean getSetWinParShow(String compName, String parName) {
        try {
            if (lMeaPar.size() > 0) {
                MeaParTable mt;
                MeaParCfg cfg;
                mt = lMeaPar.get(compName);
                for (int i = 0; i < mt.MeaParMap.size(); i++) {
                    cfg = mt.Get(String.valueOf(i));
                    if (cfg.getName().equals(parName)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            Log.i("exception", "getSetWinParShow" + e.toString());
        }
        return false;
    }

    /*
     * 设置关键参数
     */
    private static void setKeyParData(String compName, int compFlag, String channelInfo, int startReg, int regLen) {
        int regAddr = startReg;
        List<Map> list;
        List<Map> list2;
        List<Map> list3;
        List<Map> listb3;
        List<Map> listb4;
        String range = "";
        if (!lRtu2018CmdData.containsKey(channelInfo)) {
            return;
        }
        if (compFlag == 0) {
            range = getConfigData(compName, "RANGE");
            list = getLastHistoryFlowData(compName, context.getString(R.string.B1));
            list2 = getLastHistoryFlowData(compName, context.getString(R.string.B2));
            list3 = getLastHistoryFlowData(compName, context.getString(R.string.ZY));
            listb3 = getLastHistoryFlowData(compName, context.getString(R.string.B3));
            listb4 = getLastHistoryFlowData(compName, context.getString(R.string.B4));
            for (; regAddr < (startReg + regLen); regAddr += lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).getDataLen()) {
                try {
                    if (lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).getDescribe() != null) {
                        switch (lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).getDescribe()) {
                            case "测量精度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "YXWS"));
                                break;
                            case "消解温度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTemper))) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd"));
                                }
                                break;
                            case "消解时长":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTimer))) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc"));
                                }
                                break;
                            case "量程下限":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!getConfigData(compName, "YBLCL").equals("")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "YBLCL"));
                                }
                                break;
                            case "量程上限":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!getConfigData(compName, "YBLCH").equals("")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "YBLCH"));
                                }
                                break;
                            case "曲线斜率K":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, range, "K")));
                                break;
                            case "曲线截距B":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, range, "B")));
                                break;
                            case "标定日期_年月":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                String calTimeYM = getConfigData(compName, "CAL_SUC_TIME");
                                if (!calTimeYM.equals("")) {
                                    String[] sTime = calTimeYM.replace(" ", "-").split("[-:：]");
                                    setRegDateYearMonth(lRtu2018CmdData, channelInfo, regAddr, parseInt(sTime[0]), parseInt(sTime[1]));
                                }
                                break;
                            case "标定日期_日时":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                String calTimeDH = getConfigData(compName, "CAL_SUC_TIME");
                                if (!calTimeDH.equals("")) {
                                    String[] sTime = calTimeDH.replace(" ", "-").split("[-:：]");
                                    setRegDateDayHour(lRtu2018CmdData, channelInfo, regAddr, parseInt(sTime[2]), parseInt(sTime[3]));
                                } else {
                                    setRegDateYearMonth(lRtu2018CmdData, channelInfo, regAddr, 0, 0);
                                }
                                break;
                            case "标定日期_分秒":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                String calTimeMS = getConfigData(compName, "CAL_SUC_TIME");
                                if (!calTimeMS.equals("")) {
                                    String[] sTime = calTimeMS.replace(" ", "-").split("[-:：]");
                                    setRegDateMinSec(lRtu2018CmdData, channelInfo, regAddr, parseInt(sTime[4]), parseInt(sTime[5]));
                                }
                                break;
                            case "标液1浓度":
                                if (list.size() > 0) {
                                    String value = (ConvertUnit("mg/L", getComponentUnit(compName), list.get(0).get("C").toString(), 6));
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(value);
                                } else {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                }
                                break;
                            case "标液1吸光度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (list.size() > 0) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(list.get(0).get("A").toString());
                                }
                                break;
                            case "标液2浓度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (list2.size() > 0) {
                                    String value = (ConvertUnit("mg/L", getComponentUnit(compName), list2.get(0).get("C").toString(), 6));
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(value);
                                }
                                break;
                            case "标液2吸光度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (list2.size() > 0) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(list2.get(0).get("A").toString());
                                }
                                break;
                            case "标液3浓度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (listb3.size() > 0) {
                                    String value = (ConvertUnit("mg/L", getComponentUnit(compName), listb3.get(0).get("C").toString(), 6));
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(value);
                                }
                                break;
                            case "标液3吸光度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (listb3.size() > 0) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(listb3.get(0).get("A").toString());
                                }
                                break;
                            case "标液4浓度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (listb4.size() > 0) {
                                    String value = (ConvertUnit("mg/L", getComponentUnit(compName), listb4.get(0).get("C").toString(), 6));
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(value);
                                }
                                break;
                            case "标液4吸光度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (listb4.size() > 0) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(listb4.get(0).get("A").toString());
                                }
                                break;
                            case "试剂余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(0));
                                } else {
                                    int num = minimumReagentBottle(compName);
                                    int surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + num));
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf((surplus << 16) + num));
                                }
                                break;
                            case "消解降温温度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.dispellingCooling)) || getPublicConfigData("LogInName").equals("0")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xjjw"));
                                }
                                break;
                            case "显色降温温度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorCooling)) || getPublicConfigData("LogInName").equals("0")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xsjw"));
                                }
                                break;
                            case "显色静置":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorStand))) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xsjz"));
                                }
                                break;
                            case "测量吸光度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (list3.size() > 0) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(list3.get(0).get("A").toString());
                                }
                                break;
                            case "标样2校准_年月":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                String cal2TimeYM = getConfigData(compName, "CAL2_SUC_TIME");
                                if (!cal2TimeYM.equals("")) {
                                    String[] sTime = cal2TimeYM.replace(" ", "-").split("[-:：]");
                                    setRegDateYearMonth(lRtu2018CmdData, channelInfo, regAddr, parseInt(sTime[0]), parseInt(sTime[1]));
                                }
                                break;
                            case "标样2校准_日时":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                String cal2TimeDH = getConfigData(compName, "CAL2_SUC_TIME");
                                if (!cal2TimeDH.equals("")) {
                                    String[] sTime = cal2TimeDH.replace(" ", "-").split("[-:：]");
                                    setRegDateDayHour(lRtu2018CmdData, channelInfo, regAddr, parseInt(sTime[2]), parseInt(sTime[3]));
                                } else {
                                    setRegDateYearMonth(lRtu2018CmdData, channelInfo, regAddr, 0, 0);
                                }
                                break;
                            case "标样2校准_分秒":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                String cal2TimeMS = getConfigData(compName, "CAL2_SUC_TIME");
                                if (!cal2TimeMS.equals("")) {
                                    String[] sTime = cal2TimeMS.replace(" ", "-").split("[-:：]");
                                    setRegDateMinSec(lRtu2018CmdData, channelInfo, regAddr, parseInt(sTime[4]), parseInt(sTime[5]));
                                }
                                break;
                            case "检出限值": {
                                String value = (ConvertUnit("mg/L", getComponentUnit(compName), getMeaLimitHigh(compName), 6));
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(value);
                            }
                            break;
                            case "校准系数":
                                //lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, range, "F")));
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "ExtRange" + range + "K"));
                                break;
                            case "设备序列号1":
                                String strDevNum = getPublicConfigData("DEV_SERIAL_NUM");
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!strDevNum.equals("")) {
                                    String sn1 = strDevNum.substring(20, 24);
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(Integer.valueOf(sn1.trim(), 16)));
                                }
                                break;
                            case "设备序列号2":
                                String strDevNum2 = getPublicConfigData("DEV_SERIAL_NUM");
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!strDevNum2.equals("")) {
                                    String sn1 = strDevNum2.substring(16, 20);
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(Integer.valueOf(sn1.trim(), 16)));
                                }
                                break;
                            case "设备序列号3":
                                String strDevNum3 = getPublicConfigData("DEV_SERIAL_NUM");
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!strDevNum3.equals("")) {
                                    String sn1 = strDevNum3.substring(12, 16);
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(Integer.valueOf(sn1.trim(), 16)));
                                }
                                break;
                            case "设备序列号4":
                                String strDevNum4 = getPublicConfigData("DEV_SERIAL_NUM");
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!strDevNum4.equals("")) {
                                    String sn1 = strDevNum4.substring(8, 12);
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(Integer.valueOf(sn1.trim(), 16)));
                                }
                                break;
                            case "设备序列号5":
                                String strDevNum5 = getPublicConfigData("DEV_SERIAL_NUM");
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!strDevNum5.equals("")) {
                                    String sn1 = strDevNum5.substring(4, 8);
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(Integer.valueOf(sn1.trim(), 16)));
                                }
                                break;
                            case "设备序列号6":
                                String strDevNum6 = getPublicConfigData("DEV_SERIAL_NUM");
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!strDevNum6.equals("")) {
                                    String sn1 = strDevNum6.substring(0, 4);
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(Integer.valueOf(sn1.trim(), 16)));
                                }
                                break;
                            case "显色温度":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTemper))) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xswd").equals("") ? "0" : getConfigData(compName, "xswd"));
                                }
                                break;
                            case "显色时长":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTimer))) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "xssc").equals("") ? "0" : getConfigData(compName, "xssc"));
                                }
                                break;
                            case "试剂1余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                } else {
                                    int surplus = 255;
                                    if (lsurPlus.get(compName).getSurplusInfoMap().size() >= 1) {
                                        surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + 1));
                                    }
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(surplus));
                                }
                                break;
                            case "试剂2余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                } else {
                                    int surplus = 255;
                                    if (lsurPlus.get(compName).getSurplusInfoMap().size() >= 2) {
                                        surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + 2));
                                    }
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(surplus));
                                }
                                break;
                            case "试剂3余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                } else {
                                    int surplus = 255;
                                    if (lsurPlus.get(compName).getSurplusInfoMap().size() >= 3) {
                                        surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + 3));
                                    }
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(surplus));
                                }
                                break;
                            case "试剂4余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                } else {
                                    int surplus = 255;
                                    if (lsurPlus.get(compName).getSurplusInfoMap().size() >= 4) {
                                        surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + 4));
                                    }
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(surplus));
                                }
                                break;
                            case "试剂5余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                } else {
                                    int surplus = 255;
                                    if (lsurPlus.get(compName).getSurplusInfoMap().size() >= 5) {
                                        surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + 5));
                                    }
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(surplus));
                                }
                                break;
                            case "试剂6余量":
                                if (getConfigData(compName, "surplusOpen").equals("false")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                } else {
                                    int surplus = 255;
                                    if (lsurPlus.get(compName).getSurplusInfoMap().size() >= 6) {
                                        surplus = (int) Float.parseFloat(getConfigData(compName, "surplus" + 6));
                                    }
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(surplus));
                                }
                                break;
                            case "试剂7余量":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                break;
                            case "试剂8余量":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                break;
                            case "试剂9余量":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                break;
                            case "试剂10余量":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(255));
                                break;
                            case "试剂剩余做样量":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(calSampleTheReagent(compName, Integer.parseInt(range))));
                                break;
                            case "当前量程":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(range);
                                break;
                            case "修正系数":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "ExtRange" + range + "B"));
                                break;
                            case "当前量程低位":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!getConfigData(compName, "LC" + range + "L").equals("")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "LC" + range + "L"));
                                }
                                break;
                            case "当前量程高位":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData("0");
                                if (!getConfigData(compName, "LC" + range + "H").equals("")) {
                                    lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(getConfigData(compName, "LC" + range + "H"));
                                }
                                break;
                            case "量程个数":
                                lRtu2018CmdData.get(channelInfo).getRegObj(regAddr).setData(String.valueOf(GetPlatRangSum(compName)));
                                break;
                        }
                    }
                } catch (Exception e) {
                }
            }
        } else {

        }
    }

    /*
     * 设置常规因子关键参数
     */
    private static void setKeyParData(String compName) {
        String keyString = "KEYPAR";
        try {
            int startRegAddr = parseInt(lRtu2018CmdData.get(keyString).getMapInfo().split("_")[1]);
            int regLen = parseInt(lRtu2018CmdData.get(keyString).getMapInfo().split("_")[2]);
            setKeyParData(compName, 0, keyString, startRegAddr, regLen);
        } catch (Exception e) {

        }
    }

    /*
     * 设置五参数关键参数
     */
    private static void setFiveParKeyParData() {
        String keyString = "FIVEPAR";
        try {
            int startRegAddr = parseInt(lRtu2018FiveParCmdData.get(keyString).getMapInfo().split("_")[1]);
            int regLen = parseInt(lRtu2018FiveParCmdData.get(keyString).getMapInfo().split("_")[2]);

            setKeyParData("", 1, keyString, startRegAddr, regLen);
        } catch (Exception e) {

        }
    }

    /*
     * 设置系统关键参数
     */
    private static void setSysKeyParData() {
        String keyString;
        try {
            keyString = "BACKBOARD_SYSCONS";
            int startRegAddr = parseInt(lRtu2018SysCmdData.get(keyString).getMapInfo().split("_")[1]);
            int regLen = parseInt(lRtu2018SysCmdData.get(keyString).getMapInfo().split("_")[2]);

            setKeyParData("", 1, keyString, startRegAddr, regLen);
        } catch (Exception e) {

        }
    }

    /*
     * 将解析后的数据放至全局寄存器map中
     **/
    private static Map<Integer, RtuDataStruct> addModbusRegMap(Map<String, ModbusRtuTable> srcMap, Map<Integer, RtuDataStruct> map) {
        Iterator<Map.Entry<String, ModbusRtuTable>> iter = srcMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, ModbusRtuTable> entry = iter.next();
            String key = entry.getKey();
            ModbusRtuTable value = entry.getValue();

            Iterator<Map.Entry<Integer, RtuDataStruct>> iterStruct = value.getLModbusRweReg().entrySet().iterator();
            while (iterStruct.hasNext()) {
                Map.Entry<Integer, RtuDataStruct> entry1 = iterStruct.next();
                Integer keyReg = entry1.getKey();
                RtuDataStruct valueReg = entry1.getValue();
                map.put(keyReg, valueReg);
            }
        }
        return map;
    }

    /*
     * 增加寄存器数据到全局Map
     */
    public static void addRegisterData() {

        /*常规仪表*/
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {

                setHistoryData(item);
                setStatusData(item);
                setKeyParData(item);
                putRtu2018RegMapToCompRegMap(item, 0);
            }
        }
        setSysKeyParData();
        putRtu2018RegMapToCompRegMap("SYSTEM", 2);
    }

    /*
    将全局的缓存存储至寄存器数据map中
    * ***/
    private static void putRtu2018RegMapToCompRegMap(String CompName, int what) {
        /*将解析后的数据放置全局寄存器map中**/
        Map<Integer, RtuDataStruct> RegMap = new HashMap();
        if (what == 0) {
            RegMap = addModbusRegMap(lRtu2018CmdData, RegMap);
        } else if (what == 1) {
            RegMap = addModbusRegMap(lRtu2018FiveParCmdData, RegMap);
        } else {
            RegMap = addModbusRegMap(lRtu2018SysCmdData, RegMap);
        }
        Map<Integer, RtuDataStruct> RegNewMap;
        RegNewMap = (Map<Integer, RtuDataStruct>) CloneUtils.deepClone(RegMap);
        lRtu2018CompRegMap.put(CompName, RegNewMap);
    }


    public static String getTimeHaveSec() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
        try {
            temp = new Timestamp(format.parse(temp).getTime()).toString();
            if (temp.contains(".")) {
                temp = temp.substring(0, temp.indexOf("."));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }


    /*
    获取 工作状态
    * **/
    private static int getModbus2018JobStatus(String compName) {
        int iStatus = getJobStatus(compName);
        if (iStatus == 19 || iStatus == 34) {
            return 19;
        } else if (iStatus == 35) {
            return 33;
        } else {
            return iStatus;
        }
    }


}
