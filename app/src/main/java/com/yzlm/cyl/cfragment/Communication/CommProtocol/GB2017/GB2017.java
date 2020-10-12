package com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.findFile;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCode;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getCrc16;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataCmd;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.replaceTempData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLastHistory;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getStrNowSysTemTimeWithOutms;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getDataStruct;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastUpdateData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.workState;

/**
 * Created by zwj on 2017/9/7.
 */

public class GB2017 {
    private static String[] strCode2017 = new String[30];
    private static String[] strCmd2017 = new String[40];
    final static int COC_CH = 1;               /*1测量值上传*/
    final static int COCHOUR_CH = 2;               /*做样点测量值上传*/
    static String MN = "123456789101112131415161";

    public GB2017() {
        if (strCode2017[0] == null) {
            strCode2017 = AnalysisCodeFile("Csoft/Protocol/GB/", "Code2017");
        }
    }

    // 找Code代码
    public static String[] AnalysisCodeFile(String path, String fileName) {
        String fileContent;
        String[] strCode = new String[0];
        fileContent = findFile(path, fileName);
        if (fileContent != null) {
            strCode = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
            return strCode;
        } else {
            return strCode;
        }
    }

    // 找国标协议文件
    public static String[] AnalysisCmdFileGB(String path, String fileName) {
        String fileContent;
        String[] strCmd = new String[0];
        fileContent = findFile(path, fileName);
        if (fileContent != null) {
            strCmd = fileContent.split("\r\n");
        }
        return strCmd;
    }


    public static String getNowSysTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    // 时间封装
    public static String getNowSysTime(int year, int month, int day, int hour, int min, int sec, int ms) {

        return year + GB2005.iDataFormat(String.valueOf(month), 2)
                + GB2005.iDataFormat(String.valueOf(day), 2)
                + GB2005.iDataFormat(String.valueOf(hour), 2)
                + GB2005.iDataFormat(String.valueOf(min), 2)
                + GB2005.iDataFormat(String.valueOf(sec), 2)
                + GB2005.iDataFormat(String.valueOf(ms), 3);
    }

    // 时间封装
    public static String getNowSysTime(int year, int month, int day, int hour, int min, int sec) {

        return year + GB2005.iDataFormat(String.valueOf(month), 2)
                + GB2005.iDataFormat(String.valueOf(day), 2)
                + GB2005.iDataFormat(String.valueOf(hour), 2)
                + GB2005.iDataFormat(String.valueOf(min), 2)
                + GB2005.iDataFormat(String.valueOf(sec), 2);
    }

    // 组份，状态，流程号
    private static String getDevGB2017Status(String Component, String info, String flowName) {
        // 工作正常   仪器仪表停运，仪表未设置自动做样  仪器仪表故障，仪表故障报错	   仪器仪表处于校准状态   仪器仪表采样数值超过测量上限
        String[] status = {"N", "F", "D", "C", "T",};
        String str = "";
        FlowTable ft = Global.getFlows(Component);
        if (ft == null) return str;
        ActionTable at = Global.getActions(Component);
        if (at == null) return str;
        String strNormal = context.getResources().getString(R.string.normal);
        // "正常":
        if (info.equals(strNormal)) {
            if ((flowName.contains(context.getResources().getString(R.string.span_1))) || (flowName.contains(context.getResources().getString(R.string.span_2)))) {
                str = status[3];
            } else if ((flowName.contains(context.getResources().getString(R.string.waiting_for_instructions))) && getConfigData(Component, "LXCL").equals("false")
                    && getConfigData(Component, "ZQCL").equals("false")) {
                str = status[1];
            } else {
                str = status[0];
            }
            /*判断是否超过报警上下限*/
            List<Map> list = getLastUpdateData(Component);
            if (list.size() > 0) {
                float data = Float.parseFloat(list.get(0).get("C").toString());
                if (data < Float.parseFloat(getConfigData(Component, "YBLCL"))
                        || data > Float.parseFloat(getConfigData(Component, "YBLCH"))) {
                    str = status[4];
                }
            }
        } else {
            /*报错了*/
            str = status[2];
        }
        return str;
    }


    // 组份 数据封装
    // 测量时间，测量值，数据标识
    private static String[] packageGb2017MeaData(DataStruct DataInfo, String[] StrGbCode) {
        String[] str = new String[3];
        str[0] = getDataTime(DataInfo.getYear(), DataInfo.getMonth(), DataInfo.getDay(), DataInfo.getHour(), DataInfo.getMin(), DataInfo.getSec());
        str[1] = getCode(DataInfo.getType(), StrGbCode) + "-Rtd=" + dataFormat(DataInfo.getData(), Integer.parseInt(getConfigData(DataInfo.getType(), "YXWS")));
        str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevGB2017Status(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()));
        return str;
    }


    public static String stringTimeCompare(String oldTime, String newTime) {

        if ((!oldTime.equals("")) && (!newTime.equals(""))) {

            String strOldTime = oldTime.substring(0, 12);
            String strNewTime = newTime.substring(0, 12);
            Long lOldTime = Long.valueOf(strOldTime);
            Long lNewTime = Long.valueOf(strNewTime);
            if (lOldTime > lNewTime) {
                return oldTime;
            } else {
                return newTime;
            }
        } else {
            return oldTime;
        }
    }

    // 浓度上传命令封装
    private static String packageMeaDataUpdate(String cmd, DataStruct DataInfo, boolean isHistory, byte what) {
        String dataTime = "";
        String[] StrHeadType = {"$T$", "$MN$", "XXXX"};
        String[] StrDataType = {"$T1$", "$C1$-Rtd=", "$C1$-Flag="};
        String[] StrEnd = {"CRC"};
        String[] strings = cmd.split("@");

        String[] dataStr = new String[strComponent.get(1).length];
        DataStruct[] dataStruct = new DataStruct[strComponent.get(1).length];


        if (isHistory) {
            String flowName;
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
                    String[] temp = packageGb2017MeaData(dataStruct[i], strCode2017);
                    dataStr[i] = replaceTempData(strings[1], StrDataType, 0, temp, 0, 3);

                    if (!dataTime.equals("")) {
                        //判断时间是否是最新的
                        dataTime = stringTimeCompare(dataTime, temp[0]);
                    } else {
                        dataTime = temp[0];
                    }
                }
            }
        } else {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                dataStr[i] = "";

                DataInfo.setType(strComponent.get(1)[i]);
                String[] temp = packageGb2017MeaData(DataInfo, strCode2017);
                dataStr[i] = replaceTempData(strings[1], StrDataType, 0, temp, 0, 3);

                if (!dataTime.equals("")) {
                    //判断时间是否是最新的
                    dataTime = stringTimeCompare(dataTime, temp[0]);
                } else {
                    dataTime = temp[0];
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

        /*帧头组装*/
        String[] headTemp = new String[3];
        headTemp[0] = getStrNowSysTemTimeWithOutms();

        cmd = replaceTempData(strings[0], StrHeadType, 0, headTemp, 0, 1);
        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("GB2017ORG_ADDR").equals(""))
            strMn[0] = getPublicConfigData("GB2017ORG_ADDR") + getPublicConfigData("GB2017DEV_ADDR");
        else
            strMn[0] = MN;
        cmd = replaceTempData(cmd, StrHeadType, 1, strMn, 0, 1);

        if (what == 1) {
            cmd = cmd.replaceFirst("CN=2011", "CN=2082");
        }
        /*头+数据段+尾*/
        cmd += strData;

        /*兼容只有一个dataTime的情况*/
        if ((!dataTime.equals("")) && cmd.contains("$T1$")) {
            cmd = cmd.replace("$T1$", dataTime + "000");
        }
        /*将数据内容区的最后一个符号";"去除*/
        cmd = cmd.substring(0, cmd.length() - 1);

        cmd += strings[2];
        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = GB2005.iDataFormat(String.valueOf(getDataCmd(cmd, "QN", "&&").length()), 4);
        cmd = replaceTempData(cmd, StrHeadType, 2, strLen, 0, 1);
        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(cmd, "QN", "&&"), findCrc(cmd));
        StrEnd[0] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        /*替换CRC*/
        cmd = replaceTempData(cmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
        return cmd;
    }

    // 获取组份上传命令
    public String getMeaDataUpdateCmd2017(DataStruct DataInfo, boolean isHistory, byte what) {
        if (strCmd2017[COC_CH] == null) {
            strCmd2017 = AnalysisCmdFileGB("Csoft/Protocol/GB/", "GB2017");
            if (strCmd2017[COC_CH] == null) {
                return "";
            }
        }
        return packageMeaDataUpdate(strCmd2017[COC_CH], DataInfo, isHistory, what);
    }


    /*
     * 根据选择的时间及数据类型上传
     */
    private static String packageSetTimeMeaDataUpdate(String cmd, String timeAndType) {
        String dataTime = "";
        String[] StrHeadType = {"$T$", "$MN$", "XXXX"};
        String[] StrDataType = {"$T1$", "$C1$-Rtd=", "$C1$-Flag="};
        String[] StrEnd = {"CRC"};
        String[] strings = cmd.split("@");

        String[] dataStr = new String[strComponent.get(1).length];
        DataStruct[] dataStruct = new DataStruct[strComponent.get(1).length];

        History history = new History(context);
        List<Map> hisMap = history.selectTimeData(timeAndType.split("_")[0]);


        /*组装常规仪表不同元素数据字符串*/
        String strData = "";
        /*组装水站不同元素数据字符串*/
        String strWStationData = "";

        if (hisMap.size() > 0) {
            /*所有元素数据段内容组装*/
            for (int i = 0; i < strComponent.get(1).length; i++) {

                dataStruct[i] = getDataStruct(hisMap, i);
                dataStr[i] = "";
                if (dataStruct[i].getType() != null) {
                    String[] temp = packageGb2017MeaData(dataStruct[i], strCode2017);
                    dataStr[i] = replaceTempData(strings[1], StrDataType, 0, temp, 0, 3);

                    if (!dataTime.equals("")) {
                        //判断时间是否是最新的
                        dataTime = stringTimeCompare(dataTime, temp[0]);
                    } else {
                        dataTime = temp[0];
                    }
                }
            }
            for (String aDataStr : dataStr) {
                if (!aDataStr.equals("")) {
                    strData += aDataStr;
                    strData += ";";
                }
            }
        }

        if (strData.equals("")) {
            return "";
        }

        /*帧头组装*/
        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.valueOf(time[0]);
        int month = Integer.valueOf(time[1]);
        int day = Integer.valueOf(time[2]);
        int hour = Integer.valueOf(time[3]);
        int min = Integer.valueOf(time[4]);
        int sec = Integer.valueOf(time[5]);
        int ms = Integer.valueOf(time[6]);

        String[] headTemp = new String[3];
        headTemp[0] = getNowSysTime(year, month, day, hour, min, sec, ms);
        cmd = replaceTempData(strings[0], StrHeadType, 0, headTemp, 0, 1);
        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("GB2017ORG_ADDR").equals(""))
            strMn[0] = getPublicConfigData("GB2017ORG_ADDR") + getPublicConfigData("GB2017DEV_ADDR");
        else
            strMn[0] = MN;
        cmd = replaceTempData(cmd, StrHeadType, 1, strMn, 0, 1);

        /*头+数据段+尾*/
        cmd += strData + strWStationData;

        /*兼容只有一个dataTime的情况*/
        if ((!dataTime.equals("")) && cmd.contains("$T1$")) {
            cmd = cmd.replace("$T1$", dataTime);
        }
        /*将数据内容区的最后一个符号";"去除*/
        cmd = cmd.substring(0, cmd.length() - 1);

        cmd += strings[2];
        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = GB2005.iDataFormat(String.valueOf(getDataCmd(cmd, "QN", "&&").length()), 4);
        cmd = replaceTempData(cmd, StrHeadType, 2, strLen, 0, 1);
        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(cmd, "QN", "&&"), findCrc(cmd));
        StrEnd[0] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        /*替换CRC*/
        cmd = replaceTempData(cmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
        return cmd;
    }

    /*
     * 获取指定时间的数据进行数补传
     */
    public static String getSetTimeMeaDataUpdateCmd2017(String timeAndType) {
        if (strCmd2017[COC_CH] == null) {
            strCmd2017 = AnalysisCmdFileGB("Csoft/Protocol/GB/", "GB2017");
            if (strCmd2017[COC_CH] == null) {
                return "";
            }
        }
        return packageSetTimeMeaDataUpdate(strCmd2017[COC_CH], timeAndType);
    }


    // 上传通知命令
    private static String getReplyToReplyCmd() {
        String strCmd = "##XXXXQN=$T$;ST=91;CN=9013;PW=123456;MN=$MN$;Flag=4;CP=&&&&CRC1";
        String[] StrHeadType = {"$T$", "$MN$", "XXXX"};
        String[] StrEnd = {"CRC"};
        /*帧头组装*/
        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.valueOf(time[0]);
        int month = Integer.valueOf(time[1]);
        int day = Integer.valueOf(time[2]);
        int hour = Integer.valueOf(time[3]);
        int min = Integer.valueOf(time[4]);
        int sec = Integer.valueOf(time[5]);
        int ms = Integer.valueOf(time[6]);

        String[] headTemp = new String[3];
        headTemp[0] = getNowSysTime(year, month, day, hour, min, sec, ms);
        strCmd = replaceTempData(strCmd, StrHeadType, 0, headTemp, 0, 1);

        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("GB2017ORG_ADDR").equals(""))
            strMn[0] = getPublicConfigData("GB2017ORG_ADDR") + getPublicConfigData("GB2017DEV_ADDR");
        else
            strMn[0] = MN;
        strCmd = replaceTempData(strCmd, StrHeadType, 1, strMn, 0, 1);


        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = GB2005.iDataFormat(String.valueOf(getDataCmd(strCmd, "QN", "&&").length()), 4);
        strCmd = replaceTempData(strCmd, StrHeadType, 2, strLen, 0, 1);

        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(strCmd, "QN", "&&"), findCrc(strCmd));
        StrEnd[0] = strCmd.substring((strCmd.lastIndexOf("&&") + 2), (strCmd.lastIndexOf("&&") + 6));
        /*替换CRC*/
        strCmd = replaceTempData(strCmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
        return strCmd;
    }


    // 查找是什么校验算法
    public static int findCrc(String cmd) {
        //天津力合协议算法
        if (getPublicConfigData("GB2017_CRC").equals("0")) {
            return 1;
        } else if (getPublicConfigData("GB2017_CRC").equals("1")) {
            return 2;
        } else {
            return 0;
        }
    }
}
