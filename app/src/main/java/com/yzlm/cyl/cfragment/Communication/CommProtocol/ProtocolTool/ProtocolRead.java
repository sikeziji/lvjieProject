package com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool;

import android.util.Log;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.R;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.getNowSysTime;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastHistoryFlowData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitHigh;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.MeaModeSetting;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.workState;
import static java.lang.Integer.parseInt;

public class ProtocolRead {


    /*获取有没液体的类别**/
    public static int getNoLiquidErrorStatus(int nowErrorNum) {

        int[] noWater = new int[]{7, 27, 307, 327, 347, 367, 400, 401, 404, 411, 412, 441, 422};
        int[] noZero = new int[]{5, 25, 305, 325, 402, 403, 439, 420, 427};
        int[] noSpan = new int[]{6, 26, 306, 326, 421};
        int errorSum = noWater.length + noZero.length + noSpan.length;
        // 缺试剂类报错
        if ((nowErrorNum > 0 && nowErrorNum < 41) || (nowErrorNum == 259) || (nowErrorNum > 300 && nowErrorNum < 341) || (nowErrorNum > 399 && nowErrorNum < 405)
                || nowErrorNum == 411 || nowErrorNum == 412 || (nowErrorNum > 434 && nowErrorNum < 451) || (nowErrorNum > 416 && nowErrorNum < 420) || nowErrorNum == 423 || nowErrorNum == 421) {
            /*水样*/
            for (int i = 0; i < errorSum; i++) {
                if (i < noWater.length) {
                    if (nowErrorNum == noWater[i]) {
                        return 1;   // 缺水样
                    }
                }
                if (i < noZero.length) {
                    if (nowErrorNum == noZero[i]) {
                        return 2;   // 缺零样
                    }
                }
                if (i < noSpan.length) {
                    if (nowErrorNum == noSpan[i]) {
                        return 3;   // 缺标样
                    }
                }
            }
            return 4;// 缺试剂
        }
        return 5;// 其它
    }

    /*
       除缺试剂类的告警状态
       * **/
    public static int getOtherErrorStatus(int nowErrorNum) {
        int alarmCode = 0;
        if (nowErrorNum == 94) {
            alarmCode = 5;//漏液
        } else if (nowErrorNum == 533) {
            alarmCode = 6;//标定异常
        } else if (nowErrorNum == 90) {
            alarmCode = 8;//加热异常
        } else if (539 < nowErrorNum && nowErrorNum < 562) {
            alarmCode = 9;//低试剂预警
        } else if (569 < nowErrorNum && nowErrorNum < 573) {
            alarmCode = 7;//超量程告警
        } else {
            switch (nowErrorNum) {
                case 255:
                    alarmCode = 13;//滴定异常
                    break;
                case 257:
                    alarmCode = 14;//电极异常
                    break;
                case 531:
                case 583:
                    alarmCode = 10;//超上限
                    break;
                case 532:
                case 582:
                    alarmCode = 11;//超下限
                    break;
                case 529:
                    alarmCode = 15;//量程切换告警
                    break;
                case 429:
                    alarmCode = 22;// 定容异常（蒸馏平台）
                    break;
                case 562:
                    alarmCode = 23;// 废液量过多
                    break;
                case 104:
                    alarmCode = 24;// 掉电
                    break;
            }
        }
        return alarmCode;
    }


    /*故障代码**/
    public static int getDevFaultStatus(int nowErrorNum) {
        int alarmCode = 0;
        if ((nowErrorNum > 88 && nowErrorNum < 93) || (nowErrorNum == 102 || nowErrorNum == 103)) {
            alarmCode = 2;// 温度故障
        } else if ((nowErrorNum > 95 && nowErrorNum < 100) || (nowErrorNum == 256) || ((nowErrorNum > 260 && nowErrorNum < 281)) || (nowErrorNum == 520)
                || (nowErrorNum == 522)) {
            alarmCode = 3;//通讯故障
        } else if (nowErrorNum == 255 || nowErrorNum == 257 || nowErrorNum == 258 || nowErrorNum == 259) {
            alarmCode = 4;// 滴定故障
        }
        return alarmCode;
    }

    /*日志代码**/
    public static int getLogCode(String logNum) {
        int logCode = 0;
        switch (logNum) {
            case "105"://开机
                logCode = 1;
                break;
            case "596"://用户账号登录
                logCode = 2;
                break;
            case "597"://用户账号退出
                logCode = 3;
                break;
            case "598"://运维账号登录
                logCode = 4;
                break;
            case "599"://运维账号退出
                logCode = 5;
                break;
            case "540"://组合测试
                logCode = 6;
                break;
            case "600"://配置流程开启
                logCode = 7;
                break;
            case "601"://配置流程关闭
                logCode = 8;
                break;
            case "527"://标样1校准
                logCode = 9;
                break;
            case "528"://标样2校准
                logCode = 10;
                break;
            case "602"://零点核查
                logCode = 11;
                break;
            case "603"://标样核查
                logCode = 12;
                break;
            case "604"://跨度核查
                logCode = 13;
                break;
            case "524"://清洗
                logCode = 14;
                break;
            case "525"://冲洗
                logCode = 15;
                break;
            case "575"://稀释校准
                logCode = 16;
                break;
            case "586"://清除历史数据
                logCode = 17;
                break;
            case "587"://清除校准记录
                logCode = 18;
                break;
            case "588"://清除报警记录
                logCode = 19;
                break;
            case "589"://更新配置文件
            case "593"://
                logCode = 20;
                break;
            case "590"://标样3校准
                logCode = 21;
                break;
            case "591"://标样4校准
                logCode = 22;
                break;
            case "592"://更新测控板
                logCode = 23;
                break;
            case "594"://体积校准
                logCode = 24;
                break;
            case "595"://新增测量点
                logCode = 25;
                break;
            case "605"://维护运行模式
                logCode = 26;
                break;
            case "606"://在线运行模式
                logCode = 27;
                break;
            case "607"://离线运行模式
                logCode = 28;
                break;
            case "608"://定点测量模式
                logCode = 29;
                break;
            case "609"://连续测量模式
                logCode = 30;
                break;
            case "610"://周期测量模式
                logCode = 31;
                break;
            case "611"://手动测量模式
                logCode = 32;
                break;
            case "612"://受控测量模式
                logCode = 33;
                break;
            case "613"://紧急停止
                logCode = 34;
                break;
            case "614"://时间校正
                logCode = 35;
                break;
            case "619"://删除测量点
                logCode = 37;
                break;
            default:
                logCode = 36;// 内部运行日志
                break;
        }
        return logCode;
    }

    /*流程动作号转换为协议编码*/
    public static int getFlowActionCodeNum(int flowActionNum) {
        int code = 0;
        if (flowActionNum == 0 || flowActionNum == 253 || flowActionNum == 255) {
            //准备
            code = 0;
        } else if (((flowActionNum > 0 && flowActionNum < 41) || (flowActionNum > 80 && flowActionNum < 142) || (flowActionNum > 144 && flowActionNum < 147)
                || (flowActionNum > 258 && flowActionNum < 297))) {
            //进样
            code = 2;
        } else if ((flowActionNum > 1030 && flowActionNum < 1071) || (flowActionNum > 1090 && flowActionNum < 1111) || (flowActionNum == 1025) || (flowActionNum == 1027)
                || (flowActionNum > 1190 && flowActionNum < 1194) || (flowActionNum == 1200) || flowActionNum == 1202) {
            // 进样
            code = 2;
        } else if ((flowActionNum > 40 && flowActionNum < 81) || (flowActionNum > 150 && flowActionNum < 253) || (flowActionNum > 522 && flowActionNum < 543)
                || (flowActionNum > 1193 && flowActionNum < 1197) || flowActionNum == 1201 || flowActionNum == 1203) {
            //冲洗
            code = 1;
        } else if (flowActionNum > 1070 && flowActionNum < 1091) {
            //冲洗
            code = 1;
        } else if ((flowActionNum > 141 && flowActionNum < 145) || (flowActionNum > 146 && flowActionNum < 151) || (flowActionNum == 254 || flowActionNum == 256)) {
            // 排液
            code = 4;
        } else if ((flowActionNum > 1110 && flowActionNum < 1151) || (flowActionNum == 317 || flowActionNum == 319)) {
            // 排液
            code = 4;
        } else if ((flowActionNum > 513 && flowActionNum < 519 || flowActionNum > 542 && flowActionNum < 545) || (flowActionNum == 576)) {
            //消解
            code = 5;
        } else if ((flowActionNum > 518 && flowActionNum < 523) || (flowActionNum == 545) || (flowActionNum == 548)) {
            // 测量
            code = 8;
        }
        return code;
    }


    /*根据当前执行流程转换为协议编码**/
    public static int getNowWorkStatus(String workStatus) {

        if (workStatus.contentEquals(context.getText(R.string.waiting_for_instructions))) {
            return 0;
        } else if (workStatus.contentEquals(context.getText(R.string.ZY))) {
            return 1;
        } else if (workStatus.contentEquals(context.getText(R.string.BYCL)) || (workStatus.contentEquals(context.getText(R.string.BY2CL)))) {
            return 4;
        } else if (workStatus.contentEquals(context.getText(R.string.YBQX))) {
            return 5;
        } else if (workStatus.contentEquals(context.getText(R.string.CSZY))) {
            return 6;
        } else if (workStatus.contentEquals(context.getText(R.string.LDHC))) {
            return 7;
        } else if (workStatus.contentEquals(context.getText(R.string.KDHC))) {
            return 8;
        } else if (workStatus.contentEquals(context.getText(R.string.PXY))) {
            return 9;
        } else if (workStatus.contentEquals(context.getText(R.string.LYCL))) {
            return 10;
        } else if (workStatus.contentEquals(context.getText(R.string.JBHS))) {
            return 11;
        } else {
            return 0;
        }
    }

    /*
     * 工作状态
     */
    public static int getJobStatus(String compName) {

        //异常处理
        if (doFlowing.get(compName).contentEquals(context.getText(R.string.exceptionHandling))) {
            return 31;
        } /*故障*/
        if (!workState.get(compName).contentEquals(context.getText(R.string.normal))) {
            return 30;
        }
        /*空闲 */
        if (doFlowing.get(compName).contentEquals(context.getText(R.string.waiting_for_instructions))) {
            return 0;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.ZY))) {
            return 1;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.BYHC))) {
            return 2;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.LDHC))) {
            return 3;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.KDHC))) {
            return 4;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.LYCL))) {
            return 5;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.PXY))) {
            return 6;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.JBHS))) {
            return 7;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.YBQX))) {
            return 10;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.B1))) {
            return 19;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.BYCL))) {
            return 28;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.BY2CL))) {
            return 29;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.CSZY))) {
            return 32;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.B2))) {
            return 34;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.GLCX))) {
            return 35;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.GYWDX))) {
            return 36;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.JYWDX))) {
            return 37;
        } else if (doFlowing.get(compName).contentEquals(context.getText(R.string.dilutionCal))) {
            return 38;
        } else {
            return 33;
        }
    }

    /*
     * 自动校准状态
     *  true:自动  false：手动
     * **/
    public static String getAutoJustStatus(String compName) {
        return (getConfigData(compName, "ZDJZ"));
    }

    /*
     * 自动清洗状态
     *  true:自动  false：手动
     * **/
    public static String getAutoCleanStatus(String compName) {
        return getConfigData(compName, "ZDQX");
    }

    /*// 1 在线 2 离线 3 维护
     * **/
    public static int getRunningMode(String compName) {
        return Integer.parseInt(getConfigData(compName, "runningMode"));
    }

    /*// // 1 连续测量模式  2、周期测量模式  3、定点模式  4、受控模式  、手动模式
     * **/
    public static int geMeaMode(String compName) {
        return Integer.parseInt(getConfigData(compName, "meaMode"));
    }


    /**
     * 仪器测量模式  1 连续测量模式  2、周期测量模式  3、定点模式  4、受控模式  5、手动模式  6 设备离线模式下测量模式无效
     */
    public static int getDevMeaMode(String compName) {

        int iRunningMode = getRunningMode(compName);
        int iMeaMode = geMeaMode(compName);
        int iDevMeaMode = 0;
        switch (iRunningMode) {
            case 1://在线
                iDevMeaMode = iMeaMode;
                break;
            case 2:
                iDevMeaMode = 6;
                break;
            case 3:
                switch (iMeaMode) {
                    case 1:// 连续
                        iDevMeaMode = 1;
                        break;
                    case 2://周期
                        iDevMeaMode = 2;
                        break;
                    case 3://定点
                        iDevMeaMode = 3;
                        break;
                    case 4://受控
                        iDevMeaMode = 5;
                        break;
                }
                break;
        }
        return iDevMeaMode;
    }


    /*在线模式下设置测量模式**/
    public static boolean setOnLineModeSetMeaMode(String compName, int iMode) {

        int iRunningMode = getRunningMode(compName);

        if (iRunningMode == 1) {//在线模式下
            //设置测量模式
            return setMeaMode(compName, iMode);
        } else {
            return false;
        }
    }

    /*
    设置测量模式
    * **/
    public static boolean setMeaMode(String compName, int iMode) {
        if (iMode > 0 && iMode < 5) {
            updateConfigData(compName, "meaMode", String.valueOf(iMode));
            MeaModeSetting(compName, String.valueOf(iMode));
            saveOperationLogDataModifyMsg(compName, "meaMode", String.valueOf(iMode), "协议修改-测量模式", ErrorLog.msgType.操作_信息);
        } else {
            return false;
        }
        return true;
    }


    /*
        自动清洗周期
    * **/
    public static int getAutoCleanTimeSetting(String compName) {
        return Integer.parseInt(getConfigData(compName, "zdqxh"));
    }


    /*
    获取自动校准天数
    * ***/
    public static int getAutoJustSettingDay(String compName) {
        return Integer.parseInt(getConfigData(compName, "zdjzt"));
    }

    /*
        获取自动校准 时间点
        * ***/
    public static int getAutoJustSettingHour(String compName) {


        return Integer.parseInt(getConfigData(compName, "zdjzh"));
    }

    /*
    获取自动校准间隔小时
    * ***/
    public static int getAutoJustSettingSpaceHour(String compName) {
        return Integer.parseInt(getConfigData(compName, "zdjzt")) * 24;
    }


    /**
     * 测量精度，小数点位数
     */
    public static int getMeaPoint(String compName) {
        int point;
        try {
            point = Integer.parseInt(getConfigData(compName, "YXWS"));
        } catch (Exception e) {
            point = 2;
        }
        return point;
    }


    /*
     * 获取仪器量程
     * **/
    public static int getDevNowRangeNum(String compName) {

        return Integer.parseInt(getConfigData(compName, "RANGE"));
    }

    /*
     * 获取当前量程L
     * **/
    public static String getNowRangeL(String compName) {
        int iNowRange = getDevNowRangeNum(compName);
        return getConfigData(compName, "LC" + iNowRange + "L");
    }

    /*
     * 获取当前量程H
     * **/
    public static String getNowRangeH(String compName) {
        int iNowRange = getDevNowRangeNum(compName);
        return getConfigData(compName, "LC" + iNowRange + "H");
    }

    /*
     * 获取仪表量程L
     * **/
    public static String getDevRangeL(String compName) {

        return getConfigData(compName, "YBLCL");
    }

    /*
     * 获取仪表量程H
     * **/
    public static String getDevRangeH(String compName) {

        return getConfigData(compName, "YBLCH");
    }


    /*
    获取仪器消解温度
    * **/
    public static String getHeatTemp(String compName) {
        return getConfigData(compName, "xjwd").equals("") ? "0" : getConfigData(compName, "xjwd");
    }

    /*
      获取仪器消解时长
      * **/
    public static String getHeatTime(String compName) {
        return getConfigData(compName, "xjsc").equals("") ? "0" : getConfigData(compName, "xjsc");
    }

    /*
    获取仪器显色温度
    * **/
    public static String getColorTemp(String compName) {
        return getConfigData(compName, "xswd").equals("") ? "0" : getConfigData(compName, "xswd");
    }

    /*
      获取仪器显色时长
      * **/
    public static String getColorTime(String compName) {
        return getConfigData(compName, "xssc").equals("") ? "0" : getConfigData(compName, "xssc");
    }

    /*
    获取仪器消解降温温度
    * **/
    public static String getHeatCoolTemp(String compName) {
        return getConfigData(compName, "xjjw").equals("") ? "0" : getConfigData(compName, "xjjw");
    }

    /*
    获取仪器显色降温温度
    * **/
    public static String getColorCoolTemp(String compName) {
        return getConfigData(compName, "xsjw").equals("") ? "0" : getConfigData(compName, "xsjw");
    }

    /*
    获取仪器显色静置
    * **/
    public static String getColorStandTime(String compName) {
        return getConfigData(compName, "xsjz").equals("") ? "0" : getConfigData(compName, "xsjz");
    }

    /*获取量程1的标1的设置值
     * **/
    public static String getRange1Cal1SettingValue(String compName) {
        return getConfigData(compName, "C0");
    }

    /*获取量程2的标1的设置值
     * **/
    public static String getRange2Cal1SettingValue(String compName) {
        return getConfigData(compName, "C2");
    }

    /*获取量程3的标1的设置值
     * **/
    public static String getRange3Cal1SettingValue(String compName) {
        return getConfigData(compName, "C4");
    }

    /*获取量程1的标2的设置值
     * **/
    public static String getRange1Cal2SettingValue(String compName) {
        return getConfigData(compName, "C1");
    }

    /*获取量程2的标2的设置值
     * **/
    public static String getRange2Cal2SettingValue(String compName) {
        return getConfigData(compName, "C3");
    }

    /*获取量程3的标2的设置值
     * **/
    public static String getRange3Cal2SettingValue(String compName) {
        return getConfigData(compName, "C5");
    }

    /*
    读取间隔测量分钟值
    * */
    public static String getContinueMeaSetting(String compName) {
        if ((!getConfigData(compName, "lxclh").equals(""))
                && (!getConfigData(compName, "lxclm").equals(""))) {
            int hour = parseInt(getConfigData(compName, "lxclh"));
            int min = parseInt(getConfigData(compName, "lxclm"));
            return String.valueOf(hour * 60 + min);
        } else {
            return null;
        }
    }

    /*
     * 周期测量设置
     *
     */
    public static String getCycMeaSetting(String compName) {
        long tnum = 0;
        if (!getConfigData(compName, "zqclh").equals("")) {
            String[] strCycTime = getConfigData(compName, "zqclh").split("[-，]");
            for (String aStrCycTime : strCycTime) {
                tnum |= 1 << Integer.parseInt(aStrCycTime);
            }
            return String.valueOf(tnum);
        } else {
            return null;
        }
    }


    /*
     * 获取消解管温度
     * **/
    public static String getDigestionTubeTemp(String compName) {

        return (getCmds(compName).getCmd(55).getValue() == null ? "0" : String.valueOf(getCmds(compName).getCmd(55).getValue()));
    }

    /*
     * 获取当前量程标样2浓度
     * **/
    public static String getSpan2Value(String compName) {
        String strValue = getConfigData(compName, "C" + ((Integer.parseInt(getConfigData(compName, "RANGE")) - 1) * 2 + 1));
        return strValue.equals("") ? null : strValue;
    }

    /*
     * 获取当前量程标样1浓度
     * **/
    public static String getSpan1Value(String compName) {
        String strValue = getConfigData(compName, "C" + ((Integer.parseInt(getConfigData(compName, "RANGE")) - 1) * 2));
        return strValue.equals("") ? null : strValue;
    }

    /*
    获取当前量程K
    * **/
    public static String getNowRangeK(String compName) {
        return String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "K"));

    }

    /*
        获取当前量程B
        * **/
    public static String getNowRangeB(String compName) {
        return String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "B"));

    }

    /*
    获取当前量程F
    * **/
    public static String getNowRangeF(String compName) {
        return String.valueOf(getNewestKBF(compName, getConfigData(compName, "RANGE"), "F"));

    }

    /*
    获取当前量程校正因子
    * **/
    public static String getNowRangeExtRangeK(String compName) {
        return String.valueOf(getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "K"));

    }

    /*
    获取当前量程修正因子
    * **/
    public static String getNowRangeExtRangeB(String compName) {
        return String.valueOf(getConfigData(compName, "ExtRange" + getConfigData(compName, "RANGE") + "B"));

    }

    /*
     * 获取组分测量检出限
     * **/
    public static String getMeaLimitHigh(String compName) {
        String strLimitHigh = getConfigData(compName, "limitHigh");
        if (strLimitHigh == null || strLimitHigh.equals("")) {
            strLimitHigh = String.valueOf(getLimitHigh(compName));// 默认检出限上限值
        }
        return strLimitHigh;
    }


    /*
    获取最近一次校准标1的校准结果状态  0:未校准过，1：失败，2：成功
    * **/
    public static String getCal1Status(String compName) {
        String strZeroResult;
        strZeroResult = getConfigData(compName, "CAL_Suc_Flag");
        if (strZeroResult.equals("")) {
            strZeroResult = "0";
        } else if (strZeroResult.equals("true")) {
            strZeroResult = "2";
        } else {
            strZeroResult = "1";
        }
        return strZeroResult;
    }

    /*
    获取 测量 吸光度
    * **/
    public static String getCalVol(String compName) {
        String calVol = "0";
        List<Map> listCal;
        listCal = getLastHistoryFlowData(compName, context.getString(R.string.ZY));
        if (listCal.size() > 0) {
            calVol = String.valueOf(Float.parseFloat(listCal.get(0).get("A").toString()));
        }
        return calVol;
    }


    /*
    获取校准 标1的吸光度
    * **/
    public static String getCal1Vol(String compName) {
        String cal1Vol = "0";
        List<Map> listCal1;
        listCal1 = getLastHistoryFlowData(compName, context.getString(R.string.B1));
        if (listCal1.size() > 0) {
            cal1Vol = String.valueOf(Float.parseFloat(listCal1.get(0).get("A").toString()));
        }
        return cal1Vol;
    }

    /*
        获取校准 标1的浓度值
        * **/
    public static String getCal1Value(String compName) {
        List<Map> listCal1;
        listCal1 = getLastHistoryFlowData(compName, context.getString(R.string.B1));
        String cal1Value = "0";
        if (listCal1.size() > 0) {
            cal1Value = String.valueOf(Float.parseFloat(listCal1.get(0).get("C").toString()));
        }
        return cal1Value;
    }

    /*
      获取校准 标2的吸光度
      * **/
    public static String getCal2Vol(String compName) {
        String cal2Vol = "0";
        List<Map> listCal2;
        listCal2 = getLastHistoryFlowData(compName, context.getString(R.string.B2));
        if (listCal2.size() > 0) {
            cal2Vol = String.valueOf(Float.parseFloat(listCal2.get(0).get("A").toString()));
        }
        return cal2Vol;
    }

    /*
        获取校准 标2的浓度值
        * **/
    public static String getCal2Value(String compName) {
        List<Map> listCal2;
        listCal2 = getLastHistoryFlowData(compName, context.getString(R.string.B2));
        String cal2Value = "0";
        if (listCal2.size() > 0) {
            cal2Value = String.valueOf(Float.parseFloat(listCal2.get(0).get("C").toString()));
        }
        return cal2Value;
    }


    /*获取标1的数据时间
     * **/
    public static String getCal1DataTime(String compName) {

        List<Map> listSpan2;
        listSpan2 = getLastHistoryFlowData(compName, context.getString(R.string.B1));
        String cal1_time = "";
        if (listSpan2.size() > 0) {
            String sTime = listSpan2.get(0).get("time").toString();
            cal1_time = sTime.replace("-", "").replace(":", "").replace("：", "").replace(" ", "");
        }
        return cal1_time;
    }

    /*获取标2的数据时间
     * **/
    public static String getCal2DataTime(String compName) {

        List<Map> listSpan2;
        listSpan2 = getLastHistoryFlowData(compName, context.getString(R.string.B2));
        String cal2_time = "";
        if (listSpan2.size() > 0) {
            String sTime = listSpan2.get(0).get("time").toString();
            cal2_time = sTime.replace("-", "").replace(":", "").replace("：", "").replace(" ", "");
        }
        return cal2_time;
    }

    /*校准成功时间*/
    public static String getCalSucTime(String compName) {
        String calTime = getConfigData(compName, "CAL_SUC_TIME").replace(":", "").replace("-", "").replace(" ", "");
        if (calTime.equals("")) {
            calTime = "00000000000000";
        }
        return calTime;
    }


    /*
     * 获取不带有ms的系统时间字符串  190101120035
     * */
    public static String getStrNowSysTemTimeWithOutms() {

        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.valueOf(time[0]);
        int month = Integer.valueOf(time[1]);
        int day = Integer.valueOf(time[2]);
        int hour = Integer.valueOf(time[3]);
        int min = Integer.valueOf(time[4]);
        int sec = Integer.valueOf(time[5]);
        int ms = Integer.valueOf(time[6]);
        return getNowSysTime(year, month, day, hour, min, sec);

    }

    /*
     * 获取带有ms的系统时间字符串  190101120035111
     * */
    public static String getStrNowSysTemTime() {

        String[] time = getNowSysTime().split("[:/.]");
        int year = Integer.valueOf(time[0]);
        int month = Integer.valueOf(time[1]);
        int day = Integer.valueOf(time[2]);
        int hour = Integer.valueOf(time[3]);
        int min = Integer.valueOf(time[4]);
        int sec = Integer.valueOf(time[5]);
        int ms = Integer.valueOf(time[6]);
        return getNowSysTime(year, month, day, hour, min, sec, ms);

    }


    /*
     * flowName  : 做样  ，null
     * */
    public static DataStruct getLastHistory(String component, String flowName) {
        History mHistory;
        mHistory = new History(context);
        List<Map> list;
        DataStruct data = new DataStruct();
        list = mHistory.select(component, flowName, null, null, 0, 1);
        if (list.size() > 0) {
            String timeStr = list.get(0).get("time").toString();
            timeStr = timeStr.replace(" ", "-");
            String[] time = timeStr.split("[:-]");
            int year = Integer.valueOf(time[0]);
            int month = Integer.valueOf(time[1]);
            int day = Integer.valueOf(time[2]);
            int hour = Integer.valueOf(time[3]);
            int min = Integer.valueOf(time[4]);
            int sec = Integer.valueOf(time[5]);
            data.setDataTime(year, month, day, hour, min, sec);
            data.setData(Float.parseFloat(list.get(0).get("C").toString()));
            data.setType(list.get(0).get("component").toString());
            try {
                data.setAi(Float.parseFloat(list.get(0).get("A").toString()));
                data.setDataTag(list.get(0).get("tag").toString());
                data.setFlow(list.get(0).get("flow").toString());
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
        }
        return data;
    }


    /*
    获取测量时间
    * **/
    public static String getStringMeaDataTime(String cmd, String key, DataStruct DataInfo) {
        cmd = cmd.replace(key, getDataTime(DataInfo.getYear(), DataInfo.getMonth(), DataInfo.getDay(), DataInfo.getHour(), DataInfo.getMin(), DataInfo.getSec()));
        return cmd;
    }


    /**
     * 获取组分单位编码，（此编码按照国家地表水通讯协议执行）
     *
     * @param compName 组分
     * @return
     */
    public static int getComponentUnitCode(String compName) {
        int iUnit = 1;
        switch (getComponentUnit(compName)) {
            case "mg/L":
                iUnit = 1;
                break;
            case "μg/L":
                iUnit = 0;
                break;
            case "ppb":
                iUnit = 5;
                break;
            case "ppm":
                iUnit = 2;
                break;
            case "%":
                iUnit = 7;
                break;
        }

        return iUnit;
    }


    /**
     * 获取组分单位 mg/L  ug/l  ppm等
     *
     * @param compName
     * @return
     */
    public static String getComponentUnit(String compName) {
        return getConfigData(compName, "UNIT");
    }
}
