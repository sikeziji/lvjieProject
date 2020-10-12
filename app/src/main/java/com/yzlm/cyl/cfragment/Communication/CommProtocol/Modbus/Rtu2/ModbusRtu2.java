package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu2;

import android.os.Bundle;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtuTable;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuDataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.DBConvert.CloneUtils;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;
import com.yzlm.cyl.cfragment.R;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.findFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.getNowSysTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getComponentUnit;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getContinueMeaSetting;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getDevFaultStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getNoLiquidErrorStatus;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getOtherErrorStatus;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getDataStruct;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastHistoryFlowData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.ZeroPadding;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.bytesToHexString;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getNumbers;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.shortToByteArray;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat2;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.allAlarmInfo;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.cfragment.Global.getIsNoBoard;
import static com.yzlm.cyl.cfragment.Global.lRtu2018CmdData;
import static com.yzlm.cyl.cfragment.Global.lRtu2CmdData;
import static com.yzlm.cyl.cfragment.Global.lRtu2CompRegMap;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static java.lang.Integer.parseInt;

public class ModbusRtu2 {
    private static String[] strRtu2Code = new String[30];

    public static void syncListModbusRtu2() {

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
        String errorPath = modbusRtuDir + "Rtu2" + ".txt";
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
                        if (strCmd.contains("READ") || strCmd.contains("CONTROL")) {
                            /*常规因子存储*/
                            lRtu2CmdData.put(strCmd, rrt);
                            lRtu2CmdData.get(strCmd).setMapInfo(strCmdInfo);
                        }
                    }
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", "Rtu2 文件有误 ！");
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
            }
            if (lRtu2CmdData.size() > 0) {
                addRtu2RegisterData();
            }
        } else {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle bundle = new Bundle();
            bundle.putString("alert-err", " Rtu2文件丢失！");
            st.setArguments(bundle);
            st.show(fm, "Dialog_err");
        }
    }

    private static boolean AnalysisRtuCode2018File() {
        String fileContent = null;
        fileContent = findFile("Csoft/Protocol/Modbus/", "Rtu2Code");
        if (fileContent != null) {
            strRtu2Code = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
            return true;
        } else {
            return false;
        }
    }

    private static void setRegDateMonthYear(Map<String, ModbusRtuTable> map, String regStrInfo, int regAddr, int month, int year) {
        int b = new java.math.BigInteger(String.valueOf(month), 10).intValue();
        int cd = new java.math.BigInteger(String.valueOf((year - 1970) & 0xff), 10).intValue();
        short bcd = (short) (b * 256 + cd);
        map.get(regStrInfo).getRegObj(regAddr).setData(String.valueOf(bcd));
    }

    private static void setRegDateHourDay(Map<String, ModbusRtuTable> map, String regStrInfo, int regAddr, int hour, int day) {
        int b = new java.math.BigInteger(String.valueOf(hour), 10).intValue();
        int cd = new java.math.BigInteger(String.valueOf(day & 0xff), 10).intValue();
        short bcd = (short) (b * 256 + cd);
        map.get(regStrInfo).getRegObj(regAddr).setData(String.valueOf(bcd));
    }

    private static void setRegDateSecMin(Map<String, ModbusRtuTable> map, String regStrInfo, int regAddr, int sec, int min) {
        int b = new java.math.BigInteger(String.valueOf(sec), 10).intValue();
        int cd = new java.math.BigInteger(String.valueOf(min & 0xff), 10).intValue();
        short bcd = (short) (b * 256 + cd);
        map.get(regStrInfo).getRegObj(regAddr).setData(String.valueOf(bcd));
    }

    /*
     * 增加寄存器数据到全局Map
     */
    public static void addRtu2RegisterData() {
        /*常规仪表*/
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                int startRegAddr = parseInt(lRtu2CmdData.get("READ").getMapInfo().split("_")[1]);
                int regLen = parseInt(lRtu2CmdData.get("READ").getMapInfo().split("_")[2]);
                try {
                    setHistoryData(lRtu2CmdData, "READ", item, 0, startRegAddr, regLen);
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
            RegMap = addModbusRegMap(lRtu2CmdData, RegMap);
        }
        Map<Integer, RtuDataStruct> RegNewMap;
        RegNewMap = (Map<Integer, RtuDataStruct>) CloneUtils.deepClone(RegMap);
        lRtu2CompRegMap.put(CompName, RegNewMap);
    }

    /**
     * 组份，组份类型（常规还是五参数），开始寄存器地址，单通道总的寄存器数
     */
    private static void setHistoryData(Map<String, ModbusRtuTable> map, String mapKey, String compName, int compFlag, int startReg, int regLen) {
        int regAddr = startReg;

        String strHisData = "0";

        int point = Integer.parseInt(getConfigData(compName, "YXWS"));
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量
        List<Map> listzy = null, listby2 = null, listlycl = null, listldhc = null, listkdhc = null, listjbhs = null, listpxy = null, listby1 = null, listbyhc = null;
        List<Map> listb1 = null, listb2 = null;
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量,标样核查
        DataStruct[] dataStruct = new DataStruct[1];
        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.valueOf(time[0]);
        int month = Integer.valueOf(time[1]);
        int day = Integer.valueOf(time[2]);
        int hour = Integer.valueOf(time[3]);
        int min = Integer.valueOf(time[4]);
        int sec = Integer.valueOf(time[5]);
        String range = "";
        if (compFlag == 0) {
            range = getConfigData(compName, "RANGE");
            if (!getModePermissions(compName, "历史数据上传")) {
                return;
            }
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                listzy = getLastHistoryFlowData(compName, null);
            } else {
                listzy = getLastHistoryFlowData(compName, context.getString(R.string.ZY));
            }
            listb1 = getLastHistoryFlowData(compName, context.getString(R.string.B1));
            listb2 = getLastHistoryFlowData(compName, context.getString(R.string.B2));
            //做样
            dataStruct[0] = getDataStruct(listzy, 0);
            strHisData = dataFormat2(String.valueOf(Float.valueOf(dataStruct[0].getData())), point);
        } else {

        }
        for (; regAddr < (startReg + regLen); regAddr += map.get(mapKey).getRegObj(regAddr).getDataLen()) {
            if (map.get(mapKey).getRegObj(regAddr) != null) {
                if (map.get(mapKey).getRegObj(regAddr).getDescribe() != null)
                    switch (map.get(mapKey).getRegObj(regAddr).getDescribe()) {
                        case "水样测量值":
                            map.get(mapKey).getRegObj(regAddr).setData(strHisData);
                            break;
                        case "参数代码":
                            AnalysisRtuCode2018File();
                            int code = 0;
                            String strCode = getCode(compName, strRtu2Code);
                            if (!strCode.equals("")) {
                                code = Integer.parseInt(strCode);
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(code));
                            break;
                        case "工作状态":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getModbusRtu2JobStatus(compName)));
                            break;
                        case "时间标准_秒分":
                            setRegDateSecMin(map, mapKey, regAddr, 0, min);
                            break;
                        case "时间标准_时日":
                            setRegDateHourDay(map, mapKey, regAddr, hour, day);
                            break;
                        case "时间标准_月年":
                            setRegDateMonthYear(map, mapKey, regAddr, month, year);
                            break;
                        case "测量时间_秒分":
                            setRegDateSecMin(map, mapKey, regAddr, 0, dataStruct[0].getMin());
                            break;
                        case "测量时间_时日":
                            setRegDateHourDay(map, mapKey, regAddr, dataStruct[0].getHour(), dataStruct[0].getDay());
                            break;
                        case "测量时间_月年":
                            setRegDateMonthYear(map, mapKey, regAddr, dataStruct[0].getMonth(), dataStruct[0].getYear());
                            break;
                        case "测量吸光度":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(dataStruct[0].getAi()));
                            break;
                        case "读取当前量程":
                            String strDQLCH = getConfigData(compName, "LC" + range + "H");
                            String strDQLCL = getConfigData(compName, "LC" + range + "L");
                            byte[] bDQLCH = new byte[]{0x00, 0x00};
                            byte[] bDQLCL = new byte[]{0x00, 0x00};
                            if (!strDQLCH.equals("")) {
                                bDQLCH = shortToByteArray((int) Float.parseFloat(strDQLCH));
                            }
                            if (!strDQLCL.equals("")) {
                                bDQLCL = shortToByteArray((int) Float.parseFloat(strDQLCL));
                            }
                            byte[] dqlcByte = new byte[]{bDQLCL[0], bDQLCL[1], bDQLCH[0], bDQLCH[1]};
                            map.get(mapKey).getRegObj(regAddr).setData(bytesToHexString(dqlcByte));
                            break;
                        case "量程1标样2浓度":
                            String strLC1BY2CKND = getConfigData(compName, "C1");
                            if (strLC1BY2CKND.equals("")) {
                                strLC1BY2CKND = "0";
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(strLC1BY2CKND);
                            break;
                        case "量程2标样2浓度":
                            String strLC2BY2CKND = getConfigData(compName, "C3");
                            if (strLC2BY2CKND.equals("")) {
                                strLC2BY2CKND = "0";
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(strLC2BY2CKND);
                            break;
                        case "量程3标样2浓度":
                            String strLC3BY2CKND = getConfigData(compName, "C5");
                            if (strLC3BY2CKND.equals("")) {
                                strLC3BY2CKND = "0";
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(strLC3BY2CKND);
                            break;
                        case "量程4标样2浓度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            break;
                        case "仪器编号":
                            long lYQBH = Long.parseLong(getConfigData(compName, "YQBH"));
                            String strHexLong = ZeroPadding(Long.toHexString(lYQBH), 8);
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (!strHexLong.equals("")) {
                                map.get(mapKey).getRegObj(regAddr).setData(strHexLong);
                            }
                            break;
                        case "消解温度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTemper))) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd")));
                            }
                            break;
                        case "显色时长":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTimer))) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xssc")).equals("") ? "0" : String.valueOf(getConfigData(compName, "xssc")));
                            }
                            break;
                        case "显色温度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTemper))) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xswd").equals("") ? "0" : getConfigData(compName, "xswd")));
                            }
                            break;
                        case "消解时间":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTimer))) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc")));
                            }
                            break;
                        case "工作曲线K":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K")));
                            break;
                        case "工作曲线B":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B")));
                            break;
                        case "量程1标样1浓度":
                            String strLC1BY1CKND = getConfigData(compName, "C0");
                            if (strLC1BY1CKND.equals("")) {
                                strLC1BY1CKND = "0";
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(strLC1BY1CKND);
                            break;
                        case "量程2标样1浓度":
                            String strLC2BY1CKND = getConfigData(compName, "C2");
                            if (strLC2BY1CKND.equals("")) {
                                strLC2BY1CKND = "0";
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(strLC2BY1CKND);
                            break;
                        case "量程3标样1浓度":
                            String strLC3BY1CKND = getConfigData(compName, "C4");
                            if (strLC3BY1CKND.equals("")) {
                                strLC3BY1CKND = "0";
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(strLC3BY1CKND);
                            break;
                        case "量程4标样1浓度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            break;
                        case "读取量程":
                            String strYBLCH = getConfigData(compName, "YBLCH");
                            String strYBLCL = getConfigData(compName, "YBLCL");
                            byte[] bYBLCH = new byte[]{0x00, 0x00};
                            byte[] bYBLCL = new byte[]{0x00, 0x00};
                            if (!strYBLCH.equals("")) {
                                bYBLCH = shortToByteArray(Integer.valueOf(strYBLCH));
                            }
                            if (!strYBLCL.equals("")) {
                                bYBLCL = shortToByteArray(Integer.valueOf(strYBLCL));
                            }
                            byte[] lcByte = new byte[]{bYBLCL[0], bYBLCL[1], bYBLCH[0], bYBLCH[1]};
                            map.get(mapKey).getRegObj(regAddr).setData(bytesToHexString(lcByte));
                            break;
                        case "做样周期":
                            byte[] zyzqByte = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                            if (!getConfigData(compName, "zqclh").equals("")) {
                                String[] strCycTime = getConfigData(compName, "zqclh").split("[-，]");
                                for (String aStrCycTime : strCycTime) {
                                    zyzqByte[Integer.parseInt(aStrCycTime)] = 0x01;
                                }
                            }
                            map.get(mapKey).getRegObj(regAddr).setData(bytesToHexString(zyzqByte));
                            break;
                        case "做样间隔":
                            String strTime = getContinueMeaSetting(compName);
                            if (strTime == null) {
                                map.get(mapKey).getRegObj(regAddr).setData("0");
                            } else {
                                map.get(mapKey).getRegObj(regAddr).setData(strTime);
                            }
                            break;
                        case "工作模式":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getModbusRtu2MeaMode(compName)));
                            break;
                        case "工作报错":
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
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(alarmNum));
                            break;
                        case "校正因子":
                            map.get(mapKey).getRegObj(regAddr).setData(getConfigData(compName, "ExtRange" + range + "K"));
                            break;
                        case "修正因子":
                            map.get(mapKey).getRegObj(regAddr).setData(getConfigData(compName, "ExtRange" + range + "B"));
                            break;
                        case "校准标1数据":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (listb1.size() > 0) {
                                String value = (ConvertUnit("mg/L", getComponentUnit(compName), listb1.get(0).get("C").toString(), 6));
                                map.get(mapKey).getRegObj(regAddr).setData(value);
                            }
                            break;
                        case "校准标1吸光度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (listb1.size() > 0) {
                                map.get(mapKey).getRegObj(regAddr).setData(listb1.get(0).get("A").toString());
                            }
                            break;
                        case "校准标2数据":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (listb2.size() > 0) {
                                String value = (ConvertUnit("mg/L", getComponentUnit(compName), listb2.get(0).get("C").toString(), 6));
                                map.get(mapKey).getRegObj(regAddr).setData(value);
                            }
                            break;
                        case "校准标2吸光度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (listb2.size() > 0) {
                                map.get(mapKey).getRegObj(regAddr).setData(listb2.get(0).get("A").toString());
                            }
                            break;
                        case "VOC组分1":
                        case "VOC组分2":
                        case "VOC组分3":
                        case "VOC组分4":
                        case "VOC组分5":
                        case "VOC组分6":
                        case "VOC组分7":
                        case "VOC组分8":
                        case "VOC组分9":
                        case "VOC组分10":
                            String strNum = getNumbers(map.get(mapKey).getRegObj(regAddr).getDescribe());
                            int iNum = Integer.parseInt(strNum);
                            if (iNum > 0) {
                                String strComps = dataStruct[0].getStrEngs();
                                if (strComps != null) {
                                    String[] strMea = strComps.split(";");
                                    if (strMea.length > 0 && (iNum - 1) < strMea.length) {
                                        //String strData = dataFormat2((strMea[iNum - 1].split("=")[1].split(" ")[0]), point);
                                        String strData = dataFormat2((strMea[iNum - 1]), point);
                                        map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(strData));
                                    } else {
                                        map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(0));
                                    }
                                } else {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(0));
                                }
                            } else {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(0));
                            }
                            break;
                    }
            } else {
                break;
            }
        }
    }

    /*
    获取 工作状态
    * **/
    private static int getModbusRtu2JobStatus(String compName) {
        int iStatus = getJobStatus(compName);
        switch (iStatus) {
            case 2:
                return 16;
            case 3:
                return 14;
            case 4:
                return 15;
            case 5:
                return 6;
            case 6:
                return 17;
            case 7:
                return 18;
            case 10:
                return 5;
            case 19:
                if (getIsNoBoard(compName)) {
                    if (getNoBoardConfigData(compName, "CAL_CONTROL").equals("true")) {
                        return 2;
                    }
                } else {
                    if (getConfigData(compName, "CAL_CONTROL").equals("true")) {
                        return 2;
                    }
                }
                return 3;
            case 28:
                return 10;
            case 29:
                return 11;
            case 34:
                if (getIsNoBoard(compName)) {
                    if (getNoBoardConfigData(compName, "CAL_CONTROL").equals("true")) {
                        return 2;
                    }
                } else {
                    if (getConfigData(compName, "CAL_CONTROL").equals("true")) {
                        return 2;
                    }
                }
                return 4;
            case 32:
                return 7;
            case 35:
                return 8;
            case 36:
                return 12;
            case 37:
                return 13;
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return 19;
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

}
