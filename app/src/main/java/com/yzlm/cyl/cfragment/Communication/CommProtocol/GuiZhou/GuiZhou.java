package com.yzlm.cyl.cfragment.Communication.CommProtocol.GuiZhou;

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
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getSetWinParShow;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getJobStatus;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getDataStruct;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastHistoryFlowData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat2;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.cfragment.Global.lGuiZhouCmdData;
import static com.yzlm.cyl.cfragment.Global.lGuiZhouCompRegMap;
import static com.yzlm.cyl.cfragment.Global.lRtu2CmdData;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static java.lang.Integer.parseInt;

public class GuiZhou {
    private static String[] strGuiZhouCode = new String[30];

    public static void syncListGuiZhou() {

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
        String errorPath = modbusRtuDir + "GuiZhou" + ".txt";
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
                    if (strsData[i].contains("READ")) {
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
                            lGuiZhouCmdData.put(strCmd, rrt);
                            lGuiZhouCmdData.get(strCmd).setMapInfo(strCmdInfo);
                        }
                    }
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", "GuiZhou 文件有误 ！");
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
            }
            if (lGuiZhouCmdData.size() > 0) {
                addGuiZhouRegisterData();
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
        fileContent = findFile("Csoft/Protocol/Modbus/", "GuiZhouCode");
        if (fileContent != null) {
            strGuiZhouCode = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
            return true;
        } else {
            return false;
        }
    }

    /*
     * 增加寄存器数据到全局Map
     */
    public static void addGuiZhouRegisterData() {
        /*常规仪表*/
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                int startRegAddr = parseInt(lGuiZhouCmdData.get("READ").getMapInfo().split("_")[1]);
                int regLen = parseInt(lGuiZhouCmdData.get("READ").getMapInfo().split("_")[2]);
                try {
                    setHistoryData(lGuiZhouCmdData, "READ", item, 0, startRegAddr, regLen);
                } catch (Exception e) {
                    saveExceptInfo2File("组分[" + item + "]" + "setHistoryData()" + e.toString());
                }
                putGuiZhouRegMapToCompRegMap(item, 0);
            }
        }
    }

    /*
    将全局的缓存存储至寄存器数据map中
    * ***/
    private static void putGuiZhouRegMapToCompRegMap(String CompName, int what) {
        /*将解析后的数据放置全局寄存器map中**/
        Map<Integer, RtuDataStruct> RegMap = new HashMap();
        if (what == 0) {
            RegMap = addGuiZhouRegMap(lGuiZhouCmdData, RegMap);
        }
        Map<Integer, RtuDataStruct> RegNewMap;
        RegNewMap = (Map<Integer, RtuDataStruct>) CloneUtils.deepClone(RegMap);
        lGuiZhouCompRegMap.put(CompName, RegNewMap);
    }

    /*
     * 将解析后的数据放至全局寄存器map中
     **/
    private static Map<Integer, RtuDataStruct> addGuiZhouRegMap(Map<String, ModbusRtuTable> srcMap, Map<Integer, RtuDataStruct> map) {
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

    /**
     * 组份，组份类型（常规还是五参数），开始寄存器地址，单通道总的寄存器数
     */
    private static void setHistoryData(Map<String, ModbusRtuTable> map, String mapKey, String compName, int compFlag, int startReg, int regLen) {
        int regAddr = startReg;

        String strHisData = "0";

        int point = Integer.parseInt(getConfigData(compName, "YXWS"));
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量
        List<Map> listzy = null, listby2 = null, listlycl = null, listldhc = null, listkdhc = null, listjbhs = null, listpxy = null, listby1 = null, listbyhc = null;
        // 做样数据，,标2测量，零样测量，零点核查，跨度核查，加标回收率，平行样，标1测量,标样核查
        DataStruct[] dataStruct = new DataStruct[1];

        if (compFlag == 0) {
            if (!getModePermissions(compName, "历史数据上传")) {
                return;
            }
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                listzy = getLastHistoryFlowData(compName, null);
            } else {
                listzy = getLastHistoryFlowData(compName, context.getString(R.string.ZY));
            }
            //做样
            dataStruct[0] = getDataStruct(listzy, 0);
            strHisData = dataFormat2(String.valueOf(Float.valueOf(dataStruct[0].getData())), point);
        } else {

        }
        for (; regAddr < (startReg + regLen); regAddr += map.get(mapKey).getRegObj(regAddr).getDataLen()) {
            if (map.get(mapKey).getRegObj(regAddr) != null) {
                if (map.get(mapKey).getRegObj(regAddr).getDescribe() != null)
                    switch (map.get(mapKey).getRegObj(regAddr).getDescribe()) {
                        case "设备编号":
                            switch (compName) {
                                case "CODcr":
                                    map.get(mapKey).getRegObj(regAddr).setData("1");
                                    break;
                                case "NH3N":
                                    map.get(mapKey).getRegObj(regAddr).setData("2");
                                    break;
                                case "TP":
                                    map.get(mapKey).getRegObj(regAddr).setData("3");
                                    break;
                                case "TN":
                                    map.get(mapKey).getRegObj(regAddr).setData("4");
                                    break;
                            }
                            break;
                        case "实时监测数据":
                            map.get(mapKey).getRegObj(regAddr).setData(strHisData);
                            break;
                        case "数据标记":
                            map.get(mapKey).getRegObj(regAddr).setData(getDataFlag(compName, listzy, 0));
                            break;
                        case "运行状态":
                            map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getGuiZhouJobStatus(compName)));
                            break;
                        case "COD量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                String strYBLCH = getConfigData(compName, "YBLCH");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "COD斜率":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K")));
                            }
                            break;
                        case "COD截距":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B")));
                            }
                            break;
                        case "COD消解时间":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTimer))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc")));
                                }
                            }
                            break;
                        case "COD消解温度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTemper))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd")));
                                }
                            }
                            break;
                        case "COD量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                String strYBLCL = getConfigData(compName, "YBLCL");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "COD校正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "K");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "COD修正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "B");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "COD当前量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "H");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "COD当前量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("CODcr")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "L");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "氨氮量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                String strYBLCH = getConfigData(compName, "YBLCH");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "氨氮斜率":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K")));
                            }
                            break;
                        case "氨氮截距":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B")));
                            }
                            break;
                        case "氨氮显色时长":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTimer))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xssc").equals("") ? "0" : getConfigData(compName, "xssc")));
                                }
                            }
                            break;
                        case "氨氮显色温度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTemper))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xswd").equals("") ? "0" : getConfigData(compName, "xswd")));
                                }
                            }
                            break;
                        case "氨氮量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                String strYBLCL = getConfigData(compName, "YBLCL");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "氨氮校正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "K");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "氨氮修正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "B");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "氨氮当前量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "H");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "氨氮当前量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("NH3N")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "L");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "总磷量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                String strYBLCH = getConfigData(compName, "YBLCH");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "总磷斜率":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K")));
                            }
                            break;
                        case "总磷截距":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B")));
                            }
                            break;
                        case "总磷消解时间":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTimer))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc")));
                                }
                            }
                            break;
                        case "总磷消解温度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTemper))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd")));
                                }
                            }
                            break;
                        case "总磷显色时长":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTimer))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xssc").equals("") ? "0" : getConfigData(compName, "xssc")));
                                }
                            }
                            break;
                        case "总磷量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                String strYBLCL = getConfigData(compName, "YBLCL");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "总磷校正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "K");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "总磷修正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "B");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "总磷当前量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "H");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "总磷当前量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TP")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "L");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "总氮量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                String strYBLCH = getConfigData(compName, "YBLCH");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "总氮斜率":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K")));
                            }
                            break;
                        case "总氮截距":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B")));
                            }
                            break;
                        case "总氮消解时间":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTimer))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc")));
                                }
                            }
                            break;
                        case "总氮消解温度":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.digestionTemper))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd")));
                                }
                            }
                            break;
                        case "总氮显色时长":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                if (getSetWinParShow(compName, context.getResources().getString(R.string.colorTimer))) {
                                    map.get(mapKey).getRegObj(regAddr).setData(String.valueOf(getConfigData(compName, "xssc").equals("") ? "0" : getConfigData(compName, "xssc")));
                                }
                            }
                            break;
                        case "总氮量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                String strYBLCL = getConfigData(compName, "YBLCL");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "总氮校正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "K");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "总氮修正因子":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                String strYBLCL = getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "B");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCL);
                            }
                            break;
                        case "总氮当前量程高位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "H");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                        case "总氮当前量程低位":
                            map.get(mapKey).getRegObj(regAddr).setData("0");
                            if (compName.equals("TN")) {
                                String strYBLCH = getConfigData(compName, "LC" + getConfigData(compName, "RANGE") + "L");
                                map.get(mapKey).getRegObj(regAddr).setData(strYBLCH);
                            }
                            break;
                    }
            } else {
                break;
            }
        }
    }

    /**
     * 数据标识  compFlag =0 常规组份， 1 五参数组份
     */
    private static String getDataFlag(String compName, List<Map> list, int compFlag) {

        String ret;
        {
            if (!workState.get(compName).equals(context.getString(R.string.normal))) {
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
                } else {
                    ret = "N";
                }
                return ret;
            } else {
                return "N";
            }
        }
    }

    /*
    获取 工作状态
    * **/
    private static int getGuiZhouJobStatus(String compName) {
        int iStatus = getJobStatus(compName);
        switch (iStatus) {
            case 0:
                return 1;
            case 1:
                return 16;
            case 2:
            case 3:
            case 4:
                return 288;
            case 28:
            case 29:
            case 5:
                return 128;
            case 6:
                return 320;
            case 7:
                return 384;
            case 10:
                return 64;
            case 19:
            case 34:
                return 32;
            case 32:
                return 272;
            case 35:
                return 257;
            case 30:
                return 256;
            default:
                return 512;
        }
    }

}
